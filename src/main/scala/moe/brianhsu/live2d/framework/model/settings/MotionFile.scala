package moe.brianhsu.live2d.framework.model.settings

import moe.brianhsu.live2d.framework.model.AvatarSettings
import org.json4s.{DefaultFormats, Formats}

import java.io.File

case class MotionFile(file: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], sound: Option[String]) {

  private implicit val formats: Formats = DefaultFormats

  def loadMotion(directory: String): Option[Motion] = {
    AvatarSettings
      .parseJson(new File(s"$directory/$file"))
      .map(_.camelizeKeys.extract[Motion])
  }
}
