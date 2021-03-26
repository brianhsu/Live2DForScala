package moe.brianhsu.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.adapter.OpenGL

class SetupMask(implicit gl: OpenGL) extends AvatarShader {
  override def vertexShaderSource: String = {
    "#version 120\n" +
      "attribute vec4 a_position;" +
      "attribute vec2 a_texCoord;" +
      "varying vec2 v_texCoord;" +
      "varying vec4 v_myPos;" +
      "uniform mat4 u_clipMatrix;" +
      "void main()" +
      "{" +
      "gl_Position = u_clipMatrix * a_position;" +
      "v_myPos = u_clipMatrix * a_position;" +
      "v_texCoord = a_texCoord;" +
      "v_texCoord.y = 1.0 - v_texCoord.y;" +
      "}"
  }

  override def fragmentShaderSource: String = {
    "#version 120\n" +
      "varying vec2 v_texCoord;" +
      "varying vec4 v_myPos;" +
      "uniform sampler2D s_texture0;" +
      "uniform vec4 u_channelFlag;" +
      "uniform vec4 u_baseColor;" +
      "void main()" +
      "{" +
      "float isInside = " +
      "  step(u_baseColor.x, v_myPos.x/v_myPos.w)" +
      "* step(u_baseColor.y, v_myPos.y/v_myPos.w)" +
      "* step(v_myPos.x/v_myPos.w, u_baseColor.z)" +
      "* step(v_myPos.y/v_myPos.w, u_baseColor.w);" +
      "gl_FragColor = u_channelFlag * texture2D(s_texture0 , v_texCoord).a * isInside;" +
      "}"
  }
}
