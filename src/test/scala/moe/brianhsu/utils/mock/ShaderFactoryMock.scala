package moe.brianhsu.utils.mock

import moe.brianhsu.live2d.usecase.renderer.opengl.shader.{InvertedMaskedShader, MaskedShader, NormalShader, SetupMaskShader, ShaderFactory}

trait ShaderFactoryMock {
  protected val stubbedSetupMaskShader: SetupMaskShader = createSetupShader()
  protected val stubbedMaskShader: MaskedShader = createMaskShader()
  protected val stubbedInvertedMaskShader: InvertedMaskedShader = createInvertedMaskShader()
  protected val stubbedNormalShader: NormalShader = createNormalShader()

  def createShaderFactory(): ShaderFactory = {

    new ShaderFactory {
      override def setupMaskShader: SetupMaskShader = stubbedSetupMaskShader
      override def normalShader: NormalShader = stubbedNormalShader
      override def maskedShader: MaskedShader = stubbedMaskShader
      override def invertedMaskedShader: InvertedMaskedShader = stubbedInvertedMaskShader
    }
  }

  protected def createSetupShader(): SetupMaskShader = {
    new SetupMaskShader()(null) {
      override val programId: Int = 1000
      override val samplerTexture0Location: Int = 1001
      override val samplerTexture1Location: Int = 1002
      override val attributePositionLocation: Int = 1003
      override val attributeTexCoordLocation: Int = 1004
      override val uniformChannelFlagLocation: Int = 1005
      override val uniformClipMatrixLocation: Int = 1006
      override val uniformBaseColorLocation: Int = 1007
      override val uniformMatrixLocation: Int = 1008

      override def createShaderProgram(): Int = 1000
    }
  }

  protected def createNormalShader(): NormalShader = {
    new NormalShader()(null) {
      override val programId: Int = 2000
      override val samplerTexture0Location: Int = 2001
      override val samplerTexture1Location: Int = 2002
      override val attributePositionLocation: Int = 2003
      override val attributeTexCoordLocation: Int = 2004
      override val uniformChannelFlagLocation: Int = 2005
      override val uniformClipMatrixLocation: Int = 2006
      override val uniformBaseColorLocation: Int = 2007
      override val uniformMatrixLocation: Int = 2008

      override def createShaderProgram(): Int = 2000

    }
  }

  protected def createMaskShader(): MaskedShader = {
    new MaskedShader()(null) {
      override val programId: Int = 3000
      override val samplerTexture0Location: Int = 3001
      override val samplerTexture1Location: Int = 3002
      override val attributePositionLocation: Int = 3003
      override val attributeTexCoordLocation: Int = 3004
      override val uniformChannelFlagLocation: Int = 3005
      override val uniformClipMatrixLocation: Int = 3006
      override val uniformBaseColorLocation: Int = 3007
      override val uniformMatrixLocation: Int = 3008

      override def createShaderProgram(): Int = 3009
    }
  }

  protected def createInvertedMaskShader(): InvertedMaskedShader = {
    new InvertedMaskedShader()(null) {
      override val programId: Int = 4000
      override val samplerTexture0Location: Int = 4001
      override val samplerTexture1Location: Int = 4002
      override val attributePositionLocation: Int = 4003
      override val attributeTexCoordLocation: Int = 4004
      override val uniformChannelFlagLocation: Int = 4005
      override val uniformClipMatrixLocation: Int = 4006
      override val uniformBaseColorLocation: Int = 4007
      override val uniformMatrixLocation: Int = 4008

      override def createShaderProgram(): Int = 4000
    }
  }

}
