package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.enitiy.avatar.effect.{Effect, FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.avatar.motion.{Expression, Motion, MotionWithTransition}
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
  private val newExpressions = Expression.createExpressions(avatarSettings)

  private var effects: List[Effect] = Nil
  private var newExpression: Motion = null

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
    newExpressions.get(name).foreach { expressions =>
      this.newExpression = new MotionWithTransition(expressions)
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
    //expressionManager.DoUpdateMotion(model, frameTimeInfo.totalElapsedTimeInSeconds)
    val expressionsOperations = if (newExpression == null) Nil else newExpression.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds, 1)
    val operations = effects.flatMap(_.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds))

    executeOperations(model, expressionsOperations ++ operations)

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
