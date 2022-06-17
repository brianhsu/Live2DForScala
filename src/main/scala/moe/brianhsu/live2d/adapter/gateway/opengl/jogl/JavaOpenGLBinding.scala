package moe.brianhsu.live2d.adapter.gateway.opengl.jogl

import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.{GL, GL2, GL2ES1, GL2ES2}
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, OpenGLConstants}

import java.nio.{ByteBuffer, FloatBuffer, IntBuffer}

class JavaOpenGLBinding(gl: GL, gl2: GL2, gl2es1: GL2ES1, gl2es2: GL2ES2) extends OpenGLBinding {


  def this(gl2: GL2) = this(gl2, gl2, gl2, gl2)

  override val openGLConstants: OpenGLConstants = JavaOpenGLConstants

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

  override def glActiveTexture(texture: Int): Unit = {
    gl.glActiveTexture(texture)
  }

  override def glBlendFuncSeparate(sfactorRGB: Int, dfactorRGB: Int, sfactorAlpha: Int, dfactorAlpha: Int): Unit = {
    gl.glBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha)
  }

  override def glIsEnabled(cap: Int): Boolean = {
    gl.glIsEnabled(cap)
  }

  override def glGetBooleanv(pname: Int, data: Array[Byte]): Unit = {
    gl.glGetBooleanv(pname, data, 0)
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

  override def glGetIntegerv(pname: Int, params: Array[Int]): Unit = {
    gl.glGetIntegerv(pname, params, 0)
  }

  override def glGetIntegerv(pname: Int, params: Array[Int], params_offset: Int): Unit = {
    gl.glGetIntegerv(pname, params, params_offset)
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

  override def glBlendFunc(sfactor: Int, dfactor: Int): Unit = {
    gl.glBlendFunc(sfactor, dfactor)
  }

  override def glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
    gl.glDrawArrays(mode, first, count)
  }

  override def glDrawElements(mode: Int, count: Int, `type`: Int, indices: ByteBuffer): Unit = {
    gl2es1.glDrawElements(mode, count, `type`, indices)
  }

  override def glUseProgram(program: Int): Unit = {
    gl2es2.glUseProgram(program)
  }

  override def glCompileShader(shader: Int): Unit = {
    gl2es2.glCompileShader(shader)
  }

  override def glDeleteProgram(program: Int): Unit = {
    gl2es2.glDeleteProgram(program)
  }

  override def glCreateProgram(): Int = {
    gl2es2.glCreateProgram()
  }

  override def glAttachShader(program: Int, shader: Int): Unit = {
    gl2es2.glAttachShader(program, shader)
  }

  override def glDetachShader(program: Int, shader: Int): Unit = {
    gl2es2.glDetachShader(program, shader)
  }

  override def glDeleteShader(shader: Int): Unit = {
    gl2es2.glDeleteShader(shader)
  }

  override def glLinkProgram(program: Int): Unit = {
    gl2es2.glLinkProgram(program)
  }

  override def glCreateShader(`type`: Int): Int = {
    gl2es2.glCreateShader(`type`)
  }

  override def glShaderSource(shader: Int, count: Int, string: Array[String]): Unit = {
    gl2es2.glShaderSource(shader, count, string, null, 0)
  }

  override def glGetProgramiv(program: Int, pname: Int, params: Array[Int]): Unit = {
    gl2es2.glGetProgramiv(program, pname, params, 0)
  }

  override def glGetProgramInfoLog(program: Int, bufSize: Int, infoLog: ByteBuffer): Unit = {
    gl2es2.glGetProgramInfoLog(program, bufSize, IntBuffer.allocate(1), infoLog)
  }

  override def glGetShaderiv(shader: Int, pname: Int, params: Array[Int]): Unit = {
    gl2es2.glGetShaderiv(shader, pname, params, 0)
  }

  override def glGetShaderInfoLog(shader: Int, bufSize: Int, infoLog: ByteBuffer): Unit = {
    gl2es2.glGetShaderInfoLog(shader, bufSize, IntBuffer.allocate(1), infoLog)
  }

  override def glGetAttribLocation(program: Int, name: String): Int = {

    gl2es2.glGetAttribLocation(program, name)
  }

  override def glGetUniformLocation(program: Int, name: String): Int = {
    gl2es2.glGetUniformLocation(program, name)
  }

  override def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: Array[Float]): Unit = {
    gl2es2.glUniformMatrix4fv(location, count, transpose, value, 0)
  }

  override def glUniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float): Unit = {
    gl2es2.glUniform4f(location, v0, v1, v2, v3)
  }

  override def glUniform1i(location: Int, v0: Int): Unit = {
    gl2es2.glUniform1i(location, v0)
  }

  override def glEnableVertexAttribArray(index: Int): Unit = {
    gl2es2.glEnableVertexAttribArray(index)
  }

  override def glGetVertexAttribiv(index: Int, pname: Int, params: Array[Int], params_offset: Int): Unit = {
    gl2es2.glGetVertexAttribiv(index, pname, params, params_offset)
  }

  override def glDisableVertexAttribArray(index: Int): Unit = {
    gl2es2.glDisableVertexAttribArray(index)
  }

  override def glClearDepth(depth: Double): Unit = {
    gl2es2.glClearDepth(depth)
  }

  override def glVertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Boolean,
                                     stride: Int, ptr: ByteBuffer): Unit = {
    gl2.glVertexAttribPointer(index, size, `type`, normalized, stride, ptr)
  }

  override def glVertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Boolean,
                                     stride: Int, ptr: FloatBuffer): Unit = {
    gl2.glVertexAttribPointer(index, size, `type`, normalized, stride, ptr)
  }




  override def newDirectFloatBuffer(floats: Array[Float]): FloatBuffer = {
    Buffers.newDirectFloatBuffer(floats)
  }

}