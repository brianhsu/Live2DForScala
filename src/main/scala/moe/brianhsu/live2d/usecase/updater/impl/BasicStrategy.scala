package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarExpressionReader
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{AvatarMotion, MotionManager}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateStrategy
import org.slf4j.LoggerFactory

class BasicStrategy(avatarSettings: Settings, protected val model: Live2DModel) extends UpdateStrategy {

  private val defaultLogger = LoggerFactory.getLogger(this.getClass)
  private val expressions = new AvatarExpressionReader(avatarSettings).loadExpressions
  private val expressionManager = new MotionManager
  private val motionManager = new MotionManager
  private var effects: List[Effect] = Nil

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
    val motion = AvatarMotion(motionSettings, avatarSettings.eyeBlinkParameterIds, avatarSettings.lipSyncParameterIds)
    defaultLogger.debug(s"Start motion $name")
    motionManager.startMotion(motion)
  }

  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expressions =>
      defaultLogger.debug(s"Start $name expression")
      this.expressionManager.startMotion(expressions)
    }
  }

  private def executeMotionOperations(frameTimeInfo: FrameTimeInfo): Unit = {
    val operations = motionManager.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds, 1)
    executeOperations(model, operations)
  }

  private def executeExpressionOperations(frameTimeInfo: FrameTimeInfo): Unit = {
    val operations = expressionManager.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds, 1)
    executeOperations(model, operations)
  }

  private def executeEffectsOperations(frameTimeInfo: FrameTimeInfo): Unit = {
    val operations = for {
      effect <- effects
      operation <- effect.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds)
    } yield {
      operation
    }

    executeOperations(model, operations)
  }

  override def update(frameTimeInfo: FrameTimeInfo): Unit = {

    model.restoreParameters()
    executeMotionOperations(frameTimeInfo)
    model.snapshotParameters()

    executeExpressionOperations(frameTimeInfo)
    executeEffectsOperations(frameTimeInfo)

    model.update()
  }
}
