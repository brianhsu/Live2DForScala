package moe.brianhsu.live2d.framework.model

import moe.brianhsu.live2d.demo.{FaceDirection, FrameTime}
import moe.brianhsu.live2d.framework.Cubism
import moe.brianhsu.live2d.framework.effect.{Breath, EyeBlink}

import java.io.File
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.io.Source
import scala.util.{Try, Using}

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

  assert(mocFile.isDefined, s"Cannot find moc file inside the $directory/")

  val modelHolder: Try[Live2DModel] = {
    cubism
      .loadModel(mocFile.get, avatarSettings.textureFiles)
      .map(_.validAllDataFromNativeLibrary)
  }


  private lazy val eyeBlinkHolder = getEyeBlinkEffect()
  private lazy val breath = new Breath()
  def update(): Unit = {
    modelHolder.foreach { model =>
      val deltaTime = FrameTime.getDeltaTime

      model.loadParameters()
      model.saveParameters()
      eyeBlinkHolder.foreach(_.updateParameters(this.modelHolder.get, deltaTime))
      breath.updateParameters(this, deltaTime)

      model.update()
    }
  }

  def getEyeBlinkEffect(blinkingIntervalSeconds: Float = 4.0f,
                        closingSeconds: Float = 0.1f,
                        closedSeconds: Float = 0.05f,
                        openingSeconds: Float = 0.15f): Option[EyeBlink] = {
    avatarSettings.eyeBlinkParameterIds match {
      case Nil => None
      case parameterIds => Some(
        new EyeBlink(parameterIds, blinkingIntervalSeconds,
                     closingSeconds, closedSeconds, openingSeconds)
      )
    }
  }

}
