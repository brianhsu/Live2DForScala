package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfInt(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def update(offset: Int, value: Int): Unit = this.getPointer.setInt(offset * 4, value)
  def apply(offset: Int): Int = this.getPointer.getInt(offset * 4)
}
