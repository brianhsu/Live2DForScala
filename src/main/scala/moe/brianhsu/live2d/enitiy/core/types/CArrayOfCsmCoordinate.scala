package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}
import moe.brianhsu.live2d.enitiy.core.CsmCoordinate

import java.nio.ByteBuffer

class CArrayOfCsmCoordinate(pointer: Pointer) extends PointerType(pointer) {
  def apply(offset: Int): CsmCoordinate = new CsmCoordinate(getPointerCsmVector(offset))
  def getPointerCsmVector(offset: Int): Pointer = this.getPointer.share(offset * CsmCoordinate.SIZE)

  def getDirectBuffer(size: Int): ByteBuffer = this.pointer.getByteBuffer(0, size * 4 * 2)
}
