package moe.brianhsu.live2d.usecase.renderer.opengl

import com.sun.jna.Memory
import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI.ConstantDrawableFlagMask
import moe.brianhsu.live2d.enitiy.math.matrix.ModelMatrix
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}
import moe.brianhsu.live2d.enitiy.model.drawable.{ConstantFlags, Drawable, DynamicFlags, VertexInfo}
import moe.brianhsu.live2d.enitiy.opengl.texture.{TextureColor, TextureInfo, TextureManager}
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.{ClippingContext, ClippingRenderer}
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.nio.ByteBuffer


class AvatarRendererFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock {
  private val StubbedModelMatrix = ModelMatrix(
    1.0f, 1.875f,
    Array(
      0.79466695f, 0.0f, 0.0f, 0.0f,
      0.0f, 0.79466695f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      -0.16200002f, -0.274f, 0.0f, 1.0f
    )
  )

  private val StubbedProjectionMatrix = new ProjectionMatrix(
    Array(
      0.49418604f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      0.0f, 0.0f, 0.0f, 1.0f
    )
  )

  private val ExpectedRendererProjection = new ProjectionMatrix(
    Array(
      0.3927133f, 0.0f, 0.0f, 0.0f,
      0.0f, 0.79466695f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      -0.08005815f, -0.274f, 0.0f, 1.0f
    )
  )

