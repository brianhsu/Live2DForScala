package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.enitiy.avatar.effect.{Effect, FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.{FrameTimeInfo, UpdateStrategy}
import moe.brianhsu.porting.live2d.framework.{CubismExpressionMotion, CubismMotion, CubismMotionQueueManager}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.slf4j.LoggerFactory

class DefaultStrategy(avatarSettings: Settings, protected val model: Live2DModel) extends UpdateStrategy {

  private val defaultLogger = LoggerFactory.getLogger(this.getClass)

  private val expressionManager = new CubismMotionQueueManager
  private val motionManager = new CubismMotionQueueManager
  private val expressions = CubismExpressionMotion.createExpressions(avatarSettings)

  private var effects: List[Effect] = Nil

  def setFunctionalEffects(effects: List[Effect]): Unit = {
    this.effects = effects
  }

  def appendEffect(effect: Effect): Unit = {
    this.effects = effects.appended(effect)
  }

  def removeEffect(effect: Effect): Unit = {
    this.effects = effects.filterNot(_ == effect)
  }

  def startMotion(motionGroup: String, index: Int): Unit = {
    val name = s"Motion(${motionGroup}_$index)"
    val motionSettings = avatarSettings.motionGroups(motionGroup)(index)
    val motion = CubismMotion(motionSettings, _ => defaultLogger.info(s"$name has finished"), avatarSettings.eyeBlinkParameterIds, Nil)
    defaultLogger.info(s"Start motion $name")
    motionManager.StartMotion(motion)
  }

  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expression =>
      defaultLogger.info(s"Start $name expression")
      expressionManager.StartMotion(expression)
    }
  }

  override def update(frameTimeInfo: FrameTimeInfo): Unit = {
    model.restoreParameters()
    if (motionManager.IsFinished()) {
      // Start Random Motion
    } else {
      motionManager.DoUpdateMotion(model, frameTimeInfo.totalElapsedTimeInSeconds)
    }
    model.snapshotParameters()
    expressionManager.DoUpdateMotion(model, frameTimeInfo.totalElapsedTimeInSeconds)
    effects.foreach { effect =>
      val operations = effect.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds)
      operations.foreach {
        case ParameterValueAdd(parameterId, value, weight) => model.parameters.get(parameterId).foreach(_.add(value, weight))
        case ParameterValueUpdate(parameterId, value, weight) => model.parameters.get(parameterId).foreach(_.update(value, weight))
        case FallbackParameterValueAdd(parameterId, value, weight) => model.parameterWithFallback(parameterId).update(value, weight)
        case FallbackParameterValueUpdate(parameterId, value, weight) => model.parameterWithFallback(parameterId).update(value, weight)
        case PartOpacityUpdate(partId, value) => model.parts.get(partId).foreach(_.opacity = value)
      }
    }

    model.update()
  }
}

/**
 * This class represent a complete Live 2D Cubism Avatar runtime model.
 *
 */
class Avatar(val avatarSettings: Settings, val model: Live2DModel) {

  var updateStrategyHolder: Option[UpdateStrategy] = None

  /**
   * Update Live2D model parameters of this avatar according to time in seconds elapsed
   * from last update.
   *
   * The actually update implementation will be controlled by [[UpdateStrategy]] inside [[updateStrategyHolder]].
   *
   * @param frameTimeInfo The FrameTimeInfo object tells us how about frame time information.
   */
  def update(frameTimeInfo: FrameTimeInfo): Unit = {
    updateStrategyHolder.foreach(_.update(frameTimeInfo))
  }
}
