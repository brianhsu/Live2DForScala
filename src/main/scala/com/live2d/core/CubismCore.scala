package com.live2d.core

import com.live2d.core.exception.NativeLibraryNotFoundError
import com.live2d.core.types.CsmLogFunction
import com.live2d.core.utils.{DefaultMemoryAllocator, MemoryAllocator}
import com.sun.jna.Native

class CubismCore(val memoryAllocator: MemoryAllocator, logger: CsmLogFunction) {

  def this() = this(DefaultMemoryAllocator, line => println(line))

  def this(logger: CsmLogFunction) = this(DefaultMemoryAllocator, logger)

  lazy val cLibrary: ICubismCore = {
    try {
      val lib = Native.load("Live2DCubismCore", classOf[ICubismCore])
      lib.csmSetLogFunction(logger)
      lib
    } catch {
      case error: UnsatisfiedLinkError => throw new NativeLibraryNotFoundError(error)
    }
  }
}