package moe.brianhsu.porting.live2d.framework.util

import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.types.MocAlignment
import moe.brianhsu.porting.live2d.framework.MocInfo

import java.nio.file.{Files, Paths}

class MocFileReader(allocator: MemoryAllocator) {

  def readFile(filename: String): MocInfo = {
    val fileContent = Files.readAllBytes(Paths.get(filename))
    val memoryInfo = allocator.allocate(fileContent.size, MocAlignment)
    memoryInfo.alignedMemory.write(0, fileContent, 0, fileContent.size)
    MocInfo(memoryInfo, fileContent.size)
  }
}
