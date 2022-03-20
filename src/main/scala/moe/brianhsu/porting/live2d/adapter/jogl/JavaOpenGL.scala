package moe.brianhsu.porting.live2d.adapter.jogl

import com.jogamp.opengl.{GL, GL2, GL2ES2}
import moe.brianhsu.porting.live2d.adapter.OpenGL

import java.nio.{Buffer, ByteBuffer, FloatBuffer, IntBuffer}

class JavaOpenGL(gl: GL2) extends OpenGL {
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

  override def glGenTextures(n: Int, textures: Array[Int]): Unit = {
    gl.glGenTextures(n, textures, 0)
  }

  override def glBindTexture(target: Int, texture: Int): Unit = {
    gl.glBindTexture(target, texture)
  }

  override def glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int,
                            border: Int, format: Int, `type`: Int, pixels: ByteBuffer): Unit = {
    gl.glTexImage2D(target, level, internalformat, width, height, border, format, `type`, pixels)
  }

  override def glGenerateMipmap(target: Int): Unit = {
    gl.glGenerateMipmap(target)
  }

  override def glTexParameteri(target: Int, pname: Int, param: Int): Unit = {
    gl.glTexParameteri(target, pname, param)
  }

  override def glViewport(x: Int, y: Int, width: Int, height: Int): Unit = {
    gl.glViewport(x, y, width, height)
  }


  override def glUseProgram(program: Int): Unit = {
    gl.glUseProgram(program)
  }

  override def glCompileShader(shader: Int): Unit = {
    gl.glCompileShader(shader)
  }

  override def glDeleteProgram(program: Int): Unit = {
    gl.glDeleteProgram(program)
  }

  override def glCreateProgram(): Int = {
    gl.glCreateProgram()
  }

  override def glAttachShader(program: Int, shader: Int): Unit = {
    gl.glAttachShader(program, shader)
  }

  override def glDetachShader(program: Int, shader: Int): Unit = {
    gl.glDetachShader(program, shader)
  }

  override def glDeleteShader(shader: Int): Unit = {
    gl.glDeleteShader(shader)
  }

  override def glLinkProgram(program: Int): Unit = {
    gl.glLinkProgram(program)
  }

  override def glCreateShader(`type`: Int): Int = {
    gl.glCreateShader(`type`)
  }

  override def glShaderSource(shader: Int, count: Int, string: Array[String]): Unit = {
    gl.glShaderSource(shader, count, string, null, 0)
  }

  override def glGetProgramiv(program: Int, pname: Int, params: Array[Int]): Unit = {
    gl.glGetProgramiv(program, pname, params, 0)
  }

  override def glGetProgramInfoLog(program: Int, bufSize: Int, length: IntBuffer, infoLog: ByteBuffer): Unit = {
    gl.glGetProgramInfoLog(program, bufSize, length, infoLog)
  }

  override def glGetShaderi(shader: Int, pname: Int): Int = {
    return 0
  }

  override def glGetShaderiv(shader: Int, pname: Int, params: IntBuffer): Unit = {
    gl.glGetShaderiv(shader, pname, params)
  }

  override def glGetShaderInfoLog(shader: Int, bufSize: Int, length: IntBuffer, infoLog: ByteBuffer): Unit = {
    gl.glGetShaderInfoLog(shader, bufSize, length, infoLog)
  }

  override def glGetAttribLocation(program: Int, name: String): Int = {

    gl.glGetAttribLocation(program, name)
  }

  override def glGetUniformLocation(program: Int, name: String): Int = {
    gl.glGetUniformLocation(program, name)
  }

  override def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer): Unit = {
    gl.glUniformMatrix4fv(location, count, transpose, value)
  }

  override def glUniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float): Unit = {
    gl.glUniform4f(location, v0, v1, v2, v3)
  }

  override def glActiveTexture(texture: Int): Unit = {
    gl.glActiveTexture(texture)
  }

  override def glUniform1i(location: Int, v0: Int): Unit = {
    gl.glUniform1i(location, v0)
  }

  override def glEnableVertexAttribArray(index: Int): Unit = {
    gl.glEnableVertexAttribArray(index)
  }

  override def glVertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Boolean,
                                     stride: Int, ptr: ByteBuffer): Unit = {
    gl.glVertexAttribPointer(index, size, `type`, normalized, stride, ptr)
  }

  override def glVertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Boolean,
                                     stride: Int, ptr: FloatBuffer): Unit = {
    gl.glVertexAttribPointer(index, size, `type`, normalized, stride, ptr)
  }

  override def glBlendFuncSeparate(sfactorRGB: Int, dfactorRGB: Int, sfactorAlpha: Int, dfactorAlpha: Int): Unit = {
    gl.glBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha)
  }

  override def glGetIntegerv(pname: Int, params: Array[Int], params_offset: Int): Unit = {
    gl.glGetIntegerv(pname, params, params_offset)
  }

  override def glGetVertexAttribiv(index: Int, pname: Int, params: Array[Int], params_offset: Int): Unit = {
    gl.glGetVertexAttribiv(index, pname, params, params_offset)
  }

  override def glIsEnabled(cap: Int): Boolean = {
    gl.glIsEnabled(cap)
  }

  override def glGetBooleanv(pname: Int, data: Array[Byte]): Unit = {
    gl.glGetBooleanv(pname, data, 0)
  }

  override def glDisableVertexAttribArray(index: Int): Unit = {
    gl.glDisableVertexAttribArray(index)
  }

  override def glEnable(cap: Int): Unit = {
    gl.glEnable(cap)
  }

  override def glDisable(cap: Int): Unit = {
    gl.glDisable(cap)
  }

  override def glFrontFace(mode: Int): Unit = {
    gl.glFrontFace(mode)
  }

  override def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit = {
    gl.glColorMask(red, green, blue, alpha)
  }

  override def glBindBuffer(target: Int, buffer: Int): Unit = {
    gl.glBindBuffer(target, buffer)
  }

  override def glGetIntegerv(pname: Int, params: IntBuffer): Unit = {
    gl.glGetIntegerv(pname, params)
  }

  override def glGenFramebuffers(n: Int, framebuffers: Array[Int]): Unit = {
    gl.glGenFramebuffers(n, framebuffers, 0)
  }

  override def glBindFramebuffer(target: Int, framebuffer: Int): Unit = {
    gl.glBindFramebuffer(target, framebuffer)
  }

  override def glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int): Unit = {
    gl.glFramebufferTexture2D(target, attachment, textarget, texture, level)
  }

  override def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
    gl.glClearColor(red, green, blue, alpha)
  }

  override def glClear(mask: Int): Unit = {
    gl.glClear(mask)
  }

  override def glDeleteTextures(n: Int, textures: Array[Int]): Unit = {
    gl.glDeleteTextures(n, textures, 0)
  }

  override def glDeleteFramebuffers(n: Int, framebuffers: Array[Int]): Unit = {
    gl.glDeleteFramebuffers(n, framebuffers, 0)
  }

  override def glDrawElements(mode: Int, count: Int, `type`: Int, indices: ByteBuffer): Unit = {
    gl.glDrawElements(mode, count, `type`, indices)
  }

  override def glClearDepth(depth: Double): Unit = {
    gl.glClearDepth(depth)
  }

  override def glBlendFunc(sfactor: Int, dfactor: Int): Unit = {
    gl.glBlendFunc(sfactor, dfactor)
  }

  override def glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
    gl.glDrawArrays(mode, first, count)
  }

}