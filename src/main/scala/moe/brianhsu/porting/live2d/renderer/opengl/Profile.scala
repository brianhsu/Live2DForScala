package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.porting.live2d.adapter.OpenGL

import java.nio.IntBuffer

class Profile(implicit gl: OpenGL) {

  import gl._

  private val lastArrayBufferBinding: Array[Int] = new Array(1)
  private val lastElementArrayBufferBinding: Array[Int] = new Array(1)
  private val lastProgram: Array[Int] = new Array(1)
  private val lastActiveTexture: Array[Int] = new Array(1)
  private val lastTexture1Binding2D: Array[Int] = new Array(1)
  private val lastTexture0Binding2D: Array[Int] = new Array(1)
  private val lastVertexAttribArrayEnabled: Array[Int] = new Array(4)
  private var lastScissorTest: Boolean = false
  private var lastStencilTest: Boolean = false
  private var lastDepthTest: Boolean = false
  private var lastCullFace: Boolean = false
  private var lastBlend: Boolean = false
  private val lastFrontFace: Array[Int] = new Array(1)
  private val lastColorMask: Array[Byte] = new Array(4)
  private val lastBlending: Array[Int] = new Array(4)
  private val lastFBO: Array[Int] = new Array(1)
  private val lastViewport: Array[Int] = new Array(4)

  def getLastFBO: Int = lastFBO(0)

  def gatLastViewPort: Array[Int] = lastViewport

  def save(): Unit = {
    gl.glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBufferBinding, 0)
    gl.glGetIntegerv(GL_ELEMENT_ARRAY_BUFFER_BINDING, lastElementArrayBufferBinding, 0)
    gl.glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram, 0)

    gl.glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture, 0)
    gl.glActiveTexture(GL_TEXTURE1)
    gl.glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture1Binding2D, 0)

    gl.glActiveTexture(GL_TEXTURE0)
    gl.glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture0Binding2D, 0)

    gl.glGetVertexAttribiv(0, GL_VERTEX_ATTRIB_ARRAY_ENABLED, lastVertexAttribArrayEnabled, 0)
    gl.glGetVertexAttribiv(1, GL_VERTEX_ATTRIB_ARRAY_ENABLED, lastVertexAttribArrayEnabled, 1)
    gl.glGetVertexAttribiv(2, GL_VERTEX_ATTRIB_ARRAY_ENABLED, lastVertexAttribArrayEnabled, 2)
    gl.glGetVertexAttribiv(3, GL_VERTEX_ATTRIB_ARRAY_ENABLED, lastVertexAttribArrayEnabled, 3)

    lastScissorTest = gl.glIsEnabled(GL_SCISSOR_TEST)
    lastStencilTest = gl.glIsEnabled(GL_STENCIL_TEST)
    lastDepthTest = gl.glIsEnabled(GL_DEPTH_TEST)
    lastCullFace = gl.glIsEnabled(GL_CULL_FACE)
    lastBlend = gl.glIsEnabled(GL_BLEND)

    gl.glGetIntegerv(GL_FRONT_FACE, lastFrontFace, 0)

    gl.glGetBooleanv(GL_COLOR_WRITEMASK, lastColorMask, 0)

    gl.glGetIntegerv(GL_BLEND_SRC_RGB, lastBlending, 0)
    gl.glGetIntegerv(GL_BLEND_DST_RGB, lastBlending, 1)
    gl.glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlending, 2)
    gl.glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlending, 3)

    gl.glGetIntegerv(GL_FRAMEBUFFER_BINDING, lastFBO, 0)
    gl.glGetIntegerv(GL_VIEWPORT, IntBuffer.wrap(lastViewport))
  }

  def restore(): Unit = {
    gl.glUseProgram(lastProgram(0))

    setGlEnableVertexAttribArray(1, lastVertexAttribArrayEnabled(0) != GL_FALSE)
    setGlEnableVertexAttribArray(1, lastVertexAttribArrayEnabled(1) != GL_FALSE)
    setGlEnableVertexAttribArray(2, lastVertexAttribArrayEnabled(2) != GL_FALSE)
    setGlEnableVertexAttribArray(3, lastVertexAttribArrayEnabled(3) != GL_FALSE)

    setGlEnable(GL_SCISSOR_TEST, lastScissorTest)
    setGlEnable(GL_STENCIL_TEST, lastStencilTest)
    setGlEnable(GL_DEPTH_TEST, lastDepthTest)
    setGlEnable(GL_CULL_FACE, lastCullFace)
    setGlEnable(GL_BLEND, lastBlend)

    gl.glFrontFace(lastFrontFace(0))

    gl.glColorMask(
      lastColorMask(0) == GL_TRUE,
      lastColorMask(1) == GL_TRUE,
      lastColorMask(2) == GL_TRUE,
      lastColorMask(3) == GL_TRUE
    )

    gl.glBindBuffer(GL_ARRAY_BUFFER, lastArrayBufferBinding(0))
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lastElementArrayBufferBinding(0))

    gl.glActiveTexture(GL_TEXTURE1)
    gl.glBindTexture(GL_TEXTURE_2D, lastTexture1Binding2D(0))

    gl.glActiveTexture(GL_TEXTURE0)
    gl.glBindTexture(GL_TEXTURE_2D, lastTexture0Binding2D(0))

    gl.glActiveTexture(lastActiveTexture(0))

    gl.glBlendFuncSeparate(lastBlending(0), lastBlending(1), lastBlending(2), lastBlending(3))
  }

  def setGlEnableVertexAttribArray(index: Int, enabled: Boolean): Unit = {
    if (enabled) {
      gl.glEnableVertexAttribArray(index)
    } else {
      gl.glDisableVertexAttribArray(index)
    }
  }

  def setGlEnable(index: Int, enabled: Boolean): Unit = {
    if (enabled) {
      gl.glEnable(index)
    } else {
      gl.glDisable(index)
    }
  }

}
