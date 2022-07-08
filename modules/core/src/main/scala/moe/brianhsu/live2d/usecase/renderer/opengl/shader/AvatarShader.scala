package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.shader.Shader

abstract class AvatarShader(implicit gl: OpenGLBinding) extends Shader(gl) {
  def attributePositionLocation: Int = gl.glGetAttribLocation(programId, "a_position")
  def attributeTexCoordLocation: Int = gl.glGetAttribLocation(programId, "a_texCoord")
  def samplerTexture0Location: Int = gl.glGetUniformLocation(programId, "s_texture0")
  def samplerTexture1Location: Int = gl.glGetUniformLocation(programId, "s_texture1")
  def uniformClipMatrixLocation: Int = gl.glGetUniformLocation(programId, "u_clipMatrix")
  def uniformChannelFlagLocation: Int = gl.glGetUniformLocation(programId, "u_channelFlag")
  def uniformBaseColorLocation: Int = gl.glGetUniformLocation(programId, "u_baseColor")
  def uniformMatrixLocation: Int = gl.glGetUniformLocation(programId, "u_matrix")
  def uniformMultiplyColorLocation: Int = gl.glGetUniformLocation(programId, "u_multiplyColor")
  def uniformScreenColorLocation: Int = gl.glGetUniformLocation(programId, "u_screenColor")
}
