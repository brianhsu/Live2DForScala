package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarExpressionReader
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{AvatarMotion, Expression, MotionManager, MotionWithTransition}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateStrategy

class BasicStrategy(avatarSettings: Settings, protected val model: Live2DModel) extends UpdateStrategy {

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

  def startMotion(motionSetting: MotionSetting): MotionWithTransition = {
    val motion = AvatarMotion(motionSetting, avatarSettings.eyeBlinkParameterIds, avatarSettings.lipSyncParameterIds)
    motionManager.startMotion(motion)
  }

  def startMotion(motionGroupName: String, indexInsideGroup: Int): Option[MotionWithTransition] = {
    for {
      motionGroup <- avatarSettings.motionGroups.get(motionGroupName) if motionGroup.size > indexInsideGroup && indexInsideGroup >= 0
      motionSetting = motionGroup(indexInsideGroup)
    } yield {
      startMotion(motionSetting)
    }
  }

  def startExpression(expression: Expression): MotionWithTransition = {
    expressionManager.startMotion(expression)
  }

  def startExpression(name: String): Option[MotionWithTransition] = {
    expressions.get(name).map(startExpression)
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
