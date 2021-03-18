package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfInt(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): Int = this.getPointer.getInt(offset * 4)
  def getPointerToInt(offset: Int): Pointer = this.getPointer.share(offset * 4)
}
