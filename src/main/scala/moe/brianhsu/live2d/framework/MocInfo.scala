package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.enitiy.core.utils.MemoryInfo

/**
 * This class represent the allocated memory for the .moc file.
 *
 * @param memory        The allocated memory information.
 * @param originalSize  The size in bytes of the .moc file.
 */
case class MocInfo(memory: MemoryInfo, originalSize: Int)
