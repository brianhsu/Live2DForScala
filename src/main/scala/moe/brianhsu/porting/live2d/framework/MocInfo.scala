package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.live2d.enitiy.core.memory.MemoryInfo

/**
 * This class represent the allocated memory for the .moc file.
 *
 * @param memory        The allocated memory information.
 * @param originalSize  The size in bytes of the .moc file.
 */
case class MocInfo(memory: MemoryInfo, originalSize: Int)
