package moe.brianhsu.live2d.enitiy.core

import moe.brianhsu.live2d.enitiy.core.memory.MemoryAllocator

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
