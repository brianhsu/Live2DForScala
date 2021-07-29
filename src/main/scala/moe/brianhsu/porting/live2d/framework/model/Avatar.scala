package moe.brianhsu.porting.live2d.framework.model

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.porting.live2d.framework.{Cubism, CubismExpressionMotion, CubismMotion, CubismMotionManager, Pose}
import moe.brianhsu.porting.live2d.framework.effect.Effect
import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.util.Try

/**
 * This class represent a complete Live 2D Cubism Avatar runtime model.
 *
 * The runtime model should be a directory that contains a bunch of json and other files
 * that describe the behavior of the avatar.
 *
 * It should also includes a .moc3 file and texture files.
 *
 * You might obtain sample avatar from https://www.live2d.com/en/download/sample-data/
 *
 * @param   directory   The directory in the filesystem that contains the settings for the avatar
 */
class Avatar(directory: String)(cubism: Cubism) {

  private val avatarSettings: Settings = new JsonSettingsReader(directory).loadSettings().get
  private val mocFile: String = avatarSettings.mocFile
  private var effects: List[Effect] = Nil
  private val expressionManager = new CubismMotionManager
  private val motionManager = new CubismMotionManager
  private val expressions = CubismExpressionMotion.createExpressions(avatarSettings)
  private val pose = Pose(avatarSettings)

  val modelHolder: Try[Live2DModel] = {
    val model = cubism
      .loadModel(mocFile, avatarSettings.textureFiles)

      model.foreach(_.validateAllData())

    model
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

  def getAvatarSettings: Settings = avatarSettings

  def getEffects: List[Effect] = effects

  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expression =>
      println(s"Start $name expression")
      expressionManager.StartMotionPriority(expression, autoDelete = false, 3)
    }
  }

  lazy val motions: Seq[MotionSetting] = avatarSettings.motionGroups.values.toList.flatten

  def startMotion(group: String, i: Int): Unit = {
    val name = s"Motion(${group}_$i)"
    val motionSettings = avatarSettings.motionGroups(group)(i)
    val m = CubismMotion(motionSettings, _ => println(s"$name has finished"), avatarSettings.eyeBlinkParameterIds, Nil)
    println(s"Start motionmotion  $name")
    motionManager.StartMotionPriority(m, autoDelete = false, 2)
  }

  /**
   * Update Live2D model parameters of this avatar according to time in seconds elapsed
   * from last update.
   *
   * @param deltaTimeInSeconds How long has elapsed since last update, in seconds.
   */
  def update(deltaTimeInSeconds: Float): Unit = {

    modelHolder.foreach { model =>
      model.restoreParameters()
      if (motionManager.IsFinished()) {

      } else {
        motionManager.UpdateMotion(model, deltaTimeInSeconds)
      }
      model.snapshotParameters()
      expressionManager.UpdateMotion(model, deltaTimeInSeconds)
      effects.foreach { _.updateParameters(model, deltaTimeInSeconds) }
      pose.UpdateParameters(model, deltaTimeInSeconds)
      model.update()
    }
  }
}
