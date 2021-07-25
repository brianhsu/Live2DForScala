package moe.brianhsu.porting.live2d.utils

import moe.brianhsu.live2d.boundary.gateway.core.memory.DefaultMemoryAllocator
import moe.brianhsu.live2d.enitiy.core.{NativeCubismAPI, CubismCore}
import moe.brianhsu.live2d.enitiy.core.memory.MemoryAllocator

class MockedCubismCore(val cubismAPI: NativeCubismAPI) extends CubismCore {
  override val memoryAllocator: MemoryAllocator = DefaultMemoryAllocator
}