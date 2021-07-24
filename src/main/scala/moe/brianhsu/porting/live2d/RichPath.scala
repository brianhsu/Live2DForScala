package moe.brianhsu.porting.live2d

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.language.implicitConversions
import scala.util.Try

object RichPath {
  implicit def convertFromPath(path: Path): RichPath = new RichPath(path)
}

class RichPath(path: Path) {

  def isLoadableFile: Boolean = {
    Files.exists(path) &&
      Files.isRegularFile(path) &&
      Files.isReadable(path)
  }

  def readToString(): Try[String] = Try {
    new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
  }

}
