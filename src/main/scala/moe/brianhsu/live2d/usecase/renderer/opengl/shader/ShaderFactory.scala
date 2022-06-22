package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding

object ShaderFactory {
  class DefaultShaderFactory(implicit gl: OpenGLBinding) extends ShaderFactory {
    override def setupMaskShader: SetupMaskShader = new SetupMaskShader
    override def normalShader: NormalShader = new NormalShader
    override def maskedShader: MaskedShader = new MaskedShader
    override def invertedMaskedShader: InvertedMaskedShader = new InvertedMaskedShader
  }
}

trait ShaderFactory {
  def setupMaskShader: SetupMaskShader
  def normalShader: NormalShader
  def maskedShader: MaskedShader
  def invertedMaskedShader: InvertedMaskedShader
}
