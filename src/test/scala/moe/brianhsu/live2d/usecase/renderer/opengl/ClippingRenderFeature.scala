package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureManager
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingManager
import moe.brianhsu.utils.mock.{Live2DModelMock, OpenGLMock, ShaderFactoryMock}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ClippingRenderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                            with Live2DModelMock with OpenGLMock with TableDrivenPropertyChecks
                            with ShaderFactoryMock with OptionValues {

  private val StubbedTextureId = 1
  private val StubbedProgramId = 2
  private val StubbedPositionLocation = 3
  private val StubbedUvLocation = 4
  private val StubbedTextureLocation = 5
  private val StubbedBaseColorLocation = 6

  Feature("Create offscreenFrameHolder") {
    Scenario("There is no clipping manager at all") {
      Given("a stubbed OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = {x => richOpenGLBinding}
      val live2DModel = new Live2DModel(stub[ModelBackend]) {
        override lazy val containMaskedDrawables: Boolean = false
      }
      val textureManager = stub[TextureManager]
      val shaderRenderer = new ShaderRenderer(createShaderFactory())

      When("create a ClippingRenderer")
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer)

      Then("the offscreenFrameHolder of that renderer should be None")
      renderer.offscreenFrameHolder shouldBe None
    }

    Scenario("There is a clipping manager") {
      Given("a stubbed OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = {x => richOpenGLBinding}
      val textureManager = stub[TextureManager]
      val shaderRenderer = new ShaderRenderer(createShaderFactory())
      val clippingManager = stub[ClippingManager]
      val live2DModel = new Live2DModel(stub[ModelBackend])

      And("RichOpenGL binding returns corresponding color texture / frame buffer id")
      val stubbedColorTextureId = 1
      val stubbedFrameBufferId = 2
      (richOpenGLBinding.generateTextures _).when(*).returns(List(stubbedColorTextureId))
      (richOpenGLBinding.generateFrameBuffers _).when(*).returns(List(stubbedFrameBufferId))

      When("create a ClippingRenderer")
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, Some(clippingManager))

      Then("the offscreenFrameHolder of that renderer should be a valid OffscreenFrame")
      renderer.offscreenFrameHolder.value.colorTextureBufferId shouldBe stubbedColorTextureId
      renderer.offscreenFrameHolder.value.frameBufferId shouldBe stubbedFrameBufferId
    }

  }

  Feature("Get ClippingContext for draw by drawable") {
    Scenario("There is no matched ClippingContext") {
      Given("a stubbed OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = {x => richOpenGLBinding}
      val textureManager = stub[TextureManager]
      val shaderRenderer = new ShaderRenderer(createShaderFactory())
      val live2DModel = new Live2DModel(stub[ModelBackend])

      And("RichOpenGL binding returns corresponding color texture / frame buffer id")
      val stubbedColorTextureId = 1
      val stubbedFrameBufferId = 2
      (richOpenGLBinding.generateTextures _).when(*).returns(List(stubbedColorTextureId))
      (richOpenGLBinding.generateFrameBuffers _).when(*).returns(List(stubbedFrameBufferId))

      And("create a ClippingRenderer with ClippingManager that drawable(id1) don't have ClippingContext")
      val (idToDrawable, clippingManager) = createClippingManager()
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, Some(clippingManager))

      When("ask clippingContextBufferForDraw with drawable2")
      val clippingContextHolder = renderer.clippingContextBufferForDraw(idToDrawable("id1"))

      Then("it should be None")
      clippingContextHolder shouldBe None
    }

    Scenario("There is a matched ClippingContext") {
      Given("a stubbed OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = {x => richOpenGLBinding}
      val textureManager = stub[TextureManager]
      val shaderRenderer = new ShaderRenderer(createShaderFactory())
      val live2DModel = new Live2DModel(stub[ModelBackend])

      And("RichOpenGL binding returns corresponding color texture / frame buffer id")
      val stubbedColorTextureId = 1
      val stubbedFrameBufferId = 2
      (richOpenGLBinding.generateTextures _).when(*).returns(List(stubbedColorTextureId))
      (richOpenGLBinding.generateFrameBuffers _).when(*).returns(List(stubbedFrameBufferId))

      And("create a ClippingRenderer with ClippingManager that drawable(id3) has a ClippingContext")
      val (idToDrawable, clippingManager) = createClippingManager()
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, Some(clippingManager))

      When("ask clippingContextBufferForDraw with drawable2")
      val clippingContextHolder = renderer.clippingContextBufferForDraw(idToDrawable("id3"))

      Then("it should return a valid clipping context")
      clippingContextHolder.value shouldBe a[ClippingContext]
      clippingContextHolder.value.maskDrawable should contain theSameElementsInOrderAs List(idToDrawable("id1"), idToDrawable("id2"))
      clippingContextHolder.value.clippedDrawables should contain theSameElementsAs List(idToDrawable("id4"), idToDrawable("id3"))
    }

  }

  private def createClippingManager(): (Map[String, Drawable], ClippingManager) = {
    val mockedDrawables = Map(
      "id1" -> createDrawable("id1", index = 0, Nil),
      "id2" -> createDrawable("id2", index = 1, Nil),
      "id3" -> createDrawable("id3", index = 2, List(1, 0)),
      "id4" -> createDrawable("id4", index = 3, List(0, 1)),
      "id5" -> createDrawable("id5", index = 4, List(1)),
    )
    val backend = new MockedBackend(drawables = mockedDrawables)
    val live2DModel = new Live2DModel(backend)

    (mockedDrawables, ClippingManager.fromLive2DModel(live2DModel).value)
  }
}
