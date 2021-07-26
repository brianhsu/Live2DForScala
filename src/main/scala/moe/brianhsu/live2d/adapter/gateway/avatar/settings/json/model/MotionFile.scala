package moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.model

import moe.brianhsu.live2d.RichPath.convertFromPath
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}

import java.nio.file.Paths
import scala.util.Try

/**
 * Represent the MotionFile object in JSON file.

 * @param file        The file for this motion, in relative path.
 * @param fadeInTime  Optional fade in time, in seconds.
 * @param fadeOutTime Optional fade out time, in seconds.
 * @param sound       Sound of this motion, in relative path.
 */
private[json] case class MotionFile(file: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], sound: Option[String]) {

  private implicit val formats: Formats = DefaultFormats

  /**
   * Load this motion from specified directory.
   *
   * @param directory The directory contain the motion file.
   * @return [[scala.util.Success]] if loaded successfully, [[scala.util.Failure]] otherwise.
   */
  def loadMotion(directory: String): Try[Motion] = {
    for {
      path <- Try(Paths.get(s"$directory/$file")) if path.isReadableFile
      jsonFileContent <- path.readToString()
      parsedJson <- Try(parse(jsonFileContent).camelizeKeys.extract[Motion])
    } yield {
      parsedJson
    }
  }
}
