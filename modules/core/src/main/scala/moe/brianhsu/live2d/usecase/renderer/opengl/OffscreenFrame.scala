package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}

object OffscreenFrame {
  private var offscreenFrame: Map[OpenGLBinding, OffscreenFrame] = Map.empty

  def getInstance(displayBufferWidth: Int, displayBufferHeight: Int)
                 (implicit gl: OpenGLBinding, converter: OpenGLBinding => RichOpenGLBinding): OffscreenFrame = {
    offscreenFrame.get(gl) match {
      case Some(offscreenFrame) => offscreenFrame
      case None =>
        val (colorTextureBufferId, frameBufferId) = createColorTextureBufferAndFrameBuffer(displayBufferWidth, displayBufferHeight)
        offscreenFrame += (gl -> new OffscreenFrame(colorTextureBufferId, frameBufferId)(gl))
        offscreenFrame(gl)
    }
  }

 protected def createColorTextureBufferAndFrameBuffer(displayBufferWidth: Int, displayBufferHeight: Int)(implicit gl: OpenGLBinding, converter: OpenGLBinding => RichOpenGLBinding): (Int, Int) = {
  import gl.constants._
  val colorTextureBufferId = gl.generateTextures(10).head
  gl.glBindTexture(GL_TEXTURE_2D, colorTextureBufferId)
  gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, displayBufferWidth, displayBufferHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
  gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
  gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
  gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
  gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
  gl.glBindTexture(GL_TEXTURE_2D, 0)

  val frameBufferId = gl.generateFrameBuffers(10).head
  val originalFrameBuffer = gl.openGLParameters(GL_FRAMEBUFFER_BINDING)
  gl.glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)
  gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTextureBufferId, 0)
  gl.glBindFramebuffer(GL_FRAMEBUFFER, originalFrameBuffer)

  (colorTextureBufferId, frameBufferId)
}



}

class OffscreenFrame(val colorTextureBufferId: Int, val frameBufferId: Int)(gl: OpenGLBinding) {

  import gl.constants._

  private var originalFrameBufferId: Int = 0

  def beginDraw(currentFrameBufferId: Int): Unit = {
    this.originalFrameBufferId = currentFrameBufferId

    gl.glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId)
    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    gl.glClear(GL_COLOR_BUFFER_BIT)
  }

  def endDraw(): Unit = {
    gl.glBindFramebuffer(GL_FRAMEBUFFER, this.originalFrameBufferId)
  }
}
