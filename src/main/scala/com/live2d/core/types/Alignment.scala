package com.live2d.core.types

sealed class Alignment2(val alignTo: Int)

object MocAlignment extends Alignment2(64)
object ModelAlignment extends Alignment2(16)

object Alignment {
  val moc: Int = 64
  val model: Int = 16
}
