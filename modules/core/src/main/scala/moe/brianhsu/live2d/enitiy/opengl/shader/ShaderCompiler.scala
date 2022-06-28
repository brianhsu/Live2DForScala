package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.exception.{ShaderCompileException, ShaderLinkException}

import java.nio.ByteBuffer
import scala.util.Try

class ShaderCompiler(gl: OpenGLBinding) {

  import gl.constants._

  def compile(shaderType: Int, shaderSourceCode: String): Try[Int] = Try {
    val shaderId = gl.glCreateShader(shaderType)

    gl.glShaderSource(shaderId, 1, Array(shaderSourceCode))
    gl.glCompileShader(shaderId)

    readShaderCompileError(shaderId) match {
      case None => shaderId
      case Some(log) =>
        gl.glDeleteShader(shaderId)
        throw new ShaderCompileException(shaderId, log)
    }
  }

  def link(programId: Int): Try[Int] = Try {
    gl.glLinkProgram(programId)
    readShaderLinkError(programId) match {
      case None => programId
      case Some(log) =>
        gl.glDeleteProgram(programId)
        throw new ShaderLinkException(programId, log)
    }
  }

  def readShaderCompileError(shaderId: Int): Option[String] = {
    val logLengthHolder = Array(Int.MinValue)
    gl.glGetShaderiv(shaderId, GL_INFO_LOG_LENGTH, logLengthHolder)
    val logLength = logLengthHolder(0)

    logLength match {
      case 0 | Int.MinValue => None
      case _ =>
        val logBuffer = ByteBuffer.allocateDirect(logLength)
        gl.glGetShaderInfoLog(shaderId, logLength, logBuffer)
        Some(byteBufferToString(logBuffer, logLength))
    }
  }

  def readShaderLinkError(programId: Int): Option[String] = {
    val logLengthHolder = Array(Int.MinValue)
    gl.glGetProgramiv(programId, GL_INFO_LOG_LENGTH, logLengthHolder)
    val logLength = logLengthHolder(0)
    logLength match {
      case 0 | Int.MinValue => None
      case _ =>
        val logBuffer = ByteBuffer.allocateDirect(logLength)
        gl.glGetProgramInfoLog(programId, logLength, logBuffer)
        Some(byteBufferToString(logBuffer, logLength))
    }
  }

  private def byteBufferToString(byteBuffer: ByteBuffer, size: Int): String = {
    (0 until size)
      .map(byteBuffer.get(_).toChar)
      .mkString
  }

}
