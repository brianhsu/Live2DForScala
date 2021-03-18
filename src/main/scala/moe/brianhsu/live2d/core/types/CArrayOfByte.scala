package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfByte(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): Byte = this.getPointer.getByte(offset)
  def getPointerToByte(offset: Int): Pointer = this.getPointer.share(offset)
}
