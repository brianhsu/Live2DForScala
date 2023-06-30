package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.enitiy.core.memory.MemoryInfo
import moe.brianhsu.live2d.enitiy.core.types.{CPointerToMoc, MocVersion}
import moe.brianhsu.live2d.exception.MocNotRevivedException

/**
 * This class represent the allocated memory for the .moc file.
 *
 * @param memory       The allocated memory information.
 * @param originalSize The size in bytes of the .moc file.
 */
case class MocInfo(memory: MemoryInfo, originalSize: Int)(implicit core: NativeCubismAPILoader) {

  /**
   * A pointer to revived moc
   */
  lazy val revivedMoc: CPointerToMoc = reviveMoc()

  /**
   * The moc version of the loaded .moc file
   */
  lazy val mocVersion: MocVersion = getMocVersion()

  private def getMocVersion(): MocVersion = {
    revivedMoc
    MocVersion(core.cubismAPI.csmGetMocVersion(memory.alignedMemory, originalSize))
  }

  private def reviveMoc(): CPointerToMoc = {
    val revivedMoc = core.cubismAPI.csmReviveMocInPlace(memory.alignedMemory, originalSize)

    if (revivedMoc == null) {
      throw new MocNotRevivedException
    }

    revivedMoc
  }

}
