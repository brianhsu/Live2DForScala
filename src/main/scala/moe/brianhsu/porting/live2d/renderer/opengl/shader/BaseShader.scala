package moe.brianhsu.porting.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, ShaderCompiler}
import moe.brianhsu.porting.live2d.renderer.opengl.shader.BaseShader.ShaderCleaner

import java.lang.ref.Cleaner
import scala.util.Try

object BaseShader {
  private val cleaner = Cleaner.create()
  class ShaderCleaner(shaderName: String, shaderProgramId: Int)(gl: OpenGLBinding) extends Runnable {
    override def run(): Unit = {
      System.out.println(s"Cleanup shaderProgramId: $shaderProgramId of shaderName: $shaderName")
      gl.glDeleteProgram(shaderProgramId)
    }
  }
}

abstract class BaseShader(gl: OpenGLBinding, shaderCompiler: ShaderCompiler) {


  def this(gl: OpenGLBinding) = this(gl, new ShaderCompiler(gl))

  import gl._

  protected def vertexShaderSource: String
  protected def fragmentShaderSource: String

  protected val shaderProgram: Int = createShaderProgram()

  def positionLocation: Int = gl.glGetAttribLocation(shaderProgram, "position")
  def uvLocation: Int = gl.glGetAttribLocation(shaderProgram, "uv")
  def textureLocation: Int = gl.glGetUniformLocation(shaderProgram, "texture")
  def baseColorLocation: Int = gl.glGetUniformLocation(shaderProgram, "baseColor")

  def useProgram(): Unit = gl.glUseProgram(shaderProgram)

  private def createShaderProgram(): Int = {
    val shaderProgram = gl.glCreateProgram()
    val vertexShaderHolder = compileShader(GL_VERTEX_SHADER, vertexShaderSource)
    val fragmentShaderHolder = compileShader(GL_FRAGMENT_SHADER, fragmentShaderSource)

    vertexShaderHolder.foreach(shaderId => gl.glAttachShader(shaderProgram, shaderId))
    fragmentShaderHolder.foreach(shaderId => gl.glAttachShader(shaderProgram, shaderId))

    val linkedProgram = linkProgram(shaderProgram)

    vertexShaderHolder.foreach(shaderId => detachAndDeleteShader(shaderProgram, shaderId))
    fragmentShaderHolder.foreach(shaderId => detachAndDeleteShader(shaderProgram, shaderId))

    vertexShaderHolder.failed.foreach(throw _)
    fragmentShaderHolder.failed.foreach(throw _)
    linkedProgram.failed.foreach(throw _)

    BaseShader.cleaner.register(this, new ShaderCleaner(this.toString, shaderProgram)(gl))
    shaderProgram
  }

  private def detachAndDeleteShader(programId: Int, shaderId: Int): Unit = {
    gl.glDetachShader(programId, shaderId)
    gl.glDeleteShader(shaderId)
  }

  private def linkProgram(programId: Int): Try[Int] = Try {
    gl.glLinkProgram(programId)
    shaderCompiler.readShaderLinkError(programId) match {
      case None => programId
      case Some(log) =>
        gl.glDeleteProgram(programId)
        throw new Exception(s"Cannot link shader program, error log=$log")
    }
  }

  private def compileShader(shaderType: Int, shaderSourceCode: String): Try[Int] = Try {
    val shaderId = gl.glCreateShader(shaderType)

    gl.glShaderSource(shaderId, 1, Array(shaderSourceCode))
    gl.glCompileShader(shaderId)

    shaderCompiler.readShaderCompileError(shaderId) match {
      case None => shaderId
      case Some(log) =>
        gl.glDeleteShader(shaderId)
        throw new Exception(s"Cannot compile shader, error log=$log")
    }
  }

}
