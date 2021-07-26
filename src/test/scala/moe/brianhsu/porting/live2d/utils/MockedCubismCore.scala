package moe.brianhsu.porting.live2d.utils

import moe.brianhsu.live2d.adapter.gateway.core.memory.DefaultMemoryAllocator
import moe.brianhsu.live2d.boundary.gateway.core.CubismCore
import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI

class MockedCubismCore(val cubismAPI: NativeCubismAPI) extends CubismCore {
  override val memoryAllocator: MemoryAllocator = DefaultMemoryAllocator
}