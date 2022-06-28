package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfInt(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): Int = this.getPointer.getInt(offset * 4)
  def pointerToInt(offset: Int): Pointer = this.getPointer.share(offset * 4)
}
