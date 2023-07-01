package moe.brianhsu.live2d.usecase.renderer.opengl.clipping

import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI.ConstantDrawableFlagMask.csmIsDoubleSided
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable.ColorFetcher
import moe.brianhsu.live2d.enitiy.model.drawable.{ConstantFlags, Drawable, DrawableColor, DynamicFlags, VertexInfo}
import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.ViewPort
import moe.brianhsu.live2d.enitiy.opengl.texture.{TextureInfo, TextureManager}
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.live2d.usecase.renderer.opengl.{OffscreenFrame, Profile}
import moe.brianhsu.utils.mock.{Live2DModelMock, OpenGLMock, ShaderFactoryMock}
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{GivenWhenThen, OptionValues}

import java.nio.ByteBuffer

class ClippingRenderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                            with Live2DModelMock with OpenGLMock with TableDrivenPropertyChecks
                            with ShaderFactoryMock with OptionValues {

  Feature("Create offscreenFrameHolder") {
    Scenario("There is no clipping manager at all") {
      Given("a stubbed OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
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
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
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
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
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
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
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

  Feature("Draw clipping") {
    Scenario("There is no clipping manager at all") {
      Given("a mocked OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLMock()
      val richOpenGLBinding: RichOpenGLBinding = mock[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
      val textureManager = stub[TextureManager]
      val shaderRenderer = new ShaderRenderer(createShaderFactory())
      val live2DModel = new Live2DModel(stub[ModelBackend])

      And("create a ClippingRenderer with ClippingManager that has no clipping manager")
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, None)

      Then("nothing should happen and no exception is thrown")
      noException should be thrownBy {
        When("draw it with mocked profile")
        val mockedProfile = mock[Profile]
        renderer.draw(mockedProfile)
      }
    }

    Scenario("There is clipping manager but no clipping is in using") {
      Given("a mocked OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLMock()
      val richOpenGLBinding: RichOpenGLBinding = mock[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
      val textureManager = stub[TextureManager]
      val shaderRenderer = new ShaderRenderer(createShaderFactory())
      val live2DModel = new Live2DModel(stub[ModelBackend])

      val stubbedClippingManager = stub[ClippingManager]
      (() => stubbedClippingManager.usingClipCount).when().returns(10)

      And("the stubbedClippingManager will return another clipping manager that has no using clipping after update context list")
      val newClippingManager = stub[ClippingManager]
      (() => newClippingManager.usingClipCount).when().returns(0)
      (() => stubbedClippingManager.updateContextListForMask()).when().returns(newClippingManager)

      And("create a ClippingRenderer with ClippingManager that has no clipping manager")
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, Some(stubbedClippingManager), None)

      Then("nothing should happen and no exception is thrown")
      noException should be thrownBy {
        When("draw it with mocked profile")
        val mockedProfile = mock[Profile]
        renderer.draw(mockedProfile)
      }
    }

    Scenario("Both clipping manager and in using clipping exist - without OffscreenFrame") {
      Given("4 drawable that 2 of them is vertex changed")
      val idToDrawable = Map(
        "id0" -> createDrawable("id0", 0, isCulling = false, isVertexChanged = true),
        "id1" -> createDrawable("id1", 1, isCulling = false, isVertexChanged = false),
        "id2" -> createDrawable("id2", 2, isCulling = true, isVertexChanged = true),
        "id3" -> createDrawable("id3", 3, isCulling = false, isVertexChanged = false),
      )

      And("a clipping manager based on those drawables")
      val clippingContextList = List(
        new ClippingContext(List(idToDrawable("id0"), idToDrawable("id1")), Nil),
        new ClippingContext(List(idToDrawable("id2"), idToDrawable("id3")), Nil),
      )
      val clippingManager = stub[ClippingManager]
      (() => clippingManager.updateContextListForMask()).when().returns(clippingManager)
      (() => clippingManager.contextListForMask).when().returns(clippingContextList)
      (() => clippingManager.usingClipCount).when().returns(1)

      And("a mocked OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
      val shaderRenderer = stub[MockableShaderRenderer]
      val live2DModel = new Live2DModel(stub[ModelBackend]) {
        override val textureFiles: List[String] = List("0.png", "1.png" ,"2.png", "3.png")
      }
      val textureManager = stub[TextureManager]
      (textureManager.loadTexture _).when("0.png").returns(TextureInfo(0, 4, 5))
      (textureManager.loadTexture _).when("1.png").returns(TextureInfo(1, 6, 7))
      (textureManager.loadTexture _).when("2.png").returns(TextureInfo(2, 8, 9))
      (textureManager.loadTexture _).when("3.png").returns(TextureInfo(3, 10, 11))

      val stubbedViewPort = ViewPort(100, 200, 300, 400)
      val stubbedProfile = new Profile()
      stubbedProfile.lastViewPort = stubbedViewPort

      And("create a ClippingRenderer with ClippingManager based on that clipping manager but without offscreen frame")
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, Some(clippingManager), None)

      When("draw it with mocked profile")
      renderer.draw(stubbedProfile)

      Then("it should delegated to underlay bindings")
      inSequence {
        import binding.constants._
        (binding.glViewport _).verify(0, 0, ClippingManager.MaskBufferSize, ClippingManager.MaskBufferSize).once()
        (() => richOpenGLBinding.preDraw()).verify().once()


        inSequence {
          // First mask drawable
          (binding.setCapabilityEnabled _).verify(GL_CULL_FACE, true)
          (binding.glFrontFace _).verify(GL_CCW).once()
          (shaderRenderer.renderMask _).verify(
            clippingContextList.head, 0, *, *,
            DrawableColor(1.0f, 2.0f, 3.0f, 4.0f),
            DrawableColor(4.0f, 3.0f, 2.0f, 1.0f)
          ).once()
          (binding.glDrawElements _).verify(GL_TRIANGLES, 0, *, *).once()
          (binding.glUseProgram _).verify(0).once()
        }

        inSequence {
          // Second mask drawable
          (binding.setCapabilityEnabled _).verify(GL_CULL_FACE, false)
          (binding.glFrontFace _).verify(GL_CCW).once()
          (shaderRenderer.renderMask _).verify(
            clippingContextList(1), 2, *, *,
            DrawableColor(1.0f, 2.0f, 3.0f, 4.0f),
            DrawableColor(4.0f, 3.0f, 2.0f, 1.0f)
          ).once()
          (binding.glDrawElements _).verify(GL_TRIANGLES, 2, *, *).once()
          (binding.glUseProgram _).verify(0).once()
        }

        (richOpenGLBinding.viewPort_= _).verify(stubbedViewPort).once()
      }
    }

    Scenario("Both clipping manager and in using clipping exist - with OffscreenFrame") {
      Given("4 drawable that 2 of them is vertex changed")
      val idToDrawable = Map(
        "id0" -> createDrawable("id0", 0, isCulling = false, isVertexChanged = true),
        "id1" -> createDrawable("id1", 1, isCulling = false, isVertexChanged = false),
        "id2" -> createDrawable("id2", 2, isCulling = true, isVertexChanged = true),
        "id3" -> createDrawable("id3", 3, isCulling = false, isVertexChanged = false),
      )

      And("a clipping manager based on those drawables")
      val clippingContextList = List(
        new ClippingContext(List(idToDrawable("id0"), idToDrawable("id1")), Nil),
        new ClippingContext(List(idToDrawable("id2"), idToDrawable("id3")), Nil),
      )
      val clippingManager = stub[ClippingManager]
      (() => clippingManager.updateContextListForMask()).when().returns(clippingManager)
      (() => clippingManager.contextListForMask).when().returns(clippingContextList)
      (() => clippingManager.usingClipCount).when().returns(1)

      And("a mocked OpenGL binding / Live2D model / TextureManager / ShaderRenderer")
      implicit val binding: OpenGLBinding = createOpenGLStub()
      val richOpenGLBinding: RichOpenGLBinding = stub[RichOpenGLBinding]
      implicit val wrapper: OpenGLBinding => RichOpenGLBinding = { _ => richOpenGLBinding}
      val shaderRenderer = stub[MockableShaderRenderer]
      val live2DModel = new Live2DModel(stub[ModelBackend]) {
        override val textureFiles: List[String] = List("0.png", "1.png" ,"2.png", "3.png")
      }
      val textureManager = stub[TextureManager]
      (textureManager.loadTexture _).when("0.png").returns(TextureInfo(0, 4, 5))
      (textureManager.loadTexture _).when("1.png").returns(TextureInfo(1, 6, 7))
      (textureManager.loadTexture _).when("2.png").returns(TextureInfo(2, 8, 9))
      (textureManager.loadTexture _).when("3.png").returns(TextureInfo(3, 10, 11))

      val stubbedViewPort = ViewPort(100, 200, 300, 400)
      val stubbedProfile = new Profile()
      stubbedProfile.lastViewPort = stubbedViewPort
      stubbedProfile.lastFrameBufferBinding = 1234

      val offscreenFrame = stub[OffscreenFrame]

      And("create a ClippingRenderer with ClippingManager based on that clipping manager and with offscreen frame")
      val renderer = new ClippingRenderer(live2DModel, textureManager, shaderRenderer, Some(clippingManager), Some(offscreenFrame))

      When("draw it with mocked profile")
      renderer.draw(stubbedProfile)

      Then("it should delegated to underlay bindings")
      inSequence {
        import binding.constants._
        (binding.glViewport _).verify(0, 0, ClippingManager.MaskBufferSize, ClippingManager.MaskBufferSize).once()
        (() => richOpenGLBinding.preDraw()).verify().once()
        (offscreenFrame.beginDraw _).verify(1234).once()

        inSequence {
          // First mask drawable
          (binding.setCapabilityEnabled _).verify(GL_CULL_FACE, true)
          (binding.glFrontFace _).verify(GL_CCW).once()
          (shaderRenderer.renderMask _).verify(
            clippingContextList.head, 0, *, *,
            DrawableColor(1.0f, 2.0f, 3.0f, 4.0f),
            DrawableColor(4.0f, 3.0f, 2.0f, 1.0f)
          ).once()
          (binding.glDrawElements _).verify(GL_TRIANGLES, 0, *, *).once()
          (binding.glUseProgram _).verify(0).once()
        }

        inSequence {
          // Second mask drawable
          (binding.setCapabilityEnabled _).verify(GL_CULL_FACE, false)
          (binding.glFrontFace _).verify(GL_CCW).once()
          (shaderRenderer.renderMask _).verify(
            clippingContextList(1), 2, *, *,
            DrawableColor(1.0f, 2.0f, 3.0f, 4.0f),
            DrawableColor(4.0f, 3.0f, 2.0f, 1.0f)
          ).once()
          (binding.glDrawElements _).verify(GL_TRIANGLES, 2, *, *).once()
          (binding.glUseProgram _).verify(0).once()
        }

        (offscreenFrame.endDraw _).verify().once()
        (richOpenGLBinding.viewPort_= _).verify(stubbedViewPort).once()
      }
    }
  }

  val multiplyColorFetcher: ColorFetcher = () => DrawableColor(1.0f, 2.0f, 3.0f, 4.0f)
  val screenColorFetcher: ColorFetcher = () => DrawableColor(4.0f, 3.0f, 2.0f, 1.0f)

  private def createDrawable(id: String, index:Int, isCulling: Boolean, isVertexChanged: Boolean): Drawable = {
    val vertexInfo = new VertexInfo(index, index, null, null, null) {
      override def vertexArrayDirectBuffer: ByteBuffer = ByteBuffer.allocate(1)
      override def uvArrayDirectBuffer: ByteBuffer = ByteBuffer.allocate(1)
      override def indexArrayDirectBuffer: ByteBuffer = ByteBuffer.allocate(1)
    }
    val flagsValue = if (isCulling) 0 | csmIsDoubleSided else 0
    val dynamicFlag = stub[DynamicFlags]
    (() => dynamicFlag.vertexPositionChanged).when().returns(isVertexChanged)

    Drawable(
      id, index, None, ConstantFlags(flagsValue.toByte), dynamicFlag, index, Nil,
      vertexInfo, null, null, null, multiplyColorFetcher, screenColorFetcher
    )
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
