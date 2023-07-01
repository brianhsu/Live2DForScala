package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.model.MocInfoReader
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import moe.brianhsu.live2d.enitiy.model.MocInfo
import moe.brianhsu.live2d.exception.MocInconsistentException

import java.nio.file.{Files, Paths}
import scala.util.Try

/**
 * MocInfo loader for .moc3 file
 *
 * @param filePath                The file path of .moc3 file.
 * @param shouldCheckConsistent   Should we check consistent of the .moc3 file after reading it or not.
 * @param core                    The CubismCore object responsible for allocate memory.
 */
class MocInfoFileReader(filePath: String, shouldCheckConsistent: Boolean = true)(implicit core: NativeCubismAPILoader) extends MocInfoReader {

  override def loadMocInfo(): Try[MocInfo] = Try {
    val fileContent = Files.readAllBytes(Paths.get(filePath))
    val memoryInfo = core.memoryAllocator.allocate(fileContent.size, MocAlignment)

    memoryInfo.alignedMemory.write(0, fileContent, 0, fileContent.size)
    val mocInfo = MocInfo(memoryInfo, fileContent.size)(core)

    if (shouldCheckConsistent && !mocInfo.isConsistent) {
      throw new MocInconsistentException(filePath)
    }

    mocInfo
  }
}
