package moe.brianhsu.porting.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}
import moe.brianhsu.porting.live2d.enitiy.core.CsmVector

class CArrayOfCsmVector(pointer: Pointer) extends PointerType(pointer) {
  def apply(offset: Int): CsmVector = new CsmVector(getPointerCsmVector(offset))
  def getPointerCsmVector(offset: Int): Pointer = this.getPointer.share(offset * CsmVector.SIZE)

  def getDirectBuffer(size: Int) = this.pointer.getByteBuffer(0, size * 4 * 2)
}
