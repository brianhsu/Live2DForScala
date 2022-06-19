package moe.brianhsu.live2d.adapter.gateway.opengl.lwjgl

import moe.brianhsu.live2d.enitiy.opengl.OpenGLConstants
import org.lwjgl.opengl.{GL11, GL12, GL13, GL14, GL15, GL20, GL30}

object LWJGLConstants extends OpenGLConstants {
  override val GL_TEXTURE_2D: Int = GL11.GL_TEXTURE_2D
  override val GL_RGBA: Int = GL11.GL_RGBA
  override val GL_UNSIGNED_BYTE: Int = GL11.GL_UNSIGNED_BYTE
  override val GL_TEXTURE_MIN_FILTER: Int = GL11.GL_TEXTURE_MIN_FILTER
  override val GL_LINEAR_MIPMAP_LINEAR: Int = GL11.GL_LINEAR_MIPMAP_LINEAR
  override val GL_TEXTURE_MAG_FILTER: Int = GL11.GL_TEXTURE_MAG_FILTER
  override val GL_LINEAR: Int = GL11.GL_LINEAR
  override val GL_VERTEX_SHADER: Int = GL20.GL_VERTEX_SHADER
  override val GL_FRAGMENT_SHADER: Int = GL20.GL_FRAGMENT_SHADER
  override val GL_INFO_LOG_LENGTH: Int = GL20.GL_INFO_LOG_LENGTH
  override val GL_ZERO: Int = GL11.GL_ZERO
  override val GL_ONE: Int = GL11.GL_ONE
  override val GL_ONE_MINUS_SRC_ALPHA: Int = GL11.GL_ONE_MINUS_SRC_ALPHA
  override val GL_ONE_MINUS_SRC_COLOR: Int = GL11.GL_ONE_MINUS_SRC_COLOR
  override val GL_DST_COLOR: Int = GL11.GL_DST_COLOR
  override val GL_TEXTURE1: Int = GL13.GL_TEXTURE1
  override val GL_TEXTURE0: Int = GL13.GL_TEXTURE0
  override val GL_FLOAT: Int = GL11.GL_FLOAT
  override val GL_ARRAY_BUFFER_BINDING: Int = GL15.GL_ARRAY_BUFFER_BINDING
  override val GL_ELEMENT_ARRAY_BUFFER_BINDING: Int = GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING
  override val GL_CURRENT_PROGRAM: Int = GL20.GL_CURRENT_PROGRAM
  override val GL_ACTIVE_TEXTURE: Int = GL13.GL_ACTIVE_TEXTURE
  override val GL_TEXTURE_BINDING_2D: Int = GL11.GL_TEXTURE_BINDING_2D
  override val GL_VERTEX_ATTRIB_ARRAY_ENABLED: Int = GL20.GL_VERTEX_ATTRIB_ARRAY_ENABLED
  override val GL_SCISSOR_TEST: Int = GL11.GL_SCISSOR_TEST
  override val GL_STENCIL_TEST: Int = GL11.GL_STENCIL_TEST
  override val GL_DEPTH_TEST: Int = GL11.GL_DEPTH_TEST
  override val GL_CULL_FACE: Int = GL11.GL_CULL_FACE
  override val GL_BLEND: Int = GL11.GL_BLEND
  override val GL_FRONT_FACE: Int = GL11.GL_FRONT_FACE
  override val GL_COLOR_WRITEMASK: Int = GL11.GL_COLOR_WRITEMASK
  override val GL_BLEND_SRC_RGB: Int = GL14.GL_BLEND_SRC_RGB
  override val GL_BLEND_DST_RGB: Int = GL14.GL_BLEND_DST_RGB
  override val GL_BLEND_SRC_ALPHA: Int = GL14.GL_BLEND_SRC_ALPHA
  override val GL_BLEND_DST_ALPHA: Int = GL14.GL_BLEND_DST_ALPHA
  override val GL_FRAMEBUFFER_BINDING: Int = GL30.GL_FRAMEBUFFER_BINDING
  override val GL_VIEWPORT: Int = GL11.GL_VIEWPORT
  override val GL_FALSE: Int = GL11.GL_FALSE
  override val GL_TRUE: Int = GL11.GL_TRUE
  override val GL_ARRAY_BUFFER: Int = GL15.GL_ARRAY_BUFFER
  override val GL_ELEMENT_ARRAY_BUFFER: Int = GL15.GL_ELEMENT_ARRAY_BUFFER
  override val GL_TEXTURE_WRAP_S: Int = GL11.GL_TEXTURE_WRAP_S
  override val GL_CLAMP_TO_EDGE: Int = GL12.GL_CLAMP_TO_EDGE
  override val GL_TEXTURE_WRAP_T: Int = GL11.GL_TEXTURE_WRAP_T
  override val GL_FRAMEBUFFER: Int = GL30.GL_FRAMEBUFFER
  override val GL_COLOR_ATTACHMENT0: Int = GL30.GL_COLOR_ATTACHMENT0
  override val GL_COLOR_BUFFER_BIT: Int = GL11.GL_COLOR_BUFFER_BIT
  override val GL_CCW: Int = GL11.GL_CCW
  override val GL_TRIANGLES: Int = GL11.GL_TRIANGLES
  override val GL_UNSIGNED_SHORT: Int = GL11.GL_UNSIGNED_SHORT
  override val GL_SRC_ALPHA: Int = GL11.GL_SRC_ALPHA
  override val GL_DEPTH_BUFFER_BIT: Int = GL11.GL_DEPTH_BUFFER_BIT
  override val GL_TRIANGLE_FAN: Int = GL11.GL_TRIANGLE_FAN

}
