package moe.brianhsu.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfArrayOfCsmVector(pointer: Pointer) extends PointerType(pointer) {
  private lazy val arrays = this.getPointer.getPointerArray(0)

  def this() = this(null)

  def apply(i: Int): CArrayOfCsmCoordinate = new CArrayOfCsmCoordinate(arrays(i))
}
