package moe.brianhsu.live2d.core.types

import com.sun.jna.{Native, Pointer, PointerType}

class CArrayOfArrayOfShort(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): CArrayOfShort =
    new CArrayOfShort(this.getPointer.getPointer(offset * Native.POINTER_SIZE))
}
