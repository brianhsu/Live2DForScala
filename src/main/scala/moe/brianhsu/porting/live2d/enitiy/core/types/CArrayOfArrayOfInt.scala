package moe.brianhsu.porting.live2d.enitiy.core.types

import com.sun.jna.{Native, Pointer, PointerType}

class CArrayOfArrayOfInt(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): CArrayOfInt =
    new CArrayOfInt(this.getPointer.getPointer(offset * Native.POINTER_SIZE))
}
