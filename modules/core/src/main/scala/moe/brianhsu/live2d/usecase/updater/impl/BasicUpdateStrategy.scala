package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.{AvatarExpressionReader, AvatarMotionDataReader}
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.RepeatedCallback
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{AvatarMotion, Expression, MotionManager, MotionWithTransition}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.{FrameTimeInfo, ModelUpdater, UpdateStrategy, Updater}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy.{EffectTiming, MotionListener}

object BasicUpdateStrategy {

  sealed trait EffectTiming

  object EffectTiming {
    case object BeforeExpression extends EffectTiming
    case object AfterExpression extends EffectTiming
  }

  trait MotionListener {
    def onMotionStart(motion: MotionSetting): Unit
  }
}
class BasicUpdateStrategy(val avatarSettings: Settings,
                          val model: Live2DModel,
                          val expressionReader: AvatarExpressionReader,
                          val expressionManager: MotionManager,
                          val motionManager: MotionManager,
                          val motionListener: Option[MotionListener],
                          updater: Updater) extends UpdateStrategy {


  private val expressions = expressionReader.loadExpressions
  private var beforeExpressionEffects: List[Effect] = Nil
  private var afterExpressionEffects: List[Effect] = Nil

  def this(avatarSettings: Settings, model: Live2DModel, motionListener: Option[MotionListener]) = {
    this(
      avatarSettings, model,
      new AvatarExpressionReader(avatarSettings),
      expressionManager = new MotionManager,
      motionManager = new MotionManager,
      motionListener,
      updater = new ModelUpdater(model),
    )
  }

  def effects(timing: EffectTiming) = timing match {
    case BeforeExpression => this.beforeExpressionEffects
    case AfterExpression => this.afterExpressionEffects
  }

  def findEffects(predicate: Effect => Boolean, timing: EffectTiming = BeforeExpression): List[Effect] = {
    timing match {
      case BeforeExpression => beforeExpressionEffects.filter(predicate)
      case AfterExpression => afterExpressionEffects.filter(predicate)
    }
  }

  def appendAndStartEffects(effect: List[Effect], timing: EffectTiming = BeforeExpression): Unit = {

    timing match {
      case BeforeExpression => this.beforeExpressionEffects ++= effect
      case AfterExpression => this.afterExpressionEffects ++= effect
    }

    effect.foreach(_.start())
  }

  def stopAndRemoveEffects(predicate: Effect => Boolean, timing: EffectTiming = BeforeExpression): List[Effect] = {
    val removed = timing match {
      case BeforeExpression =>
        val (removed, remains) = this.beforeExpressionEffects.partition(predicate)
        this.beforeExpressionEffects = remains
        removed

      case AfterExpression =>
        val (removed, remains) = this.afterExpressionEffects.partition(predicate)
        this.afterExpressionEffects = remains
        removed
    }
    removed.foreach(_.stop())
    removed
  }

  def startMotion(motionSetting: MotionSetting, isLoop: Boolean): MotionWithTransition = {
    val avatarMotionDataReader = new AvatarMotionDataReader(motionSetting)
    val motion = AvatarMotion(avatarMotionDataReader, motionSetting, avatarSettings.eyeBlinkParameterIds, avatarSettings.lipSyncParameterIds, isLoop)
    motionListener.foreach(_.onMotionStart(motionSetting))

    if (isLoop) {
      val callbackHolder: Option[RepeatedCallback] = motionListener.map(c => (_: MotionWithTransition) => c.onMotionStart(motionSetting))
      motionManager.repeatedCallbackHolder = callbackHolder
    } else {
      motionManager.repeatedCallbackHolder = None
    }

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

  private def executeEffectsOperations(effects: List[Effect], frameTimeInfo: FrameTimeInfo): Unit = {
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

    executeEffectsOperations(beforeExpressionEffects, frameTimeInfo)
    executeExpressionOperations(frameTimeInfo)
    executeEffectsOperations(afterExpressionEffects, frameTimeInfo)

    model.update()
  }
}
