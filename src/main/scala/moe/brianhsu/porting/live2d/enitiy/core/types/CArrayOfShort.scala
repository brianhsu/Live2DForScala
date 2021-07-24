package moe.brianhsu.porting.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

import java.nio.ByteBuffer

class CArrayOfShort(pointer: Pointer) extends PointerType(pointer) {
  def apply(offset: Int): Short = this.getPointer.getShort(offset * 2)

  def getDirectBuffer(size: Int): ByteBuffer = this.pointer.getByteBuffer(0, size * 2)
}
