package moe.brianhsu.live2d.boundary.gateway.core

import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI

/**
 * Basic Cubism core library interface.
 */
trait CubismCore {

  /**
   * The memory allocator used for manual memory allocation.
   */
  val memoryAllocator: MemoryAllocator

  /**
   * The underlying core Cubism API.
   */
  val cubismAPI: NativeCubismAPI
}
