package moe.brianhsu.live2d.boundary.gateway.core

import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI
import moe.brianhsu.live2d.enitiy.core.types.{CsmVersion, MocVersion}

/**
 * Basic Cubism core library interface.
 */
trait CubismCore {

  /**
   * The memory allocator used for manual memory allocation.
   */
  implicit val memoryAllocator: MemoryAllocator

  /**
   * The underlying core Cubism API.
   */
  val cubismAPI: NativeCubismAPI

  /**
   * The current version of Cubism Core C Library.
   *
   * @return  The current version of underlying C Library.
   */
  lazy val libraryVersion: CsmVersion = CsmVersion(cubismAPI.csmGetVersion())

  /**
   * Get the latest supported .moc file version.
   *
   * @return  The latest supported version.
   */
  lazy val latestSupportedMocVersion: MocVersion = MocVersion(cubismAPI.csmGetLatestMocVersion())

}
