package moe.brianhsu.live2d.enitiy.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.shader.ShaderCleaner.ShaderDeleter

import java.lang.ref.Cleaner

object ShaderCleaner {
  class ShaderDeleter(shaderProgramId: Int, gl: OpenGLBinding) extends Runnable {
    override def run(): Unit = {
      gl.glDeleteProgram(shaderProgramId)
    }
  }
}

class ShaderCleaner {

  private val cleaner = Cleaner.create()

  def register(shader: Shader, gl: OpenGLBinding): Unit = {
    cleaner.register(shader, new ShaderDeleter(shader.programId, gl))
  }

}
