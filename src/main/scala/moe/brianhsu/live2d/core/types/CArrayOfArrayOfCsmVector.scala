package moe.brianhsu.live2d.core.types

import com.sun.jna.{Pointer, PointerType}
import moe.brianhsu.live2d.core.CsmVector

class CArrayOfArrayOfCsmVector(pointer: Pointer) extends PointerType(pointer) {
  private lazy val arrays = this.getPointer.getPointerArray(0)
  private var cachedData: Map[(Int, Int), CsmVector] = Map.empty
  def this() = this(null)
  def apply(row: Int)(column: Int): CsmVector = {
    val key = (row, column)
    val pointer = arrays(row).share(column * CsmVector.SIZE)
    val data = cachedData.getOrElse(key, new CsmVector(pointer))
    cachedData = cachedData.updated(key, data)
    data
  }
}
