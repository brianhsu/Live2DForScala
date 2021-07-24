package moe.brianhsu.live2d.enitiy.core.utils

import moe.brianhsu.live2d.enitiy.core.types.Alignment

trait MemoryAllocator {
  def allocate(size: Int, alignment: Alignment): MemoryInfo
}
