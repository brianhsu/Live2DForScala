package moe.brianhsu.live2d.enitiy.opengl

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.{BlendFunction, ColorWriteMask, ViewPort}

import scala.language.implicitConversions
import reflect.runtime.universe._

object RichOpenGLBinding {
  implicit def fromOpenGLBinding(binding: OpenGLBinding) = new RichOpenGLBinding(binding)

  case class BlendFunction(sourceRGB: Int, destRGB: Int, sourceAlpha: Int, destAlpha: Int)
  case class ColorWriteMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean)
  case class ViewPort(x: Int, y: Int, width: Int, height: Int)
}

class RichOpenGLBinding(binding: OpenGLBinding) {
  import binding.constants._

  def openGLParameter[T: TypeTag](pname: Int): T = {
    pname match {
      case _ if typeOf[T] <:< typeOf[Int] =>
        val pointer = new Array[Int](1)
        binding.glGetIntegerv(pname, pointer, 0)
        pointer(0).asInstanceOf[T]
      case _ if typeOf[T] <:< typeOf[Boolean] =>
        false.asInstanceOf[T]
    }
  }

  def textureBinding2D(texture: Int): Int = {
    binding.glActiveTexture(texture)
    openGLParameter[Int](GL_TEXTURE_BINDING_2D)
  }

  def activeAndBinding2DTexture(textureUnit: Int, textureName: Int): Unit = {
    binding.glActiveTexture(textureUnit)
    binding.glBindTexture(GL_TEXTURE_2D, textureName)
  }


  def blendFunction: BlendFunction = {
    val buffer = new Array[Int](4)

    binding.glGetIntegerv(GL_BLEND_SRC_RGB, buffer, 0)
    binding.glGetIntegerv(GL_BLEND_DST_RGB, buffer, 1)
    binding.glGetIntegerv(GL_BLEND_SRC_ALPHA, buffer, 2)
    binding.glGetIntegerv(GL_BLEND_DST_ALPHA, buffer, 3)

    BlendFunction(buffer(0), buffer(1), buffer(2), buffer(3))
  }

  def updateBlendFunc(blendFunction: BlendFunction): Unit = {
    binding.glBlendFuncSeparate(
      blendFunction.sourceRGB,
      blendFunction.destRGB,
      blendFunction.sourceAlpha,
      blendFunction.destAlpha
    )
  }

  def viewPort: ViewPort = {
    val buffer: Array[Int] = new Array(4)
    binding.glGetIntegerv(GL_VIEWPORT, buffer)
    ViewPort(buffer(0), buffer(1), buffer(2), buffer(3))
  }

  def updateViewPort(viewPort: ViewPort): Unit = {
    binding.glViewport(
      viewPort.x, viewPort.y,
      viewPort.width, viewPort.height
    )
  }

  def vertexAttributes: Array[Boolean] = {
    val temp = new Array[Int](4)
    for (i <- temp.indices) {
      binding.glGetVertexAttribiv(i, GL_VERTEX_ATTRIB_ARRAY_ENABLED, temp, i)
    }
    temp.map(_ == GL_TRUE)
  }

  def setVertexAttributes(buffer: Array[Boolean]): Unit = {
    for (index <- buffer.indices) {
      if (buffer(index)) {
        binding.glEnableVertexAttribArray(index)
      } else {
        binding.glDisableVertexAttribArray(index)
      }
    }
  }

  def colorWriteMask_=(colorWriteMask: ColorWriteMask) = {
    binding.glColorMask(
      colorWriteMask.red, colorWriteMask.green,
      colorWriteMask.blue, colorWriteMask.alpha
    )
  }

  def colorWriteMask: ColorWriteMask = {
    val colorMask: Array[Byte] = new Array(4)
    binding.glGetBooleanv(GL_COLOR_WRITEMASK, colorMask)
    ColorWriteMask(
      colorMask(0) == GL_TRUE, colorMask(1) == GL_TRUE,
      colorMask(2) == GL_TRUE, colorMask(3) == GL_TRUE
    )
  }

  def setCapabilityEnabled(capability: Int, isEnabled: Boolean): Unit = {
    if (isEnabled) {
      binding.glEnable(capability)
    } else {
      binding.glDisable(capability)
    }
  }
}