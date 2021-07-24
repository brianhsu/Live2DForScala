package moe.brianhsu.porting.live2d.enitiy.core.types

import com.sun.jna.{Pointer, PointerType}

class CStringArray(pointer: Pointer) extends PointerType(pointer) {
  private lazy val values = this.getPointer.getPointerArray(0)

  def this() = this(null)
  def apply(offset: Int): String = values(offset).getString(0)
}
