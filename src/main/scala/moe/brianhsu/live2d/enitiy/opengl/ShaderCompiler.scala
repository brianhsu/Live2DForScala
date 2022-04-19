package moe.brianhsu.live2d.enitiy.opengl

import java.nio.ByteBuffer

class ShaderCompiler(gl: OpenGLBinding) {

  def readShaderCompileError(shaderId: Int): Option[String] = {
    val logLengthHolder = Array(Int.MinValue)
    gl.glGetShaderiv(shaderId, gl.GL_INFO_LOG_LENGTH, logLengthHolder)
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
    gl.glGetProgramiv(programId, gl.GL_INFO_LOG_LENGTH, logLengthHolder)
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
