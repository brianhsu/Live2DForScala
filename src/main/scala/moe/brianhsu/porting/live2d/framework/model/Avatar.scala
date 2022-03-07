package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.enitiy.avatar.effect.{AddOperation, FunctionalEffect}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.{FrameTimeInfo, UpdateStrategy}
import moe.brianhsu.porting.live2d.framework.{CubismExpressionMotion, CubismMotion, CubismMotionManager, Pose}
import moe.brianhsu.porting.live2d.framework.effect.Effect
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.slf4j.LoggerFactory

class DefaultStrategy(avatarSettings: Settings, protected val model: Live2DModel) extends UpdateStrategy {

  private val defaultLogger = LoggerFactory.getLogger(this.getClass)

  private var effects: List[Effect] = Nil
  private val expressionManager = new CubismMotionManager
  private val motionManager = new CubismMotionManager
  private val expressions = CubismExpressionMotion.createExpressions(avatarSettings)

  private var functionalEffects: List[FunctionalEffect] = Nil

  def setFunctionalEffects(effects: List[FunctionalEffect]): Unit = {
    this.functionalEffects = effects
  }

  def setEffects(effects: List[Effect]): Unit = {
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
    motionManager.StartMotionPriority(motion, autoDelete = false, 2)
  }

  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expression =>
      defaultLogger.info(s"Start $name expression")
      expressionManager.StartMotionPriority(expression, autoDelete = false, 3)
    }
  }

  override def update(frameTimeInfo: FrameTimeInfo): Unit = {
    model.restoreParameters()
    if (motionManager.IsFinished()) {
      // Start Random Motion
    } else {
      motionManager.UpdateMotion(model, frameTimeInfo.deltaTimeInSeconds)
    }
    model.snapshotParameters()
    expressionManager.UpdateMotion(model, frameTimeInfo.deltaTimeInSeconds)
    effects.foreach(_.updateParameters(model, frameTimeInfo.deltaTimeInSeconds))
    functionalEffects.foreach { effect =>
      val operations = effect.calculateOperations(frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds)
      operations.foreach {
        case AddOperation(parameterId, value, weight) => model.parameters.get(parameterId).foreach(_.add(value, weight))
        case _ => println("Unknown Operation")
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
