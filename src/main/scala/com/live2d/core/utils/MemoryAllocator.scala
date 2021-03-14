package com.live2d.core.utils

import com.live2d.core.types.Alignment

trait MemoryAllocator {
  def allocate(size: Int, alignment: Alignment): MemoryInfo
}
