package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Native, Pointer, PointerType}

class CArrayOfArrayOfFloat(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): CArrayOfFloat =
    new CArrayOfFloat(this.getPointer.getPointer(offset * Native.POINTER_SIZE))
}
