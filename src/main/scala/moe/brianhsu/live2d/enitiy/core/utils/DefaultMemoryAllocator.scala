package moe.brianhsu.live2d.enitiy.core.utils

import com.sun.jna.Memory
import moe.brianhsu.live2d.enitiy.core.types.Alignment

object DefaultMemoryAllocator extends MemoryAllocator {
  override def allocate(size: Int, alignment: Alignment): MemoryInfo = {
    val memory = new Memory(size + alignment.alignTo + 100)
    val aligned = memory.align(alignment.alignTo)
    MemoryInfo(memory, aligned)
  }
}
