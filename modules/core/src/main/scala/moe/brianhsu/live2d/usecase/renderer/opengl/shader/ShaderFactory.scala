package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

object ShaderFactory {
  class DefaultShaderFactory(implicit gl: OpenGLBinding) extends ShaderFactory {
    override lazy val normalShader: NormalShader = new NormalShader
    override lazy val setupMaskShader: SetupMaskShader = new SetupMaskShader
    override lazy val maskedShader: MaskedShader = new MaskedShader
    override lazy val invertedMaskedShader: InvertedMaskedShader = new InvertedMaskedShader
  }
}

trait ShaderFactory {
  def setupMaskShader: SetupMaskShader
  def normalShader: NormalShader
  def maskedShader: MaskedShader
  def invertedMaskedShader: InvertedMaskedShader
}
