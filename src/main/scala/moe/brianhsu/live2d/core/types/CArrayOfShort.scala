package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

import java.nio.{Buffer, ByteBuffer, ByteOrder}

class CArrayOfShort(pointer: Pointer) extends PointerType(pointer) {
  def apply(offset: Int): Short = this.getPointer.getShort(offset * 2)

  def getDirectBuffer(size: Int): Buffer = this.pointer.getByteBuffer(0, size * 2)
}
