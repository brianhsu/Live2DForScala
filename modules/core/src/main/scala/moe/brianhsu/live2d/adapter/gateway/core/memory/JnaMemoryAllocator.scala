package moe.brianhsu.live2d.adapter.gateway.core.memory

import com.sun.jna.Memory
import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.memory.MemoryInfo
import moe.brianhsu.live2d.enitiy.core.types.Alignment

/**
 * Default memory allocator
 *
 * This allocator will allocate extra `alignment.alignTo + 100` bytes when user
 * request the memory, and align the allocated memory to the desired aligned
 * bytes.
 *
 * TODO: There should be a better way than just allocate more memory.
 */
object JnaMemoryAllocator extends MemoryAllocator {
  override def allocate(size: Int, alignment: Alignment): MemoryInfo = {
    val memory = new Memory(size + alignment.alignTo + 100)
    val aligned = memory.align(alignment.alignTo)
    MemoryInfo(memory, aligned)
  }
}
