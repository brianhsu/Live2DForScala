package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureColor
import moe.brianhsu.live2d.enitiy.opengl.{BlendFunction, OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.OffscreenFrame
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext.Layout
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import java.nio.ByteBuffer

class ShaderRenderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                          with OpenGLMock with TableDrivenPropertyChecks with ShaderRenderBehaviors {

  Feature("Singleton by OpenGL binding") {
    Scenario("Get instance with same OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val binding = createOpenGLStub()

      When("create a ShaderRender from that binding")
      val thisRenderer = ShaderRenderer.getInstance(binding)

      And("create another ShaderRender from that binding")
      val thatRenderer = ShaderRenderer.getInstance(binding)

      Then("this two profile should be same object instance")
      thisRenderer should be theSameInstanceAs thatRenderer

      And("should able to renderMask which has implicit conversion to RichOpenGLBinding")
      thisRenderer.renderMask(new ClippingContext(Nil, Nil), 0, null, null)
    }

    Scenario("Get instance with different OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val thisBinding = createOpenGLStub()

      When("create a ShaderRender from that binding")
      val thisRenderer = ShaderRenderer.getInstance(thisBinding)

      And("create another ShaderRender from another binding")
      val thatBinding = createOpenGLStub()
      val thatRenderer = ShaderRenderer.getInstance(thatBinding)

      Then("this two profile should be same object instance")
      thisRenderer should not be theSameInstanceAs (thatRenderer)
    }
  }

  Feature("Render mask") {
    Scenario("Render mask using ShaderRender") {
      Given("the following stubbed ShaderFactory")
      implicit val openGLBinding: OpenGLBinding = createOpenGLStub()

      import openGLBinding.constants._

      val stubbedTextureId = 456
      val stubbedSetupMaskShader = createSetupShader()

      val shaderFactory = stub[ShaderFactory]
      (() => shaderFactory.setupMaskShader).when().returns(stubbedSetupMaskShader)

      And("the following OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGLBinding = stub[RichOpenGLBinding]

      And("create a ShaderRenderer with above objects")
      val shaderRender = new ShaderRenderer(shaderFactory)(binding, {_: OpenGLBinding => richOpenGLBinding})

      And("the following clipping context / vertex array / uv array")
      val vertexArray = ByteBuffer.allocate(1)
      val uvArray = ByteBuffer.allocate(1)
      val matrixForMask = new GeneralMatrix(Array(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f))
      val stubbedLayout = Layout(1, Rectangle(1.2f, 2.3f, 1024.0f, 768.0f))
      val clippingContext = ClippingContext(
        Nil, Nil,
        layout = stubbedLayout,
        matrixForMask = matrixForMask
      )

      When("render mask")
      shaderRender.renderMask(clippingContext, stubbedTextureId, vertexArray, uvArray)

      Then("it should delegate it to underlay OpenGL binding")
      inSequence {
        (binding.glUseProgram _).verify(stubbedSetupMaskShader.programId).once()
        (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE0, stubbedTextureId, stubbedSetupMaskShader.samplerTexture0Location, 0).once()
        (richOpenGLBinding.updateVertexInfo _).verify(vertexArray, uvArray, stubbedSetupMaskShader.attributePositionLocation, stubbedSetupMaskShader.attributeTexCoordLocation)
        (richOpenGLBinding.setColorChannel _).verify(stubbedLayout.channelColor, stubbedSetupMaskShader.uniformChannelFlagLocation).once()
        (binding.glUniformMatrix4fv _).verify(stubbedSetupMaskShader.uniformClipMatrixLocation, 1, false, matrixForMask.elements).once()
        (binding.glUniform4f _).verify(stubbedSetupMaskShader.uniformBaseColorLocation, 1.4000001f, 3.6f, 2049.4f, 1539.6f).once()
        (richOpenGLBinding.blendFunction_= _).verify(BlendFunction(GL_ZERO, GL_ONE_MINUS_SRC_COLOR, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA)).once()
      }

    }
  }

  Feature("Render drawables") {
    val constant = createOpenGLStub().constants
    import constant._
    val testDataWithoutClippingContext = Table(
      ("isInvertedMask", "colorBlendMode", "expectedBlendFunction"),
      (false, ConstantFlags.Normal, BlendFunction(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)),
      (false, ConstantFlags.AdditiveBlend, BlendFunction(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)),
      (false, ConstantFlags.MultiplicativeBlend, BlendFunction(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)),
      (true, ConstantFlags.Normal, BlendFunction(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)),
      (true, ConstantFlags.AdditiveBlend, BlendFunction(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)),
      (true, ConstantFlags.MultiplicativeBlend, BlendFunction(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)),
    )

    forAll(testDataWithoutClippingContext) { case (isInvertedMask, colorBlendMode, expectedBlendFunction) =>
      withExpectations {
        ScenariosFor(
          noClippingContext(isInvertedMask, colorBlendMode, expectedBlendFunction)
        )
      }
    }

    val testDataWithClippingContext = Table(
      ("isInvertedMask", "expectedShader", "colorBlendMode", "expectedBlendFunction"),
      (false, stubbedMaskShader, ConstantFlags.Normal, BlendFunction(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)),
      (false, stubbedMaskShader, ConstantFlags.AdditiveBlend, BlendFunction(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)),
      (false, stubbedMaskShader, ConstantFlags.MultiplicativeBlend, BlendFunction(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)),
      (true, stubbedInvertedMaskShader, ConstantFlags.Normal, BlendFunction(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)),
      (true, stubbedInvertedMaskShader, ConstantFlags.AdditiveBlend, BlendFunction(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)),
      (true, stubbedInvertedMaskShader, ConstantFlags.MultiplicativeBlend, BlendFunction(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)),
    )

    forAll(testDataWithClippingContext) { case (isInvertedMask, expectedShader, colorBlendMode, expectedBlendFunction) =>
      withExpectations {
        ScenariosFor(
          hasClippingContextButNoOffscreenFrame(
            isInvertedMask, expectedShader,
            colorBlendMode, expectedBlendFunction
          )
        )
      }
    }

    forAll(testDataWithClippingContext) { case (isInvertedMask, expectedShader, colorBlendMode, expectedBlendFunction) =>
      withExpectations {
        ScenariosFor(
          hasClippingContextAndOffscreenFrame(
            isInvertedMask, expectedShader,
            colorBlendMode, expectedBlendFunction
          )
        )
      }
    }
  }
}

trait ShaderRenderBehaviors {
  this: AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock with GivenWhenThen =>
  protected val stubbedTextureId = 456
  protected val stubbedSetupMaskShader: SetupMaskShader = createSetupShader()
  protected val stubbedMaskShader: MaskedShader = createMaskShader()
  protected val stubbedInvertedMaskShader: InvertedMaskedShader = createInvertedMaskShader()
  protected val stubbedNormalShader: NormalShader = createNormalShader()
  protected val stubbedVertex: ByteBuffer = ByteBuffer.allocate(1)
  protected val stubbedUv: ByteBuffer = ByteBuffer.allocate(1)
  protected val stubbedProjection = new ProjectionMatrix(
    Array(
      0.1f, 0.2f, 0.3f, 0.4f,
      0.5f, 0.6f, 0.7f, 0.8f,
      0.9f, 1.0f, 1.1f, 1.2f,
      1.3f, 1.4f, 1.5f, 1.6f
    )
  )
  protected val stubbedBaseColor: TextureColor = TextureColor(0.1f, 0.2f, 0.3f, 0.4f)
  protected val shaderFactory: ShaderFactory = new ShaderFactory {
    override def setupMaskShader: SetupMaskShader = stubbedSetupMaskShader
    override def normalShader: NormalShader = stubbedNormalShader
    override def maskedShader: MaskedShader = stubbedMaskShader
    override def invertedMaskedShader: InvertedMaskedShader = stubbedInvertedMaskShader
  }
  protected val stubbedMatrixForMask = new GeneralMatrix(Array(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f))
  protected val stubbedMatrixForDraw = new GeneralMatrix(Array(10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f, 100.0f, 110.0f, 120.0f, 130.0f, 140.0f, 150.0f, 160.0f))

  protected val stubbedLayout: Layout = Layout(1, Rectangle(1.2f, 2.3f, 1024.0f, 768.0f))
  protected val stubbedClippingContext: ClippingContext = ClippingContext(
    Nil, Nil,
    layout = stubbedLayout,
    matrixForMask = stubbedMatrixForMask,
    matrixForDraw = stubbedMatrixForDraw
  )

  def noClippingContext(isInvertedMask: Boolean, colorBlendMode: ConstantFlags.BlendMode, expectedBlendFunction: BlendFunction): Unit = {
    Given("a ShaderRenderer")
    val binding = createOpenGLStub()
    val richOpenGLBinding = stub[RichOpenGLBinding]
    val shaderRender = new ShaderRenderer(shaderFactory)(binding, { _: OpenGLBinding => richOpenGLBinding })

    import binding.constants._

    When(s"render mask with isInvertedMask = $isInvertedMask, colorBlendMode = $colorBlendMode")
    shaderRender.renderDrawable(
      None, None, stubbedTextureId,
      stubbedVertex, stubbedUv, colorBlendMode,
      stubbedBaseColor,
      stubbedProjection,
      isInvertedMask
    )

    Then("it should delegate call to underlay OpenGL binding")
    inSequence {
      (binding.glUseProgram _).verify(stubbedNormalShader.programId).once()
      (richOpenGLBinding.updateVertexInfo _).verify(stubbedVertex, stubbedUv, stubbedNormalShader.attributePositionLocation, stubbedNormalShader.attributeTexCoordLocation).once()

      // Shouldn't do anything related to clipping context / offscreen frame
      (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE1, *, *, *).never()
      (binding.glUniformMatrix4fv _).verify(stubbedNormalShader.uniformClipMatrixLocation, *, *, *).never()
      (richOpenGLBinding.setColorChannel _).verify(*, *).never()

      (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE0, stubbedTextureId, stubbedNormalShader.samplerTexture0Location, 0).once()
      (binding.glUniformMatrix4fv _).verify(stubbedNormalShader.uniformMatrixLocation, 1, false, stubbedProjection.elements).once()
      (binding.glUniform4f _).verify(stubbedNormalShader.uniformBaseColorLocation, stubbedBaseColor.red, stubbedBaseColor.green, stubbedBaseColor.blue, stubbedBaseColor.alpha).once()
      (richOpenGLBinding.blendFunction_= _).verify(expectedBlendFunction).once()

    }
  }

  def hasClippingContextButNoOffscreenFrame(isInvertedMask: Boolean, expectedShader: AvatarShader, colorBlendMode: ConstantFlags.BlendMode, expectedBlendFunction: BlendFunction): Unit = {
    Given("a ShaderRenderer")
    val binding = createOpenGLStub()
    val richOpenGLBinding = stub[RichOpenGLBinding]
    val shaderRender = new ShaderRenderer(shaderFactory)(binding, { _: OpenGLBinding => richOpenGLBinding })

    import binding.constants._

    When(s"render mask with isInvertedMask = $isInvertedMask, colorBlendMode = $colorBlendMode")
    shaderRender.renderDrawable(
      Some(stubbedClippingContext), None, stubbedTextureId,
      stubbedVertex, stubbedUv, colorBlendMode,
      stubbedBaseColor,
      stubbedProjection,
      isInvertedMask
    )

    Then("it should delegate call to underlay OpenGL binding")
    inSequence {
      (binding.glUseProgram _).verify(expectedShader.programId).once()
      (richOpenGLBinding.updateVertexInfo _).verify(stubbedVertex, stubbedUv, expectedShader.attributePositionLocation, expectedShader.attributeTexCoordLocation).once()

      // Shouldn't do anything related to clipping context / offscreen frame
      (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE1, *, *, *).never()
      (binding.glUniformMatrix4fv _).verify(expectedShader.uniformClipMatrixLocation, *, *, *).never()
      (richOpenGLBinding.setColorChannel _).verify(*, *).never()

      (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE0, stubbedTextureId, expectedShader.samplerTexture0Location, 0).once()
      (binding.glUniformMatrix4fv _).verify(expectedShader.uniformMatrixLocation, 1, false, stubbedProjection.elements).once()
      (binding.glUniform4f _).verify(expectedShader.uniformBaseColorLocation, stubbedBaseColor.red, stubbedBaseColor.green, stubbedBaseColor.blue, stubbedBaseColor.alpha).once()
      (richOpenGLBinding.blendFunction_= _).verify(expectedBlendFunction).once()

    }
  }

  def hasClippingContextAndOffscreenFrame(isInvertedMask: Boolean, expectedShader: AvatarShader, colorBlendMode: ConstantFlags.BlendMode, expectedBlendFunction: BlendFunction): Unit = {
    Given("a ShaderRenderer")
    val binding = createOpenGLStub()
    val richOpenGLBinding = stub[RichOpenGLBinding]
    val shaderRender = new ShaderRenderer(shaderFactory)(binding, { _: OpenGLBinding => richOpenGLBinding })
    import binding.constants._

    When(s"render mask with isInvertedMask = $isInvertedMask, colorBlendMode = $colorBlendMode")
    val stubbedFrameBufferId = 5678
    val stubbedOffscreenFrame = createOffscreenFrame(stubbedFrameBufferId)
    shaderRender.renderDrawable(
      Some(stubbedClippingContext),
      Some(stubbedOffscreenFrame),
      stubbedTextureId,
      stubbedVertex, stubbedUv, colorBlendMode,
      stubbedBaseColor,
      stubbedProjection,
      isInvertedMask
    )

    Then("it should delegate call to underlay OpenGL binding")
    inSequence {
      (binding.glUseProgram _).verify(expectedShader.programId).once()
      (richOpenGLBinding.updateVertexInfo _).verify(stubbedVertex, stubbedUv, expectedShader.attributePositionLocation, expectedShader.attributeTexCoordLocation).once()

      // Shouldn't do anything related to clipping context / offscreen frame
      (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE1, stubbedFrameBufferId, expectedShader.samplerTexture1Location, 1).once()
      (binding.glUniformMatrix4fv _).verify(expectedShader.uniformClipMatrixLocation, 1, false, stubbedMatrixForDraw.elements).once()
      (richOpenGLBinding.setColorChannel _).verify(stubbedLayout.channelColor, expectedShader.uniformChannelFlagLocation).once()

      (richOpenGLBinding.activeAndUpdateTextureVariable _).verify(GL_TEXTURE0, stubbedTextureId, expectedShader.samplerTexture0Location, 0).once()
      (binding.glUniformMatrix4fv _).verify(expectedShader.uniformMatrixLocation, 1, false, stubbedProjection.elements).once()
      (binding.glUniform4f _).verify(expectedShader.uniformBaseColorLocation, stubbedBaseColor.red, stubbedBaseColor.green, stubbedBaseColor.blue, stubbedBaseColor.alpha).once()
      (richOpenGLBinding.blendFunction_= _).verify(expectedBlendFunction).once()
    }
  }

  private def createOffscreenFrame(stubbedFrameBufferId: Int): OffscreenFrame = {
    new OffscreenFrame(1024, 768)(null, null) {
      override def createColorTextureBufferAndFrameBuffer(): (Int, Int) = (1234, stubbedFrameBufferId)
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

