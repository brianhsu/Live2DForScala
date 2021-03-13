package com.live2d

import com.sun.jna.Native

package object core {
  val coreLib = Native.load("Live2DCubismCore", classOf[Live2DCubismCore]).asInstanceOf[Live2DCubismCore]
}
