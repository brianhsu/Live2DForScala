package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.porting.live2d.adapter.OpenGL

import java.nio.IntBuffer

class OffscreenFrame(displayBufferWidth: Int, displayBufferHeight: Int)(implicit gl: OpenGL) {

  import gl._

  val (renderTextureHolder, colorBufferHolder) = createTextureAndColorBuffer()
  var oldFBO: Int = 0

  def createTextureAndColorBuffer(): (Option[Int], Option[Int]) = {

    val colorBuffer: Array[Int] = Array(0)

    gl.glGenTextures(1, colorBuffer, 0)
    gl.glBindTexture(GL_TEXTURE_2D, colorBuffer(0))
    gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, displayBufferWidth, displayBufferHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    gl.glBindTexture(GL_TEXTURE_2D, 0)

    val tmpFramebufferObjectWrapper = IntBuffer.allocate(1)
    gl.glGetIntegerv(GL_FRAMEBUFFER_BINDING, tmpFramebufferObjectWrapper)
    val tmpFramebufferObject = tmpFramebufferObjectWrapper.get()

    val texture: Array[Int] = Array(0)
    gl.glGenFramebuffers(1, texture, 0)
    gl.glBindFramebuffer(GL_FRAMEBUFFER, texture(0))
    gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBuffer(0), 0)
    gl.glBindFramebuffer(GL_FRAMEBUFFER, tmpFramebufferObject)

    (texture.find(_ != 0), colorBuffer.find(_ != 0))
  }

  def beginDraw(restoreFBO: Int): Unit = {
    renderTextureHolder.foreach { texture =>
      if (restoreFBO < 0) {
        val binding = IntBuffer.allocate(1)
        gl.glGetIntegerv(GL_FRAMEBUFFER_BINDING, binding)
        oldFBO = binding.get()
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
    renderTextureHolder.foreach { _ =>
      gl.glBindFramebuffer(GL_FRAMEBUFFER, oldFBO)
    }
  }

  override def finalize(): Unit = {
    colorBufferHolder.foreach { colorBuffer =>
      gl.glDeleteTextures(1, Array(colorBuffer), 0)
    }

    renderTextureHolder.foreach { renderTexture =>
      gl.glDeleteFramebuffers(1, Array(renderTexture), 0)
    }

    super.finalize()
  }

  def getColorBuffer: Int = colorBufferHolder.getOrElse(0)

}
