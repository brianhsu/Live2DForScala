package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.porting.live2d.adapter.OpenGL
import moe.brianhsu.porting.live2d.renderer.opengl.OffscreenFrame.{colorBufferHolder, textureBufferHolder}

case class BufferIds(textureBufferHolder: Option[Int], colorBufferHolder: Option[Int])

object OffscreenFrame {
  var colorBufferHolder: Option[Int] = None
  var textureBufferHolder: Option[Int] = None
}

class OffscreenFrame(displayBufferWidth: Int, displayBufferHeight: Int)(implicit gl: OpenGL) {

  import gl._

  val bufferIds: BufferIds = createTextureAndColorBuffer()
  var oldFBO: Int = 0

  def createTextureAndColorBuffer(): BufferIds = {

    if (colorBufferHolder.isDefined && textureBufferHolder.isDefined) {
      return BufferIds(colorBufferHolder, textureBufferHolder)
    }

    val newColorBuffer: Array[Int] = Array(0)
    gl.glGenTextures(1, newColorBuffer)
    gl.glBindTexture(GL_TEXTURE_2D, newColorBuffer(0))
    colorBufferHolder = newColorBuffer.find(_ != 0)

    gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, displayBufferWidth, displayBufferHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    gl.glBindTexture(GL_TEXTURE_2D, 0)

    val newTextureBuffer: Array[Int] = Array(0)
    gl.glGenFramebuffers(1, newTextureBuffer)
    textureBufferHolder = newTextureBuffer.find(_ != 0)

    val tmpFramebufferObjectWrapper = Array(Int.MinValue)
    gl.glGetIntegerv(GL_FRAMEBUFFER_BINDING, tmpFramebufferObjectWrapper)
    val tmpFramebufferObject = tmpFramebufferObjectWrapper(0)

    gl.glBindFramebuffer(GL_FRAMEBUFFER, textureBufferHolder.get)
    gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBufferHolder.get, 0)
    gl.glBindFramebuffer(GL_FRAMEBUFFER, tmpFramebufferObject)

    BufferIds(textureBufferHolder, colorBufferHolder)
  }

  def beginDraw(restoreFBO: Int): Unit = {
    bufferIds.textureBufferHolder.foreach { texture =>
      if (restoreFBO < 0) {
        val binding = Array(Int.MinValue)
        gl.glGetIntegerv(GL_FRAMEBUFFER_BINDING, binding)
        oldFBO = binding(0)
      } else {
        oldFBO = restoreFBO
      }

      // マスク用RenderTextureをactiveにセット
      gl.glBindFramebuffer(GL_FRAMEBUFFER, texture)
    }

    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    gl.glClear(GL_COLOR_BUFFER_BIT)
  }

  def endDraw(): Unit = {
    bufferIds.textureBufferHolder.foreach { _ =>
      gl.glBindFramebuffer(GL_FRAMEBUFFER, oldFBO)
    }
  }

  def destroy(): Unit = {
    bufferIds.colorBufferHolder.foreach { colorBuffer =>
      gl.glDeleteTextures(1, Array(colorBuffer))
    }

    bufferIds.textureBufferHolder.foreach { renderTexture =>
      gl.glDeleteFramebuffers(1, Array(renderTexture))
    }
  }
}
