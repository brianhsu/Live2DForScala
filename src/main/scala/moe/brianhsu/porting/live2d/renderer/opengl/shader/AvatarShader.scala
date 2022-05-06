package moe.brianhsu.porting.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.shader.Shader

abstract class AvatarShader(implicit gl: OpenGLBinding) extends Shader(gl) {
  val attributePositionLocation: Int = gl.glGetAttribLocation(programId, "a_position")
  val attributeTexCoordLocation: Int = gl.glGetAttribLocation(programId, "a_texCoord")
  val samplerTexture0Location: Int = gl.glGetUniformLocation(programId, "s_texture0")
  val samplerTexture1Location: Int = gl.glGetUniformLocation(programId, "s_texture1")
  val uniformClipMatrixLocation: Int = gl.glGetUniformLocation(programId, "u_clipMatrix")
  val uniformChannelFlagLocation: Int = gl.glGetUniformLocation(programId, "u_channelFlag")
  val uniformBaseColorLocation: Int = gl.glGetUniformLocation(programId, "u_baseColor")
  val uniformMatrixLocation: Int = gl.glGetUniformLocation(programId, "u_matrix")
}
