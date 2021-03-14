package com.live2d.framework.util

import com.live2d.core.types.MocAlignment
import com.live2d.core.utils.MemoryAllocator
import com.live2d.framework.MocInfo

import java.nio.file.{Files, Paths}

class MocFileReader(allocator: MemoryAllocator) {

  def readFile(filename: String): MocInfo = {
    val bytes = Files.readAllBytes(Paths.get(filename))
    val memoryInfo = allocator.allocate(bytes.size, MocAlignment)
    memoryInfo.alignedMemory.write(0, bytes, 0, bytes.size)
    MocInfo(memoryInfo, bytes.size)
  }
}
