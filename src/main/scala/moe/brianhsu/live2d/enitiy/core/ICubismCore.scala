package moe.brianhsu.live2d.enitiy.core

import moe.brianhsu.live2d.enitiy.core.utils.MemoryAllocator

trait ICubismCore {
  def memoryAllocator: MemoryAllocator
  def cLibrary: CubismCoreCLibrary
}
