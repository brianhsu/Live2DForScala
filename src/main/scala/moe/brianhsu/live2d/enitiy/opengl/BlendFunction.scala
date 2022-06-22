package moe.brianhsu.live2d.enitiy.opengl

import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}

object BlendFunction {

  def apply(mode: BlendMode)(implicit gl: OpenGLBinding): BlendFunction = {
    import gl.constants._
    mode match {
      case Normal => BlendFunction(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
      case AdditiveBlend => BlendFunction(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)
      case MultiplicativeBlend => BlendFunction(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)
    }
  }
}

case class BlendFunction(sourceRGB: Int, destRGB: Int, sourceAlpha: Int, destAlpha: Int)

