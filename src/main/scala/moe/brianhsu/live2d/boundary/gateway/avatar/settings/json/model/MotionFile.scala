package moe.brianhsu.live2d.boundary.gateway.avatar.settings.json.model

import moe.brianhsu.live2d.RichPath.convertFromPath
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}

import java.nio.file.Paths

/**
 * Represent the MotionFile object in JSON file.

 * @param file  The file for this motion, in relative path.
 * @param fadeInTime Optional fade in time, in seconds.
 * @param fadeOutTime Optional fade out time, in seconds.
 * @param sound Sound of this motion, in relative path.
 */
private[json] case class MotionFile(file: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], sound: Option[String]) {

  private implicit val formats: Formats = DefaultFormats

  def loadMotion(directory: String): Option[Motion] = {
    for {
      path <- Option(Paths.get(s"$directory/$file")) if path.isReadableFile
      jsonFileContent <- path.readToString().toOption
    } yield {
      parse(jsonFileContent).camelizeKeys.extract[Motion]
    }
  }
}
