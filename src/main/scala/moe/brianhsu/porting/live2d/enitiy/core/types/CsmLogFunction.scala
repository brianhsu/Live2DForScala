package moe.brianhsu.porting.live2d.enitiy.core.types

import com.sun.jna.Callback

trait CsmLogFunction extends Callback {
  def invoke(message: String): Unit
}
