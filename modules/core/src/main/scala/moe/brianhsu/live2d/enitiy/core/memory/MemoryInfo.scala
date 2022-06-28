package moe.brianhsu.live2d.enitiy.core.memory

import com.sun.jna.Memory

/**
 * The class denote the allocated aligned memory.
 *
 * @param originalMemory    The original memory window.
 * @param alignedMemory     The aligned memory window.
 */
case class MemoryInfo(originalMemory: Memory, alignedMemory: Memory)