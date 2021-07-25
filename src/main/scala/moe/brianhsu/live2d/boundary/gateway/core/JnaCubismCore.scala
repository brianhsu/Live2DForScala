package moe.brianhsu.live2d.boundary.gateway.core

import com.sun.jna.Native
import moe.brianhsu.live2d.boundary.gateway.core.memory.DefaultMemoryAllocator
import moe.brianhsu.live2d.exception.NativeLibraryNotFoundError
import moe.brianhsu.live2d.enitiy.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.types.CsmLogFunction
import moe.brianhsu.live2d.enitiy.core.{CubismCore, NativeCubismAPI}

/**
 * CubismCore implemented with JNA
 *
 * @constructor Create new CubismCore instance, backed by JNA implementation.
 *
 * @param memoryAllocator   The memory allocator used during manually allocating memory.
 * @param logger            The logger that will log message from underlying C Cubism Library.
 */
class JnaCubismCore(override val memoryAllocator: MemoryAllocator, logger: CsmLogFunction) extends CubismCore {

  /**
   * Create an instance with [[memory.DefaultMemoryAllocator]], and log message into
   * the standard output.
   */
  def this() = this(DefaultMemoryAllocator, line => println(line))

  /**
   * Create an instance with [[memory.DefaultMemoryAllocator]], and customize how to
   * log message.
   *
   * @param logger  The logger that will log message from underlying C Cubism Library.
   */
  def this(logger: CsmLogFunction) = this(DefaultMemoryAllocator, logger)

  override lazy val cubismAPI: NativeCubismAPI = {
    try {
      val lib = Native.load("Live2DCubismCore", classOf[NativeCubismAPI])
      lib.csmSetLogFunction(logger)
      lib
    } catch {
      case error: UnsatisfiedLinkError => throw new NativeLibraryNotFoundError(error)
    }
  }
}