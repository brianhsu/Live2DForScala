package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfShort(pointer: Pointer) extends PointerType(pointer) {
  def apply(offset: Int): Short = this.getPointer.getShort(offset * 2)
}
