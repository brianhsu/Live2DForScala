package moe.brianhsu.porting.live2d.enitiy.core.utils

import moe.brianhsu.porting.live2d.enitiy.core.types.Alignment

trait MemoryAllocator {
  def allocate(size: Int, alignment: Alignment): MemoryInfo
}
