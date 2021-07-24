package moe.brianhsu.porting.live2d.utils

import moe.brianhsu.live2d.enitiy.core.{CubismCoreCLibrary, ICubismCore}
import moe.brianhsu.live2d.enitiy.core.utils.{DefaultMemoryAllocator, MemoryAllocator}

class MockedCubismCore(val cLibrary: CubismCoreCLibrary) extends ICubismCore {
  override def memoryAllocator: MemoryAllocator = DefaultMemoryAllocator
}