package moe.brianhsu.live2d.gateway.avatar.settings.impl.json.model

import moe.brianhsu.live2d.RichPath.convertFromPath
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}

import java.nio.file.Paths

case class MotionFile(file: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], sound: Option[String]) {

  private implicit val formats: Formats = DefaultFormats

  def loadMotion(directory: String): Option[Motion] = {
    for {
      path <- Option(Paths.get(s"$directory/$file")) if path.isLoadableFile
      jsonFileContent <- path.readToString().toOption
    } yield {
      parse(jsonFileContent).camelizeKeys.extract[Motion]
    }
  }
}
