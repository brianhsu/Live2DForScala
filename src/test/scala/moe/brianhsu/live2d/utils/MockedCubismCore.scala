package moe.brianhsu.live2d.utils

import moe.brianhsu.live2d.enitiy.core.utils.{DefaultMemoryAllocator, MemoryAllocator}
import moe.brianhsu.live2d.enitiy.core.{CubismCoreCLibrary, ICubismCore}

class MockedCubismCore(val cLibrary: CubismCoreCLibrary) extends ICubismCore {
  override def memoryAllocator: MemoryAllocator = DefaultMemoryAllocator
}