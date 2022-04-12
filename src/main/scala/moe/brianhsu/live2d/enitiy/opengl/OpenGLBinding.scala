package moe.brianhsu.live2d.enitiy.opengl

import java.nio.{ByteBuffer, FloatBuffer}

trait OpenGLBinding {
  def glGenTextures(n: Int, textures: Array[Int]): Unit

  def glBindTexture(target: Int, texture: Int): Unit

  def glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: ByteBuffer): Unit

  def glGenerateMipmap(target: Int): Unit

  def glTexParameteri(target: Int, pname: Int, param: Int): Unit

  def glUseProgram(program: Int): Unit

  def glCompileShader(shader: Int): Unit

  def glDeleteProgram(program: Int): Unit

  def glCreateProgram(): Int

  def glAttachShader(program: Int, shader: Int): Unit

  def glDetachShader(program: Int, shader: Int): Unit

  def glDeleteShader(shader: Int): Unit

  def glLinkProgram(program: Int): Unit

  def glCreateShader(`type`: Int): Int

  def glShaderSource(shader: Int, count: Int, string: Array[String]): Unit

  def glGetProgramiv(program: Int, pname: Int, params: Array[Int]): Unit

  def glGetProgramInfoLog(program: Int, bufSize: Int, infoLog: ByteBuffer): Unit

  def glGetShaderiv(shader: Int, pname: Int, params: Array[Int]): Unit

  def glGetShaderInfoLog(shader: Int, bufSize: Int, infoLog: ByteBuffer): Unit

  def glGetAttribLocation(program: Int, name: String): Int

  def glGetUniformLocation(program: Int, name: String): Int

  def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: Array[Float]): Unit

  def glUniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float): Unit

  def glActiveTexture(texture: Int): Unit

  def glUniform1i(location: Int, v0: Int): Unit

  def glEnableVertexAttribArray(index: Int): Unit

  def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: ByteBuffer): Unit

  def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: FloatBuffer): Unit

  def glBlendFuncSeparate(sfactorRGB: Int, dfactorRGB: Int, sfactorAlpha: Int, dfactorAlpha: Int): Unit

  def glGetIntegerv(pname: Int, params: Array[Int], params_offset: Int): Unit

  def glGetVertexAttribiv(index: Int, pname: Int, params: Array[Int], params_offset: Int): Unit

  def glIsEnabled(cap: Int): Boolean

  def glGetBooleanv(pname: Int, data: Array[Byte]): Unit

  def glDisableVertexAttribArray(index: Int): Unit

  def glEnable(cap: Int): Unit

  def glDisable(cap: Int): Unit

  def glFrontFace(mode: Int): Unit

  def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit

  def glBindBuffer(target: Int, buffer: Int): Unit

  def glGetIntegerv(pname: Int, params: Array[Int]): Unit

  def glGenFramebuffers(n: Int, framebuffers: Array[Int]): Unit

  def glBindFramebuffer(target: Int, framebuffer: Int): Unit

  def glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int): Unit

  def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit

  def glClear(mask: Int): Unit

  def glDeleteTextures(n: Int, textures: Array[Int]): Unit

  def glDeleteFramebuffers(n: Int, framebuffers: Array[Int]): Unit

  def glDrawElements(mode: Int, count: Int, `type`: Int, indices: ByteBuffer): Unit

  def glClearDepth(depth: Double): Unit

  def glBlendFunc(sfactor: Int, dfactor: Int): Unit

  def glDrawArrays(mode: Int, first: Int, count: Int): Unit

  def newDirectFloatBuffer(float: Array[Float]): FloatBuffer

  def glViewport(x: Int, y: Int, w: Int, h: Int): Unit

  def GL_TEXTURE_2D: Int
  def GL_RGBA: Int
  def GL_UNSIGNED_BYTE: Int
  def GL_TEXTURE_MIN_FILTER: Int
  def GL_LINEAR_MIPMAP_LINEAR: Int
  def GL_TEXTURE_MAG_FILTER: Int
  def GL_LINEAR: Int
  def GL_VERTEX_SHADER: Int
  def GL_FRAGMENT_SHADER: Int
  def GL_INFO_LOG_LENGTH: Int
  def GL_ZERO: Int
  def GL_ONE: Int
  def GL_ONE_MINUS_SRC_ALPHA: Int
  def GL_ONE_MINUS_SRC_COLOR: Int
  def GL_DST_COLOR: Int
  def GL_TEXTURE1: Int
  def GL_TEXTURE0: Int
  def GL_FLOAT: Int
  def GL_ARRAY_BUFFER_BINDING: Int
  def GL_ELEMENT_ARRAY_BUFFER_BINDING: Int
  def GL_CURRENT_PROGRAM: Int
  def GL_ACTIVE_TEXTURE: Int
  def GL_TEXTURE_BINDING_2D: Int
  def GL_VERTEX_ATTRIB_ARRAY_ENABLED: Int
  def GL_SCISSOR_TEST: Int
  def GL_STENCIL_TEST: Int
  def GL_DEPTH_TEST: Int
  def GL_CULL_FACE: Int
  def GL_BLEND: Int
  def GL_FRONT_FACE: Int
  def GL_COLOR_WRITEMASK: Int
  def GL_BLEND_SRC_RGB: Int
  def GL_BLEND_DST_RGB: Int
  def GL_BLEND_SRC_ALPHA: Int
  def GL_BLEND_DST_ALPHA: Int
  def GL_FRAMEBUFFER_BINDING: Int
  def GL_VIEWPORT: Int
  def GL_FALSE: Int
  def GL_TRUE: Int
  def GL_ARRAY_BUFFER: Int
  def GL_ELEMENT_ARRAY_BUFFER: Int
  def GL_TEXTURE_WRAP_S: Int
  def GL_CLAMP_TO_EDGE: Int
  def GL_TEXTURE_WRAP_T: Int
  def GL_FRAMEBUFFER: Int
  def GL_COLOR_ATTACHMENT0: Int
  def GL_COLOR_BUFFER_BIT: Int
  def GL_CCW: Int
  def GL_TRIANGLES: Int
  def GL_UNSIGNED_SHORT: Int
  def GL_SRC_ALPHA: Int
  def GL_DEPTH_BUFFER_BIT: Int
  def GL_TRIANGLE_FAN: Int
}
