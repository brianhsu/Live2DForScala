package com.live2d.core.utils

import com.live2d.core.types.Alignment
import com.sun.jna.Memory

object DefaultMemoryAllocator extends MemoryAllocator {
  override def allocate(size: Int, alignment: Alignment): MemoryInfo = {
    val memory = new Memory(size + alignment.alignTo + 100)
    val aligned = memory.align(alignment.alignTo)
    MemoryInfo(memory, aligned)
  }
}
