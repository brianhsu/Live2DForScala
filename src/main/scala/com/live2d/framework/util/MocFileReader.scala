package com.live2d.framework.util

import com.live2d.core.types.MocAlignment
import com.live2d.core.utils.MemoryAllocator
import com.sun.jna.Memory

import java.nio.file.{Files, Paths}

class MocFileReader(allocator: MemoryAllocator) {
  case class MocInfo(memory: Memory, originalSize: Int)

  def readFile(filename: String): MocInfo = {
    val bytes = Files.readAllBytes(Paths.get(filename))
    val memory = allocator.allocate(bytes.size, MocAlignment)
    memory.write(0, bytes, 0, bytes.size)
    MocInfo(memory, bytes.size)
  }
}
