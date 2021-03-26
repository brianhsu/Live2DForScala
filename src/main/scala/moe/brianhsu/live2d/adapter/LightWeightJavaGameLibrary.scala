package moe.brianhsu.live2d.adapter

import java.nio.{Buffer, ByteBuffer, FloatBuffer, IntBuffer}

class LightWeightJavaGameLibrary extends OpenGL {
  override def glGenTextures(n: Int, textures: Array[Int], textures_offset: Int): Unit = {
    org.lwjgl.opengl.GL11.glGenTextures(textures)
  }

  override def glBindTexture(target: Int, texture: Int): Unit = {
    org.lwjgl.opengl.GL11.glBindTexture(target, texture)
  }

  override def glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: Buffer): Unit = {
    org.lwjgl.opengl.GL11.glTexImage2D(target, level, internalformat, width, height, border, format, `type`, pixels.asInstanceOf[ByteBuffer])
  }

  override def glGenerateMipmap(target: Int): Unit = {
    org.lwjgl.opengl.GL30.glGenerateMipmap(target)
  }

  override def glTexParameteri(target: Int, pname: Int, param: Int): Unit = {
    org.lwjgl.opengl.GL11.glTexParameteri(target, pname, param)
  }

  override def glViewport(x: Int, y: Int, width: Int, height: Int): Unit = {
    org.lwjgl.opengl.GL11.glViewport(x, y, width, height)
  }

  override def glUseProgram(program: Int): Unit = {
    org.lwjgl.opengl.GL20.glUseProgram(program)
  }

  override def glCompileShader(shader: Int): Unit = {
    org.lwjgl.opengl.GL20.glCompileShader(shader)
  }

  override def glDeleteProgram(program: Int): Unit = {
    org.lwjgl.opengl.GL20.glDeleteProgram(program)
  }

  override def glCreateProgram(): Int = {
    org.lwjgl.opengl.GL20.glCreateProgram()
  }

  override def glAttachShader(program: Int, shader: Int): Unit = {
    org.lwjgl.opengl.GL20.glAttachShader(program, shader)
  }

  override def glDetachShader(program: Int, shader: Int): Unit = {
    org.lwjgl.opengl.GL20.glDetachShader(program, shader)
  }

  override def glDeleteShader(shader: Int): Unit = {
    org.lwjgl.opengl.GL20.glDeleteShader(shader)
  }

  override def glLinkProgram(program: Int): Unit = {
    org.lwjgl.opengl.GL20.glLinkProgram(program)
  }

  override def glCreateShader(`type`: Int): Int = {
    org.lwjgl.opengl.GL20.glCreateShader(`type`)
  }

  override def glShaderSource(shader: Int, count: Int, string: Array[String], length: IntBuffer): Unit = {
    org.lwjgl.opengl.GL20.glShaderSource(shader, string(0))
  }

  override def glGetProgramiv(program: Int, pname: Int, params: IntBuffer): Unit = {
    org.lwjgl.opengl.GL20.glGetProgramiv(program, pname, params)
  }

  override def glGetProgramInfoLog(program: Int, bufSize: Int, length: IntBuffer, infoLog: ByteBuffer): Unit = {
    org.lwjgl.opengl.GL20.glGetProgramInfoLog(program, length, infoLog)
  }

  override def glGetShaderiv(shader: Int, pname: Int, params: IntBuffer): Unit = {
    org.lwjgl.opengl.GL20.glGetShaderiv(shader, pname, params)
  }

  override def glGetShaderInfoLog(shader: Int, bufSize: Int, length: IntBuffer, infoLog: ByteBuffer): Unit = {
    org.lwjgl.opengl.GL20.glGetShaderInfoLog(shader, length, infoLog)
  }

  override def glGetAttribLocation(program: Int, name: String): Int = {
    org.lwjgl.opengl.GL20.glGetAttribLocation(program, name)
  }

  override def glGetUniformLocation(program: Int, name: String): Int = {
    org.lwjgl.opengl.GL20.glGetUniformLocation(program, name)
  }

  override def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer): Unit = {
    org.lwjgl.opengl.GL20.glUniformMatrix4fv(location, transpose, value)
  }

  override def glUniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float): Unit = {
    org.lwjgl.opengl.GL20.glUniform4f(location, v0, v1, v2, v3)
  }

  override def glActiveTexture(texture: Int): Unit = {
    org.lwjgl.opengl.GL13C.glActiveTexture(texture)
  }

  override def glUniform1i(location: Int, v0: Int): Unit = {
    org.lwjgl.opengl.GL20.glUniform1i(location, v0)
  }

  override def glEnableVertexAttribArray(index: Int): Unit = {
    org.lwjgl.opengl.GL20.glEnableVertexAttribArray(index)
  }

  override def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: ByteBuffer): Unit = {
    org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, `type`, normalized, stride, ptr)
  }

  override def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: FloatBuffer): Unit = {
    org.lwjgl.opengl.GL20.glVertexAttribPointer(indx, size, `type`, normalized, stride, ptr)
  }

  override def glBlendFuncSeparate(sfactorRGB: Int, dfactorRGB: Int, sfactorAlpha: Int, dfactorAlpha: Int): Unit = {
    org.lwjgl.opengl.GL14C.glBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha)
  }

  override def glGetIntegerv(pname: Int, params: Array[Int], params_offset: Int): Unit = {
    org.lwjgl.opengl.GL11.glGetIntegerv(pname, params)
  }

  override def glGetVertexAttribiv(index: Int, pname: Int, params: Array[Int], params_offset: Int): Unit = {
    org.lwjgl.opengl.GL20C.glGetVertexAttribiv(index, pname, params)
  }

  override def glIsEnabled(cap: Int): Boolean = {
    org.lwjgl.opengl.GL11.glIsEnabled(cap)
  }

  override def glGetBooleanv(pname: Int, data: Array[Byte], data_offset: Int): Unit = {
    org.lwjgl.opengl.GL11.glGetBooleanv(pname, ByteBuffer.wrap(data))
  }

  override def glDisableVertexAttribArray(index: Int): Unit = {
    org.lwjgl.opengl.GL20.glDisableVertexAttribArray(index)
  }

  override def glEnable(cap: Int): Unit = {
    org.lwjgl.opengl.GL11.glEnable(cap)
  }

  override def glDisable(cap: Int): Unit = {
    org.lwjgl.opengl.GL11.glDisable(cap)
  }

  override def glFrontFace(mode: Int): Unit = {
    org.lwjgl.opengl.GL11.glFrontFace(mode)
  }

  override def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit = {
    org.lwjgl.opengl.GL11.glColorMask(red, green, blue, alpha)
  }

  override def glBindBuffer(target: Int, buffer: Int): Unit = {
    org.lwjgl.opengl.GL15.glBindBuffer(target, buffer)
  }

  override def glGetIntegerv(pname: Int, params: IntBuffer): Unit = {
    org.lwjgl.opengl.GL11.glGetIntegerv(pname, params)
  }

  override def glGenFramebuffers(n: Int, framebuffers: Array[Int], framebuffers_offset: Int): Unit = {
    org.lwjgl.opengl.GL11.glGetIntegerv(n, framebuffers)
  }

  override def glBindFramebuffer(target: Int, framebuffer: Int): Unit = {
    org.lwjgl.opengl.GL30.glBindFramebuffer(target, framebuffer)
  }

  override def glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int): Unit = {
    org.lwjgl.opengl.GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level)
  }

  override def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
    org.lwjgl.opengl.GL11.glClearColor(red, green, blue, alpha)
  }

  override def glClear(mask: Int): Unit = {
    org.lwjgl.opengl.GL11.glClear(mask)
  }

  override def glDeleteTextures(n: Int, textures: Array[Int], textures_offset: Int): Unit = {
    org.lwjgl.opengl.GL11.glDeleteTextures(textures)
  }

  override def glDeleteFramebuffers(n: Int, framebuffers: Array[Int], framebuffers_offset: Int): Unit = {
    org.lwjgl.opengl.GL30.glDeleteFramebuffers(framebuffers)
  }

  override def glDrawElements(mode: Int, count: Int, `type`: Int, indices: ByteBuffer): Unit = {
    org.lwjgl.opengl.GL11.glDrawElements(mode, `type`, indices)
  }

  override def glClearDepth(depth: Double): Unit = {
    org.lwjgl.opengl.GL11.glClearDepth(depth)
  }

  override def glBlendFunc(sfactor: Int, dfactor: Int): Unit = {
    org.lwjgl.opengl.GL11.glBlendFunc(sfactor, dfactor)
  }

  override def glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
    org.lwjgl.opengl.GL11.glDrawArrays(mode, first, count)
  }

  override val GL_TEXTURE_2D: Int = org.lwjgl.opengl.GL11.GL_TEXTURE_2D
  override val GL_RGBA: Int = org.lwjgl.opengl.GL11.GL_RGBA
  override val GL_UNSIGNED_BYTE: Int = org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
  override val GL_TEXTURE_MIN_FILTER: Int = org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER
  override val GL_LINEAR_MIPMAP_LINEAR: Int = org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR
  override val GL_TEXTURE_MAG_FILTER: Int = org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER
  override val GL_LINEAR: Int = org.lwjgl.opengl.GL11.GL_LINEAR
  override val GL_VERTEX_SHADER: Int = org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
  override val GL_FRAGMENT_SHADER: Int = org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
  override val GL_INFO_LOG_LENGTH: Int = org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH
  override val GL_ZERO: Int = org.lwjgl.opengl.GL11.GL_ZERO
  override val GL_ONE: Int = org.lwjgl.opengl.GL11.GL_ONE
  override val GL_ONE_MINUS_SRC_ALPHA: Int = org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA
  override val GL_ONE_MINUS_SRC_COLOR: Int = org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR
  override val GL_DST_COLOR: Int = org.lwjgl.opengl.GL11.GL_DST_COLOR
  override val GL_TEXTURE1: Int = org.lwjgl.opengl.GL13.GL_TEXTURE1
  override val GL_TEXTURE0: Int = org.lwjgl.opengl.GL13.GL_TEXTURE0
  override val GL_FLOAT: Int = org.lwjgl.opengl.GL11.GL_FLOAT
  override val GL_ARRAY_BUFFER_BINDING: Int = org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER_BINDING
  override val GL_ELEMENT_ARRAY_BUFFER_BINDING: Int = org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING
  override val GL_CURRENT_PROGRAM: Int = org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM
  override val GL_ACTIVE_TEXTURE: Int = org.lwjgl.opengl.GL13.GL_ACTIVE_TEXTURE
  override val GL_TEXTURE_BINDING_2D: Int = org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D
  override val GL_VERTEX_ATTRIB_ARRAY_ENABLED: Int = org.lwjgl.opengl.GL20.GL_VERTEX_ATTRIB_ARRAY_ENABLED
  override val GL_SCISSOR_TEST: Int = org.lwjgl.opengl.GL11.GL_SCISSOR_TEST
  override val GL_STENCIL_TEST: Int = org.lwjgl.opengl.GL11.GL_STENCIL_TEST
  override val GL_DEPTH_TEST: Int = org.lwjgl.opengl.GL11.GL_DEPTH_TEST
  override val GL_CULL_FACE: Int = org.lwjgl.opengl.GL11.GL_CULL_FACE
  override val GL_BLEND: Int = org.lwjgl.opengl.GL11.GL_BLEND
  override val GL_FRONT_FACE: Int = org.lwjgl.opengl.GL11.GL_FRONT_FACE
  override val GL_COLOR_WRITEMASK: Int = org.lwjgl.opengl.GL11.GL_COLOR_WRITEMASK
  override val GL_BLEND_SRC_RGB: Int = org.lwjgl.opengl.GL14.GL_BLEND_SRC_RGB
  override val GL_BLEND_DST_RGB: Int = org.lwjgl.opengl.GL14.GL_BLEND_DST_RGB
  override val GL_BLEND_SRC_ALPHA: Int = org.lwjgl.opengl.GL14.GL_BLEND_SRC_ALPHA
  override val GL_BLEND_DST_ALPHA: Int = org.lwjgl.opengl.GL14.GL_BLEND_DST_ALPHA
  override val GL_FRAMEBUFFER_BINDING: Int = org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_BINDING
  override val GL_VIEWPORT: Int = org.lwjgl.opengl.GL11.GL_VIEWPORT
  override val GL_FALSE: Int = org.lwjgl.opengl.GL11.GL_FALSE
  override val GL_TRUE: Int = org.lwjgl.opengl.GL11.GL_TRUE
  override val GL_ARRAY_BUFFER: Int = org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
  override val GL_ELEMENT_ARRAY_BUFFER: Int = org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER
  override val GL_TEXTURE_WRAP_S: Int = org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S
  override val GL_CLAMP_TO_EDGE: Int = org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
  override val GL_TEXTURE_WRAP_T: Int = org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T
  override val GL_FRAMEBUFFER: Int = org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
  override val GL_COLOR_ATTACHMENT0: Int = org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
  override val GL_COLOR_BUFFER_BIT: Int = org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
  override val GL_CCW: Int = org.lwjgl.opengl.GL11.GL_CCW
  override val GL_TRIANGLES: Int = org.lwjgl.opengl.GL11.GL_TRIANGLES
  override val GL_UNSIGNED_SHORT: Int = org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT
  override val GL_SRC_ALPHA: Int = org.lwjgl.opengl.GL11.GL_SRC_ALPHA
  override val GL_DEPTH_BUFFER_BIT: Int = org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
  override val GL_TRIANGLE_FAN: Int = org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN
}
