package moe.brianhsu.live2d.enitiy.core

import com.sun.jna.Native
import moe.brianhsu.live2d.enitiy.core.exception.NativeLibraryNotFoundError
import moe.brianhsu.live2d.enitiy.core.types.CsmLogFunction
import moe.brianhsu.live2d.enitiy.core.utils.{DefaultMemoryAllocator, MemoryAllocator}

class CubismCore(val memoryAllocator: MemoryAllocator, logger: CsmLogFunction) extends ICubismCore {

  def this() = this(DefaultMemoryAllocator, line => println(line))

  def this(logger: CsmLogFunction) = this(DefaultMemoryAllocator, logger)

  lazy val cLibrary: CubismCoreCLibrary = {
    try {
      val lib = Native.load("Live2DCubismCore", classOf[CubismCoreCLibrary])
      lib.csmSetLogFunction(logger)
      lib
    } catch {
      case error: UnsatisfiedLinkError => throw new NativeLibraryNotFoundError(error)
    }
  }
}