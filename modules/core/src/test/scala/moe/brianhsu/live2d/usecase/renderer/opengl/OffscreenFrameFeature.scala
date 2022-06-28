package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Inside, OptionValues}

class OffscreenFrameFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock
                     with OptionValues with Inside {

  private val ColorTextureBufferId = 1
  private val FrameBufferId = 2
  private val DisplayBufferWidth = 1024
  private val DisplayBufferHeight = 768

  Feature("Singleton by OpenGL binding") {
    Scenario("Get instance with same OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGLBinding = generateRichOpenGLBinding()

      When("create a OffscreenFrame from that binding")
      val thisFrame = OffscreenFrame.getInstance(DisplayBufferWidth, DisplayBufferHeight)(binding, _ => richOpenGLBinding)

      And("create another OffscreenFrame from that binding")
      val thatFrame = OffscreenFrame.getInstance(DisplayBufferWidth, DisplayBufferHeight)(binding, _ => richOpenGLBinding)

      Then("this two OffscreenFrame should be same object instance")
      thisFrame should be theSameInstanceAs thatFrame
    }

    Scenario("Get instance with different OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val thisBinding = createOpenGLStub()
      val richOpenGLBinding = generateRichOpenGLBinding()

      When("create a OffscreenFrame from that binding")
      val thisFrame = OffscreenFrame.getInstance(DisplayBufferWidth, DisplayBufferHeight)(thisBinding, _ => richOpenGLBinding)

      And("create another OffscreenFrame from another binding")
      val thatBinding = createOpenGLStub()
      val thatFrame = OffscreenFrame.getInstance(DisplayBufferWidth, DisplayBufferHeight)(thatBinding, _ => richOpenGLBinding)

      Then("this two OffscreenFrame should be same object instance")
      thisFrame should not be theSameInstanceAs (thatFrame)
    }
  }

  Feature("Initialization") {
    Scenario("Initialize when object is created") {
      Given("a stubbed OpenGL binding")
      val openGL = createOpenGLStub()
      val richOpenGL = generateRichOpenGLBinding()
      import openGL.constants._

      And("the original frameBufferId is 9")
      addDummyIntOpenGLParameter(richOpenGL, GL_FRAMEBUFFER_BINDING, 9)

      When("create a OffscreenFrame from that binding")
      val offscreenFrame = OffscreenFrame.getInstance(1024, 768)(openGL, { _ => richOpenGL })

      Then("it should have correct texture / color buffer id")
      offscreenFrame.colorTextureBufferId shouldBe ColorTextureBufferId
      offscreenFrame.frameBufferId shouldBe FrameBufferId

      And("it should initialize color buffer")
      inSequence {
        (openGL.glBindTexture _).verify(GL_TEXTURE_2D, ColorTextureBufferId).once()
        (openGL.glTexImage2D _).verify(
          GL_TEXTURE_2D, 0, GL_RGBA, DisplayBufferWidth, DisplayBufferHeight, 0,
          GL_RGBA, GL_UNSIGNED_BYTE, null
        ).once()

        (openGL.glTexParameteri _).verify(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        (openGL.glTexParameteri _).verify(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        (openGL.glTexParameteri _).verify(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        (openGL.glTexParameteri _).verify(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        (openGL.glBindTexture _).verify(GL_TEXTURE_2D, 0).once()
      }

      And("it should initial texture buffer")
      inSequence {
        (openGL.glBindFramebuffer _).verify(GL_FRAMEBUFFER, FrameBufferId).once()
        (openGL.glFramebufferTexture2D _).verify(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, 1, 0).once()
        (openGL.glBindFramebuffer _).verify(GL_FRAMEBUFFER, 9).once()
      }

    }
  }

  Feature("Drawing") {
    Scenario("Begin drawing") {
      Given("a stubbed OpenGL binding")
      val openGL = createOpenGLStub()

      And("create a OffscreenFrame from that binding")
      val offscreenFrame = new OffscreenFrame(0, 1)(openGL)

      And("begin draw with originalFrameBufferId = 123")
      offscreenFrame.beginDraw(123)

      Then("it should ")
    }

    Scenario("End drawing") {
      Given("a stubbed OpenGL binding")
      val openGL = createOpenGLStub()

      And("create a OffscreenFrame from that binding")
      val offscreenFrame = new OffscreenFrame(0, 1)(openGL)

      And("begin draw with originalFrameBufferId = 123")
      offscreenFrame.beginDraw(123)

      When("end drawing")
      offscreenFrame.endDraw()

      Then("the OpenGL binding should switch back to original frame buffer binding")
      import openGL.constants._
      (openGL.glBindFramebuffer _).verify(GL_FRAMEBUFFER, 123).atLeastOnce()
    }

  }

  private def generateRichOpenGLBinding(): RichOpenGLBinding = {
    val richOpenGLBinding = stub[RichOpenGLBinding]
    (richOpenGLBinding.generateTextures _).when(*).returns(List(ColorTextureBufferId))
    (richOpenGLBinding.generateFrameBuffers _).when(*).returns(List(FrameBufferId))
    richOpenGLBinding
  }



}
