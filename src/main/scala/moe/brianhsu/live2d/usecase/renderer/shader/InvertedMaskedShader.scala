package moe.brianhsu.live2d.usecase.renderer.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

class InvertedMaskedShader(implicit gl: OpenGLBinding) extends AvatarShader {
  override def vertexShaderSource: String =
    """|#version 120
       |
       |attribute vec4 a_position;
       |attribute vec2 a_texCoord;
       |
       |varying vec2 v_texCoord;
       |varying vec4 v_clipPos;
       |
       |uniform mat4 u_matrix;
       |uniform mat4 u_clipMatrix;
       |
       |void main() {
       |  gl_Position = u_matrix * a_position;
       |  v_clipPos = u_clipMatrix * a_position;
       |  v_texCoord = a_texCoord;
       |  v_texCoord.y = 1.0 - v_texCoord.y;
       |}
       |""".stripMargin

  override def fragmentShaderSource: String =
    """|#version 120
       |
       |varying vec2 v_texCoord;
       |varying vec4 v_clipPos;
       |
       |uniform sampler2D s_texture0;
       |uniform sampler2D s_texture1;
       |
       |uniform vec4 u_channelFlag;
       |uniform vec4 u_baseColor;
       |
       |void main() {
       |  vec4 col_formask = texture2D(s_texture0 , v_texCoord) * u_baseColor;
       |  col_formask.rgb = col_formask.rgb  * col_formask.a;
       |  vec4 clipMask = (1.0 - texture2D(s_texture1, v_clipPos.xy / v_clipPos.w)) * u_channelFlag;
       |  float maskVal = clipMask.r + clipMask.g + clipMask.b + clipMask.a;
       |  col_formask = col_formask * (1.0 - maskVal);
       |  gl_FragColor = col_formask;
       |}
       |""".stripMargin
}
