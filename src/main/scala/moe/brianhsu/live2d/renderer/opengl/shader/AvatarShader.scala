package moe.brianhsu.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.adapter.OpenGL

abstract class AvatarShader(implicit gl: OpenGL) extends BaseShader[AvatarShader] {
  val attributePositionLocation: Int = gl.glGetAttribLocation(shaderProgram, "a_position")
  val attributeTexCoordLocation: Int = gl.glGetAttribLocation(shaderProgram, "a_texCoord")
  val samplerTexture0Location: Int = gl.glGetUniformLocation(shaderProgram, "s_texture0")
  val samplerTexture1Location: Int = gl.glGetUniformLocation(shaderProgram, "s_texture1")
  val uniformClipMatrixLocation: Int = gl.glGetUniformLocation(shaderProgram, "u_clipMatrix")
  val uniformChannelFlagLocation: Int = gl.glGetUniformLocation(shaderProgram, "u_channelFlag")
  val uniformBaseColorLocation: Int = gl.glGetUniformLocation(shaderProgram, "u_baseColor")
  val uniformMatrixLocation: Int = gl.glGetUniformLocation(shaderProgram, "u_matrix")
}
