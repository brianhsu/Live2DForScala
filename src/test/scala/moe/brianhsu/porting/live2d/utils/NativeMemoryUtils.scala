package moe.brianhsu.porting.live2d.utils

import com.sun.jna.{Native, Pointer}

object NativeMemoryUtils {
  def createPointerToFloat(value: Float): Pointer = {
    val pointer = new Pointer(Native.malloc(1 * Native.getNativeSize(classOf[Float])))
    pointer.setFloat(0, value)
    pointer
  }
}
