package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}
import moe.brianhsu.live2d.enitiy.core.CsmVector

import java.nio.ByteBuffer

class CArrayOfCsmVector(pointer: Pointer) extends PointerType(pointer) {
  def apply(offset: Int): CsmVector = new CsmVector(getPointerCsmVector(offset))
  def getPointerCsmVector(offset: Int): Pointer = this.getPointer.share(offset * CsmVector.SIZE)

  def getDirectBuffer(size: Int): ByteBuffer = this.pointer.getByteBuffer(0, size * 4 * 2)
}
