package moe.brianhsu.live2d.core.utils

import com.sun.jna.{Memory, Native, Pointer}

/**
 * The class denote the allocated aligned memory.
 *
 * @param originalMemory    The original memory window.
 * @param alignedMemory     The aligned memory window.
 */
case class MemoryInfo(originalMemory: Memory, alignedMemory: Memory) {
  /**
   * Free the allocated C memory
   */
  def dispose(): Unit = {
    if (originalMemory.valid) {
      Native.free(Pointer.nativeValue(originalMemory))
    }
  }
}