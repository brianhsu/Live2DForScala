package moe.brianhsu.live2d.adapter.gateway.core

import com.sun.jna.Native
import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader.DefaultLogger
import moe.brianhsu.live2d.adapter.gateway.core.memory.JnaMemoryAllocator
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.core.memory.MemoryAllocator
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI
import moe.brianhsu.live2d.enitiy.core.types.CsmLogFunction
import moe.brianhsu.live2d.exception.NativeLibraryNotFoundError
import org.slf4j.LoggerFactory

object JnaNativeCubismAPILoader {
  object DefaultLogger extends CsmLogFunction {
    private val logger = LoggerFactory.getLogger(this.getClass)
    override def invoke(message: String): Unit = {
      logger.info(message)
    }
  }
}

/**
 * CubismCore implemented with JNA
 *
 * @constructor Create new CubismCore instance, backed by JNA implementation.
 *
 * @param memoryAllocator   The memory allocator used during manually allocating memory.
 * @param logger            The logger that will log message from underlying C Cubism Library.
 */
class JnaNativeCubismAPILoader(override val memoryAllocator: MemoryAllocator, logger: CsmLogFunction) extends NativeCubismAPILoader {


  /**
   * Create an instance with [[moe.brianhsu.live2d.adapter.gateway.core.memory.JnaMemoryAllocator]], and log message into
   * the standard output.
   */
  def this() = this(JnaMemoryAllocator, DefaultLogger)

  /**
   * Create an instance with [[moe.brianhsu.live2d.adapter.gateway.core.memory.JnaMemoryAllocator]], and customize how to
   * log message.
   *
   * @param logger  The logger that will log message from underlying C Cubism Library.
   */
  def this(logger: CsmLogFunction) = this(JnaMemoryAllocator, logger)

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