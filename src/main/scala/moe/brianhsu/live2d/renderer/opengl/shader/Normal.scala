package moe.brianhsu.live2d.renderer.opengl.shader

import com.jogamp.opengl.GL2

class Normal(implicit gl: GL2) extends AvatarShader {
  override def vertexShaderSource: String = {
    "#version 120\n" +
    "attribute vec4 a_position;" +
    "attribute vec2 a_texCoord;" +
    "varying vec2 v_texCoord;" +
    "uniform mat4 u_matrix;" +
    "void main()" +
    "{" +
    "gl_Position = u_matrix * a_position;" +
    "v_texCoord = a_texCoord;" +
    "v_texCoord.y = 1.0 - v_texCoord.y;" +
    "}"

  }

  override def fragmentShaderSource: String = {
    "#version 120\n" +
    "varying vec2 v_texCoord;" + //v2f.texcoord
    "uniform sampler2D s_texture0;" + //_MainTex
    "uniform vec4 u_baseColor;" + //v2f.color
    "void main()" +
    "{" +
    "vec4 color = texture2D(s_texture0 , v_texCoord) * u_baseColor;" +
    "gl_FragColor = vec4(color.rgb * color.a,  color.a);" +
    "}"
  }
}
