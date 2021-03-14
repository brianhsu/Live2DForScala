package moe.brianhsu.live2d.core

import moe.brianhsu.live2d.core.utils.MemoryAllocator

trait ICubismCore {
  def memoryAllocator: MemoryAllocator
  def cLibrary: CubismCoreCLibrary
}
