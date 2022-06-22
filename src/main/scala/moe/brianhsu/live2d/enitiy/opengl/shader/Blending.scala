package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

object Blending {

  def apply(mode: BlendMode)(implicit gl: OpenGLBinding): Blending = {
    import gl.constants._
    mode match {
      case Normal => Blending(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
      case AdditiveBlend => Blending(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)
      case MultiplicativeBlend => Blending(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)
    }
  }
}

case class Blending(srcColor: Int, dstColor: Int, srcAlpha: Int, dstAlpha: Int)

