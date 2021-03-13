package com.live2d.core.utils

import com.live2d.core.types.Alignment2
import com.sun.jna.Memory

trait MemoryAllocator {
  def allocate(size: Int, alignment: Alignment2): Memory
}
