package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.boundary.gateway.model.MocInfoReader
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import moe.brianhsu.live2d.enitiy.model
import moe.brianhsu.live2d.enitiy.model.MocInfo

import java.nio.file.{Files, Paths}
import scala.util.Try

class MocInfoFileReader(filename: String)(implicit allocator: MemoryAllocator) extends MocInfoReader {

  override def loadMocInfo(): Try[MocInfo] = Try {
    val fileContent = Files.readAllBytes(Paths.get(filename))
    val memoryInfo = allocator.allocate(fileContent.size, MocAlignment)

    memoryInfo.alignedMemory.write(0, fileContent, 0, fileContent.size)

    model.MocInfo(memoryInfo, fileContent.size)
  }
}
