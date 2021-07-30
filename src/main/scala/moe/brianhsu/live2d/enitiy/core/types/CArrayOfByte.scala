package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfByte(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): Byte = this.getPointer.getByte(offset)
  def pointerToByte(offset: Int): Pointer = this.getPointer.share(offset)
}
