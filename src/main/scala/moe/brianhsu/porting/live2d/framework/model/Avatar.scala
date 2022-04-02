package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.adapter.gateway.avatar.motion.AvatarExpressionReader
import moe.brianhsu.live2d.adapter.gateway.avatar.physics.AvatarPhysicsReader
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.motion.MotionEvent
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.{AvatarMotion, MotionManager, MotionWithTransition}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.updater.{FrameTimeInfo, UpdateOperation, UpdateStrategy}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.model.DefaultStrategy.enablePhy
import moe.brianhsu.porting.live2d.physics.CubismPhysics
import org.slf4j.LoggerFactory
object DefaultStrategy {
  var enablePhy: Boolean = false
}
class DefaultStrategy(avatarSettings: Settings, protected val model: Live2DModel) extends UpdateStrategy {

  private val defaultLogger = LoggerFactory.getLogger(this.getClass)
  private val expressions = new AvatarExpressionReader(avatarSettings).loadExpressions
  private var effects: List[Effect] = Nil
  private val expressionManager = new MotionManager
  private val newMotionManager = new MotionManager
  private val physicsHolder = new AvatarPhysicsReader(avatarSettings).loadPhysics

  newMotionManager.setEventCallbackForAllMotions((m: MotionWithTransition, e:MotionEvent) => {
    println("motion:" + m)
    println("motionEvent:" + e)
  })

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
    val motion = AvatarMotion(motionSettings, avatarSettings.eyeBlinkParameterIds, avatarSettings.lipSyncParameterIds)
    defaultLogger.info(s"Start motion $name")
    newMotionManager.startMotion(motion)
  }

  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expressions =>
      defaultLogger.info(s"Start $name expression")
      this.expressionManager.startMotion(expressions)
    }
  }

  private def startMotionWithNew(frameTimeInfo: FrameTimeInfo): Unit = {
    if (newMotionManager.iaAllFinished) {
      // Start Random Motion
    } else {
      val operations = newMotionManager.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds, 1)
      executeOperations(model, operations)
    }

  }

  override def update(frameTimeInfo: FrameTimeInfo): Unit = {
    model.restoreParameters()
    startMotionWithNew(frameTimeInfo)
    model.snapshotParameters()

    val expressionsOperations = expressionManager.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds, 1)
    val operations = effects.flatMap(_.calculateOperations(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds))

    executeOperations(model, expressionsOperations ++ operations)
    //executeOperations(model, operations)

    if (enablePhy) {
      val operations: List[UpdateOperation] = for {
        physics <- physicsHolder.toList
        operations <- physics.evaluate(model, frameTimeInfo.totalElapsedTimeInSeconds, frameTimeInfo.deltaTimeInSeconds)
      } yield {
        operations
      }
      executeOperations(model, operations)
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
   * The actually update implementation will be controlled by [[moe.brianhsu.live2d.enitiy.avatar.updater.UpdateStrategy]] inside [[updateStrategyHolder]].
   *
   * @param frameTimeInfo The FrameTimeInfo object tells us how about frame time information.
   */
  def update(frameTimeInfo: FrameTimeInfo): Unit = {
    updateStrategyHolder.foreach(_.update(frameTimeInfo))
  }
}
