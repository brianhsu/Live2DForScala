package moe.brianhsu.utils

import moe.brianhsu.live2d.adapter.gateway.core.memory.JnaMemoryAllocator
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI

class MockedNativeCubismAPILoader(val cubismAPI: NativeCubismAPI) extends NativeCubismAPILoader {
  override val memoryAllocator: MemoryAllocator = JnaMemoryAllocator
}