package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfFloat(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def update(offset: Int, value: Float): Unit = this.getPointer.setFloat(offset * 4, value)
  def apply(offset: Int): Float = this.getPointer.getFloat(offset * 4)
  def getPointerToFloat(offset: Int): Pointer = this.getPointer.share(offset * 4)
}
