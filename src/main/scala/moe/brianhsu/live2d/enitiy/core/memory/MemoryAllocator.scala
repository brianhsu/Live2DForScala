package moe.brianhsu.live2d.enitiy.core.memory

import moe.brianhsu.live2d.enitiy.core.types.Alignment

/**
 * Manual Memory Allocator for native Cubism C library.
 *
 * A manual memory allocator used when interact with Live2D Cubism C library,
 * where in some case we need pass an allocated and aligned memory.
 */
trait MemoryAllocator {

  /**
   * Allocate aligned memroy
   *
   * @param size      Desired size, in bytes.
   * @param alignment Desired alignment in bytes, should be a power of two.
   * @return          The allocated memory information.
   */
  def allocate(size: Int, alignment: Alignment): MemoryInfo
}