  Feature("AvatarRenderer factory object") {
    Scenario("Create AvatarRenderer only from Live2D model and implicit OpenGL binding") {
      Given("stubbed OpenGL / RichOpenGL binding")
      val richOpenGLBinding = stub[RichOpenGLBinding]
      implicit val binding: OpenGLBinding = createOpenGLStub()
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding }

      And("a Live2D model without any drawables")
      val modelBackend = stub[ModelBackend]
      (() => modelBackend.drawables).when().returns(Map.empty)
      val live2DModel = new Live2DModel(modelBackend)

      When("create a AvatarRenderer from them")
      val avatarRenderer = AvatarRenderer(live2DModel)

      Then("it should be an AvatarRenderer")
      avatarRenderer shouldBe an[AvatarRenderer]
    }
  }

  Feature("Draw Live2D avatar model") {
    Scenario("There is no drawables inside Live2D model at all") {

      Given("mocked OpenGL / RichOpenGL binding")
      val richOpenGLBinding = stub[RichOpenGLBinding]
      implicit val binding: OpenGLBinding = createOpenGLStub()
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding }

      And("a Live2D model without any drawables")
      val live2DModel = createStubbedLive2DModel(drawable = Nil)

      And("create a AvatarRenderer from that model")
      val textureManager = mock[TextureManager]
      val shaderRenderer = mock[ShaderRenderer]
      val profile = stub[Profile]
      val clippingRenderer = stub[ClippingRenderer]
      val avatarRenderer = new AvatarRenderer(live2DModel, textureManager, shaderRenderer, profile, clippingRenderer)

      When("draw the model")
      avatarRenderer.draw(StubbedProjectionMatrix)

      Then("it should delegate it to underlay dependency")
      inSequence {
        (() => profile.save()).verify().once()
        (clippingRenderer.draw _).verify(profile).once()
        (() => richOpenGLBinding.preDraw()).verify().once()
        (() => profile.restore()).verify().once()
      }
    }

    Scenario("Only draw model that is visible") {
      Given("some visible and non visible drawables")
      val drawables = List(
        createDrawable("id0", 0, 0.1f, isCulling = true, AdditiveBlend, isInvertedMask = true, isVisible = false),
        createDrawable("id1", 1, 0.2f, isCulling = true, MultiplicativeBlend, isInvertedMask = false, isVisible = true),
        createDrawable("id2", 2, 0.3f, isCulling = false, AdditiveBlend, isInvertedMask = true, isVisible = true),
        createDrawable("id3", 3, 0.4f, isCulling = true, AdditiveBlend, isInvertedMask = true, isVisible = false),

      )
      val idToDrawable = drawables.map(drawable => drawable.id -> drawable).toMap

      And("mocked OpenGL / RichOpenGL binding")
      val richOpenGLBinding = stub[RichOpenGLBinding]
      implicit val binding: OpenGLBinding = createOpenGLStub()
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding }

      And("a Live2D model / textureManager handle with above drawables")
      val live2DModel = createStubbedLive2DModel(drawables)
      val textureManager = stub[TextureManager]
      (() => live2DModel.textureFiles).when().returns(List("0.png", "1.png", "2.png", "3.png"))
      (textureManager.loadTexture _).when("1.png").returns(TextureInfo(123, 456, 789))
      (textureManager.loadTexture _).when("2.png").returns(TextureInfo(789, 345, 678))

      And("corresponding ClippingRenderer")
      val stubbedClippingContext1Holder = None
      val stubbedClippingContext2Holder = Some(new ClippingContext(Nil, Nil))
      val clippingRenderer = stub[ClippingRenderer]
      (clippingRenderer.clippingContextBufferForDraw _).when(idToDrawable("id1")).returns(stubbedClippingContext1Holder)
      (clippingRenderer.clippingContextBufferForDraw _).when(idToDrawable("id2")).returns(stubbedClippingContext2Holder)

      And("create a AvatarRenderer from that model")
      val shaderRenderer = stub[ShaderRenderer]
      val profile = stub[Profile]

      val avatarRenderer = new AvatarRenderer(live2DModel, textureManager, shaderRenderer, profile, clippingRenderer)

      When("draw the model")
      avatarRenderer.draw(StubbedProjectionMatrix)
      Then("it should delegate it to underlay dependency")

      inSequence {
        import binding.constants._
        (() => profile.save()).verify().once()
        (clippingRenderer.draw _).verify(profile).once()
        (() => richOpenGLBinding.preDraw()).verify().once()

        // First visible drawable
        inSequence {
          (binding.setCapabilityEnabled _).verify(GL_CULL_FACE, true).once()
          (binding.glFrontFace _).verify(GL_CCW).once()
          (shaderRenderer.renderDrawable _)
            .verify(
              stubbedClippingContext1Holder, *, 123, *, *,
              MultiplicativeBlend,
              TextureColor(1.0f, 1.0f, 1.0f, 0.2f),
              ExpectedRendererProjection,
              false
            )
            .once()
          (binding.glDrawElements _).verify(GL_TRIANGLES, 1, GL_UNSIGNED_SHORT, *).once()
          (binding.glUseProgram _).verify(0).once()

        }

        // Second visible drawable
        inSequence {
          (binding.setCapabilityEnabled _).verify(GL_CULL_FACE, false).once()
          (binding.glFrontFace _).verify(GL_CCW).once()
          (shaderRenderer.renderDrawable _)
            .verify(
              stubbedClippingContext2Holder, *, 789, *, *,
              AdditiveBlend,
              TextureColor(1.0f, 1.0f, 1.0f, 0.3f),
              ExpectedRendererProjection,
              true
            )
            .once()

          (binding.glDrawElements _).verify(GL_TRIANGLES, 2, GL_UNSIGNED_SHORT, *).once()
          (binding.glUseProgram _).verify(0).once()
        }

        (() => profile.restore()).verify().once()
      }

    }
  }

  private def createDrawable(id: String, index:Int, opacity: Float, isCulling: Boolean, blendMode: BlendMode, isInvertedMask: Boolean, isVisible: Boolean): Drawable = {
    val vertexInfo = new VertexInfo(index, index, null, null, null) {
      override def vertexArrayDirectBuffer: ByteBuffer = ByteBuffer.allocate(1)
      override def uvArrayDirectBuffer: ByteBuffer = ByteBuffer.allocate(1)
      override def indexArrayDirectBuffer: ByteBuffer = ByteBuffer.allocate(1)
    }
    val blendModeBit: Byte = blendMode match {
      case Normal => 0.toByte
      case AdditiveBlend => ConstantDrawableFlagMask.csmBlendAdditiveBit
      case MultiplicativeBlend => ConstantDrawableFlagMask.csmBlendMultiplicative
    }
    val isCullingBit: Byte = if (!isCulling) ConstantDrawableFlagMask.csmIsDoubleSided else 0.toByte
    val isInvertedMaskBit: Byte = if (isInvertedMask) ConstantDrawableFlagMask.csmIsInvertedMask else 0.toByte
    val flagsValue: Byte = (blendModeBit | isCullingBit | isInvertedMaskBit).toByte

    val dynamicFlag = stub[DynamicFlags]
    (() => dynamicFlag.isVisible).when().returns(isVisible)
    val opacityPointer = new Memory(4)
    opacityPointer.setFloat(0, opacity)
    Drawable(id, index, ConstantFlags(flagsValue), dynamicFlag, index, Nil, vertexInfo, null, null, opacityPointer)
  }

  private def createStubbedLive2DModel(drawable: List[Drawable]) = {
    val live2DModel = stub[Live2DModel]
    (() => live2DModel.modelMatrix).when().returns(StubbedModelMatrix)
    (() => live2DModel.sortedDrawables).when().returns(drawable)
    live2DModel
  }
  
}
