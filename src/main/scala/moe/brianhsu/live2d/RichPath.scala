package moe.brianhsu.live2d

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path}
import scala.language.implicitConversions
import scala.util.Try

/**
 * Provide an implicit conversion for RichPath from Java NIO Path object.
 *
 * @example
 *  {{{
 *    import java.nio.file.Paths
 *    import moe.brianhsu.live2d.RichPath._
 *
 *    val path = Paths.get("/usr/share/doc")
 *    val isReadableFile = path.isReadableFile
 *  }}}
 *
 */
object RichPath {
  /**
   * Implicit conversion for RichPath.
   *
   * @param path  The original Java NIO Path object.
   * @return      A RichPath object.
   */
  implicit def convertFromPath(path: Path): RichPath = new RichPath(path)
}

/**
 * Rich Java NIO Path
 *
 * Provide convince method for working with Java NIO path.

 * @constructor Create a new RichPath object.
 * @param self  The original Java NIO object.
 */
class RichPath(self: Path) {

  /**
   * Is this path a readable file?
   *
   * @return  `ture` if it's readable file, `false` otherwise.
   */
  def isReadableFile: Boolean = {
    Files.exists(self) &&
      Files.isRegularFile(self) &&
      Files.isReadable(self)
  }

  /**
   * Read the content from the path into String.
   *
   * @param   charset   The encoding of file represent by this path.
   * @return            A [[scala.util.Success]]`[String]` contains the file content, or a [[scala.util.Failure]]`[Throwable]` if something goes wrong.
   */
  def readToString(charset: Charset = StandardCharsets.UTF_8): Try[String] = Try {
    new String(Files.readAllBytes(self), charset)
  }

}
