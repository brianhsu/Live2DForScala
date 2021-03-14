package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CPointerToMoc(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
}





