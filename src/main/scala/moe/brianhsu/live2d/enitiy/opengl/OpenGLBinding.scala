package moe.brianhsu.live2d.enitiy.opengl

import java.nio.{ByteBuffer, FloatBuffer}

trait OpenGLBinding {
  val openGLConstants: OpenGLConstants

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
}
