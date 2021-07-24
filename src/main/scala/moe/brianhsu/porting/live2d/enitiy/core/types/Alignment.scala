package moe.brianhsu.porting.live2d.enitiy.core.types

sealed class Alignment(val alignTo: Int)

object MocAlignment extends Alignment(64)
object ModelAlignment extends Alignment(16)