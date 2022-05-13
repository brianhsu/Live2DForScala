package moe.brianhsu.porting.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.shader.AvatarShader

class Normal(implicit gl: OpenGLBinding) extends AvatarShader {
  override def vertexShaderSource: String =
    """|#version 120
       |
       |attribute vec4 a_position;
       |attribute vec2 a_texCoord;
       |
       |varying vec2 v_texCoord;
       |uniform mat4 u_matrix;
       |
       |void main() {
       |  gl_Position = u_matrix * a_position;
       |  v_texCoord = a_texCoord;
       |  v_texCoord.y = 1.0 - v_texCoord.y;
       |}
       |""".stripMargin

  override def fragmentShaderSource: String =
    """|#version 120
       |
       |varying vec2 v_texCoord;
       |
       |uniform sampler2D s_texture0;
       |uniform vec4 u_baseColor;
       |
       |void main() {
       |  vec4 color = texture2D(s_texture0 , v_texCoord) * u_baseColor;
       |  gl_FragColor = vec4(color.rgb * color.a,  color.a);
       |}
       |""".stripMargin
}
