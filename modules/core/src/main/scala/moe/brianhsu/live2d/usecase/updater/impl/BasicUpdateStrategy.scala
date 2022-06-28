package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarExpressionReader
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{AvatarMotion, Expression, MotionManager, MotionWithTransition}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.{FrameTimeInfo, ModelUpdater, UpdateStrategy, Updater}

class BasicUpdateStrategy(val avatarSettings: Settings,
                          val model: Live2DModel,
                          val expressionReader: AvatarExpressionReader,
                          val expressionManager: MotionManager,
                          val motionManager: MotionManager,
                          updater: Updater) extends UpdateStrategy {


  private val expressions = expressionReader.loadExpressions
  var effects: List[Effect] = Nil

  def this(avatarSettings: Settings, model: Live2DModel) = {
    this(
      avatarSettings, model,
      new AvatarExpressionReader(avatarSettings),
      expressionManager = new MotionManager,
      motionManager = new MotionManager,
      updater = new ModelUpdater(model)
    )
  }

  def startMotion(motionSetting: MotionSetting, isLoop: Boolean): MotionWithTransition = {
    val motion = AvatarMotion(motionSetting, avatarSettings.eyeBlinkParameterIds, avatarSettings.lipSyncParameterIds, isLoop)
    motionManager.startMotion(motion)
  }

  def startMotion(motionGroupName: String, indexInsideGroup: Int, isLoop: Boolean): Option[MotionWithTransition] = {
    for {
      motionGroup <- avatarSettings.motionGroups.get(motionGroupName) if motionGroup.size > indexInsideGroup && indexInsideGroup >= 0
      motionSetting = motionGroup(indexInsideGroup)
    } yield {
      startMotion(motionSetting, isLoop)
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
    updater.executeOperations(operations)
  }

  private def executeExpressionOperations(frameTimeInfo: FrameTimeInfo): Unit = {
    val operations = expressionManager.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds, 1)
    updater.executeOperations(operations)
  }

  private def executeEffectsOperations(frameTimeInfo: FrameTimeInfo): Unit = {
    val operations = for {
      effect <- effects
      operation <- effect.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds)
    } yield {
      operation
    }

    updater.executeOperations(operations)
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
