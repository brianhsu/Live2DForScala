package moe.brianhsu.porting.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

class CPointerToModel(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
}
