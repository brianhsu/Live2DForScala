package com.live2d.core.types

sealed class Alignment(val alignTo: Int)

object MocAlignment extends Alignment(64)
object ModelAlignment extends Alignment(16)