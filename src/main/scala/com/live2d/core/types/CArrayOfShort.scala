package com.live2d.core.types

import com.sun.jna.{Pointer, PointerType}

class CArrayOfShort(pointer: Pointer) extends PointerType(pointer) {
  def this() = this(null)
  def update(offset: Int, value: Short): Unit = this.getPointer.setShort(offset * 2, value)
  def apply(offset: Int): Short = this.getPointer.getShort(offset * 2)
}
