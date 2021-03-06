package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

class NormalShader(implicit gl: OpenGLBinding) extends AvatarShader {
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
       |uniform vec4 u_multiplyColor;
       |uniform vec4 u_screenColor;
       |void main() {
       |  vec4 texColor = texture2D(s_texture0 , v_texCoord);
       |  texColor.rgb = texColor.rgb * u_multiplyColor.rgb;
       |  texColor.rgb = texColor.rgb + u_screenColor.rgb - (texColor.rgb * u_screenColor.rgb);
       |  vec4 color = texColor * u_baseColor;
       |  gl_FragColor = vec4(color.rgb * color.a,  color.a);
       |}
       |""".stripMargin
}
