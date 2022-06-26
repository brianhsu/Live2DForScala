package moe.brianhsu.live2d.adapter.gateway.opengl.jogl

import com.jogamp.opengl.{GL, GL2ES2}
import moe.brianhsu.live2d.enitiy.opengl.OpenGLConstants

object JavaOpenGLConstants extends OpenGLConstants {
  override val GL_TEXTURE_2D: Int = GL.GL_TEXTURE_2D
  override val GL_RGBA: Int = GL.GL_RGBA
  override val GL_UNSIGNED_BYTE: Int = GL.GL_UNSIGNED_BYTE
  override val GL_TEXTURE_MIN_FILTER: Int = GL.GL_TEXTURE_MIN_FILTER
  override val GL_LINEAR_MIPMAP_LINEAR: Int = GL.GL_LINEAR_MIPMAP_LINEAR
  override val GL_TEXTURE_MAG_FILTER: Int = GL.GL_TEXTURE_MAG_FILTER
  override val GL_LINEAR: Int = GL.GL_LINEAR
  override val GL_VERTEX_SHADER: Int = GL2ES2.GL_VERTEX_SHADER
  override val GL_FRAGMENT_SHADER: Int = GL2ES2.GL_FRAGMENT_SHADER
  override val GL_INFO_LOG_LENGTH: Int = GL2ES2.GL_INFO_LOG_LENGTH
  override val GL_ZERO: Int = GL.GL_ZERO
  override val GL_ONE: Int = GL.GL_ONE
  override val GL_ONE_MINUS_SRC_ALPHA: Int = GL.GL_ONE_MINUS_SRC_ALPHA
  override val GL_ONE_MINUS_SRC_COLOR: Int = GL.GL_ONE_MINUS_SRC_COLOR
  override val GL_DST_COLOR: Int = GL.GL_DST_COLOR
  override val GL_TEXTURE1: Int = GL.GL_TEXTURE1
  override val GL_TEXTURE0: Int = GL.GL_TEXTURE0
  override val GL_FLOAT: Int = GL.GL_FLOAT
  override val GL_ARRAY_BUFFER_BINDING: Int = GL.GL_ARRAY_BUFFER_BINDING
  override val GL_ELEMENT_ARRAY_BUFFER_BINDING: Int = GL.GL_ELEMENT_ARRAY_BUFFER_BINDING
  override val GL_CURRENT_PROGRAM: Int = GL2ES2.GL_CURRENT_PROGRAM
  override val GL_ACTIVE_TEXTURE: Int = GL.GL_ACTIVE_TEXTURE
  override val GL_TEXTURE_BINDING_2D: Int = GL.GL_TEXTURE_BINDING_2D
  override val GL_VERTEX_ATTRIB_ARRAY_ENABLED: Int = GL2ES2.GL_VERTEX_ATTRIB_ARRAY_ENABLED
  override val GL_SCISSOR_TEST: Int = GL.GL_SCISSOR_TEST
  override val GL_STENCIL_TEST: Int = GL.GL_STENCIL_TEST
  override val GL_DEPTH_TEST: Int = GL.GL_DEPTH_TEST
  override val GL_CULL_FACE: Int = GL.GL_CULL_FACE
  override val GL_BLEND: Int = GL.GL_BLEND
  override val GL_FRONT_FACE: Int = GL.GL_FRONT_FACE
  override val GL_COLOR_WRITEMASK: Int = GL.GL_COLOR_WRITEMASK
  override val GL_BLEND_SRC_RGB: Int = GL.GL_BLEND_SRC_RGB
  override val GL_BLEND_DST_RGB: Int = GL.GL_BLEND_DST_RGB
  override val GL_BLEND_SRC_ALPHA: Int = GL.GL_BLEND_SRC_ALPHA
  override val GL_BLEND_DST_ALPHA: Int = GL.GL_BLEND_DST_ALPHA
  override val GL_FRAMEBUFFER_BINDING: Int = GL.GL_FRAMEBUFFER_BINDING
  override val GL_VIEWPORT: Int = GL.GL_VIEWPORT
  override val GL_FALSE: Int = GL.GL_FALSE
  override val GL_TRUE: Int = GL.GL_TRUE
  override val GL_ARRAY_BUFFER: Int = GL.GL_ARRAY_BUFFER
  override val GL_ELEMENT_ARRAY_BUFFER: Int = GL.GL_ELEMENT_ARRAY_BUFFER
  override val GL_TEXTURE_WRAP_S: Int = GL.GL_TEXTURE_WRAP_S
  override val GL_CLAMP_TO_EDGE: Int = GL.GL_CLAMP_TO_EDGE
  override val GL_TEXTURE_WRAP_T: Int = GL.GL_TEXTURE_WRAP_T
  override val GL_FRAMEBUFFER: Int = GL.GL_FRAMEBUFFER
  override val GL_COLOR_ATTACHMENT0: Int = GL.GL_COLOR_ATTACHMENT0
  override val GL_COLOR_BUFFER_BIT: Int = GL.GL_COLOR_BUFFER_BIT
  override val GL_CCW: Int = GL.GL_CCW
  override val GL_TRIANGLES: Int = GL.GL_TRIANGLES
  override val GL_UNSIGNED_SHORT: Int = GL.GL_UNSIGNED_SHORT
  override val GL_SRC_ALPHA: Int = GL.GL_SRC_ALPHA
  override val GL_DEPTH_BUFFER_BIT: Int = GL.GL_DEPTH_BUFFER_BIT
  override val GL_TRIANGLE_FAN: Int = GL.GL_TRIANGLE_FAN
}
