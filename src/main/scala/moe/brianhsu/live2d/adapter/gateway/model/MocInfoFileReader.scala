package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.model.MocInfoReader
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import moe.brianhsu.live2d.enitiy.model
import moe.brianhsu.live2d.enitiy.model.MocInfo

import java.nio.file.{Files, Paths}
import scala.util.Try

/**
 * MocInfo loader for .moc3 file
 *
 * @param filePath  The file path of .moc3 file
 * @param core      The CubismCore object responsible for allocate memory.
 */
class MocInfoFileReader(filePath: String)(implicit core: NativeCubismAPILoader) extends MocInfoReader {

  override def loadMocInfo(): Try[MocInfo] = Try {
    val fileContent = Files.readAllBytes(Paths.get(filePath))
    val memoryInfo = core.memoryAllocator.allocate(fileContent.size, MocAlignment)

    memoryInfo.alignedMemory.write(0, fileContent, 0, fileContent.size)

    model.MocInfo(memoryInfo, fileContent.size)
  }
}
