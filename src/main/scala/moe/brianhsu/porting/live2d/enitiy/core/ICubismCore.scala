package moe.brianhsu.porting.live2d.enitiy.core

import moe.brianhsu.porting.live2d.enitiy.core.utils.MemoryAllocator

trait ICubismCore {
  def memoryAllocator: MemoryAllocator
  def cLibrary: CubismCoreCLibrary
}
