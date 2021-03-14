package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfByte(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def update(offset: Int, value: Byte): Unit = this.getPointer.setByte(offset, value)
  def apply(offset: Int): Byte = this.getPointer.getByte(offset)
}
