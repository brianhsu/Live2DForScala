package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.shader.Shader

class SpriteShader(implicit gl: OpenGLBinding) extends Shader(gl) {
  override def vertexShaderSource: String =
    """
      |#version 120
      |attribute vec3 position;
      |attribute vec2 uv;
      |varying vec2 vuv;
      |void main() {
      |   gl_Position = vec4(position, 1.0);
      |   vuv = uv;
      |}
      |""".stripMargin.trim

  override def fragmentShaderSource: String =
    """
      |#version 120
      |varying vec2 vuv;
      |uniform sampler2D texture;
      |uniform vec4 baseColor;
      |void main() {
      |   gl_FragColor = texture2D(texture, vuv) * baseColor;
      |}
      |""".stripMargin.trim
}
