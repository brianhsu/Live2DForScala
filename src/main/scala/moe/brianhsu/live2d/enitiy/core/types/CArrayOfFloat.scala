package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfFloat(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): Float = this.getPointer.getFloat(offset * 4)
  def pointerToFloat(offset: Int): Pointer = this.getPointer.share(offset * 4)
}
