package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}
import moe.brianhsu.live2d.enitiy.core.CsmColor

class CArrayOfCsmColor(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def apply(offset: Int): CsmColor = new CsmColor(pointerToCsmColor(offset))
  def pointerToCsmColor(offset: Int): Pointer = this.getPointer.share(offset * CsmColor.SIZE)
}
