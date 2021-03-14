package moe.brianhsu.live2d.core.utils

import moe.brianhsu.live2d.core.types.Alignment

trait MemoryAllocator {
  def allocate(size: Int, alignment: Alignment): MemoryInfo
}
