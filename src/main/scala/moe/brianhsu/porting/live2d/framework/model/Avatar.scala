package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.avatar.updater.UpdateStrategy
import moe.brianhsu.porting.live2d.framework.{CubismExpressionMotion, CubismMotion, CubismMotionManager, Pose}
import moe.brianhsu.porting.live2d.framework.effect.Effect
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class DefaultStrategy(avatarSettings: Settings, protected val model: Live2DModel) extends UpdateStrategy {
  private var effects: List[Effect] = Nil
  private val expressionManager = new CubismMotionManager
  private val motionManager = new CubismMotionManager
  private val expressions = CubismExpressionMotion.createExpressions(avatarSettings)

  def setEffects(effects: List[Effect]): Unit = {
    this.effects = effects
  }

  def appendEffect(effect: Effect): Unit = {
    this.effects = effects.appended(effect)
  }

  def removeEffect(effect: Effect): Unit = {
    this.effects = effects.filterNot(_ == effect)
  }

  lazy val motions: Seq[MotionSetting] = avatarSettings.motionGroups.values.toList.flatten

  def startMotion(group: String, i: Int): Unit = {
    val name = s"Motion(${group}_$i)"
    val motionSettings = avatarSettings.motionGroups(group)(i)
    val m = CubismMotion(motionSettings, _ => println(s"$name has finished"), avatarSettings.eyeBlinkParameterIds, Nil)
    println(s"Start motionmotion  $name")
    motionManager.StartMotionPriority(m, autoDelete = false, 2)
  }
  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expression =>
      println(s"Start $name expression")
      expressionManager.StartMotionPriority(expression, autoDelete = false, 3)
    }
  }

  override def update(deltaTimeInSeconds: Float): Unit = {
    model.restoreParameters()
    if (motionManager.IsFinished()) {

    } else {
      motionManager.UpdateMotion(model, deltaTimeInSeconds)
    }
    model.snapshotParameters()
    expressionManager.UpdateMotion(model, deltaTimeInSeconds)
    effects.foreach {
      _.updateParameters(model, deltaTimeInSeconds)
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
   * @param deltaTimeInSeconds How long has elapsed since last update, in seconds.
   */
  def update(deltaTimeInSeconds: Float): Unit = {
    updateStrategyHolder.foreach(_.update(deltaTimeInSeconds))
  }
}
