package com.live2d.core.utils

import com.live2d.core.types.Alignment2
import com.sun.jna.Memory

class DefaultMemoryAllocator extends MemoryAllocator {
  override def allocate(size: Int, alignment: Alignment2): Memory = {
    val memory = new Memory(size + alignment.alignTo + 100)
    memory.align(alignment.alignTo)
  }
}
