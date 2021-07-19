package moe.brianhsu.live2d.framework.model

import moe.brianhsu.live2d.framework.{Cubism, CubismExpressionMotion, CubismMotionManager, CubismMotionQueueManager}
import moe.brianhsu.live2d.framework.effect.Effect

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

  private val avatarSettings = new AvatarSettings(directory)
  private val mocFile: Option[String] = avatarSettings.mocFile
  private var effects: List[Effect] = Nil
  private val expressionManager = new CubismMotionManager
  private val expressions = CubismExpressionMotion.createExpressions(avatarSettings)

  assert(mocFile.isDefined, s"Cannot find moc file inside the $directory/")

  val modelHolder: Try[Live2DModel] = {
    cubism
      .loadModel(mocFile.get, avatarSettings.textureFiles)
      .map(_.validAllDataFromNativeLibrary)
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

  def getAvatarSettings: AvatarSettings = avatarSettings

  def getEffects: List[Effect] = effects

  def setExpression(name: String): Unit = {
    expressions.get(name).foreach { expression =>
      println(s"Start $name expression")
      expressionManager.StartMotionPriority(expression, false, 3)
    }
  }
  /**
   * Update Live2D model parameters of this avatar according to time in seconds elapsed
   * from last update.
   *
   * @param deltaTimeInSeconds How long has elapsed since last update, in seconds.
   */
  def update(deltaTimeInSeconds: Float): Unit = {
    modelHolder.foreach { model =>
      model.loadParameters()
      model.saveParameters()
      expressionManager.UpdateMotion(model, deltaTimeInSeconds)
      effects.foreach { _.updateParameters(model, deltaTimeInSeconds) }
      model.update()
    }
  }
}
