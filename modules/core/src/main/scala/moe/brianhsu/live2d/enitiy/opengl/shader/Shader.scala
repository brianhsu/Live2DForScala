package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

object Shader {
  private val cleaner = new ShaderCleaner
}

abstract class Shader(gl: OpenGLBinding, shaderCompiler: ShaderCompiler, shaderCleaner: ShaderCleaner) {

  def this(gl: OpenGLBinding) = this(gl, new ShaderCompiler(gl), Shader.cleaner)

  import gl.constants._

  protected def vertexShaderSource: String
  protected def fragmentShaderSource: String

  val programId: Int = createShaderProgram()

  def positionLocation: Int = gl.glGetAttribLocation(programId, "position")
  def uvLocation: Int = gl.glGetAttribLocation(programId, "uv")
  def textureLocation: Int = gl.glGetUniformLocation(programId, "texture")
  def baseColorLocation: Int = gl.glGetUniformLocation(programId, "baseColor")

  protected def createShaderProgram(): Int = {
    val shaderProgram = gl.glCreateProgram()
    val vertexShaderHolder = shaderCompiler.compile(GL_VERTEX_SHADER, vertexShaderSource)
    val fragmentShaderHolder = shaderCompiler.compile(GL_FRAGMENT_SHADER, fragmentShaderSource)

    vertexShaderHolder.foreach(shaderId => gl.glAttachShader(shaderProgram, shaderId))
    fragmentShaderHolder.foreach(shaderId => gl.glAttachShader(shaderProgram, shaderId))

    val linkedProgram = shaderCompiler.link(shaderProgram)

    vertexShaderHolder.foreach(shaderId => detachAndDeleteShader(shaderProgram, shaderId))
    fragmentShaderHolder.foreach(shaderId => detachAndDeleteShader(shaderProgram, shaderId))

    vertexShaderHolder.failed.foreach(throw _)
    fragmentShaderHolder.failed.foreach(throw _)
    linkedProgram.failed.foreach(throw _)

    shaderCleaner.register(this, gl)

    shaderProgram
  }

  private def detachAndDeleteShader(programId: Int, shaderId: Int): Unit = {
    gl.glDetachShader(programId, shaderId)
    gl.glDeleteShader(shaderId)
  }

}
