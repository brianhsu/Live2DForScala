package moe.brianhsu.porting

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.{BlendFunction, ColorWriteMask, ViewPort}
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.porting.live2d.renderer.opengl.Profile
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Inside, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.reflect.runtime.universe._

class ProfileFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock
                     with OptionValues with Inside {
  Feature("Singleton by OpenGL binding") {
    Scenario("Get instance with same OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val binding = createOpenGLStub()

      When("create a profile from that binding")
      val thisProfile = Profile.getInstance(binding)

      And("create another profile from that binding")
      val thatProfile = Profile.getInstance(binding)

      Then("this two profile should be same object instance")
      thisProfile should be theSameInstanceAs thatProfile

      And("it should able to save / restore profile without problem")
      thatProfile.save()
      thisProfile.restore()
    }

    Scenario("Get instance with different OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val thisBinding = createOpenGLStub()

      When("create a profile from that binding")
      val thisProfile = Profile.getInstance(thisBinding)

      And("create another profile from another binding")
      val thatBinding = stub[OpenGLBinding]
      val thatProfile = Profile.getInstance(thatBinding)

      Then("this two profile should be same object instance")
      thisProfile should not be theSameInstanceAs (thatProfile)
    }
  }


  Feature("Save profile") {
    Scenario("Initial state") {
      Given("a stubbed OpenGL binding and a Profile")
      val binding = createOpenGLStub()
      val richOpenGLBinding = stub[RichOpenGLBinding]
      val profile = createProfile(binding, richOpenGLBinding)

      Then("the lastFrameBufferBinding / lastViewPort should be 0 / null")
      profile.lastFrameBufferBinding shouldBe 0
      profile.lastViewPort should be (null)
    }

    Scenario("Save state") {
      Given("a stubbed OpenGL binding and a Profile")
      val binding = createOpenGLStub()
      val richOpenGLBinding = stub[RichOpenGLBinding]
      val profile = createProfile(binding, richOpenGLBinding)

      import binding.constants._

      And("the OpenGL binding return some value for frameBufferBinding / viewPort")
      (() => richOpenGLBinding.viewPort).when().returning(ViewPort(1234, 5678, 9012, 3456))
      (richOpenGLBinding.openGLParameters(_: Int)(_: TypeTag[Int]))
        .when(GL_FRAMEBUFFER_BINDING, typeTag[Int])
        .returning(123)

      When("save the profile")
      profile.save()

      Then("the lastFrameBufferBinding should have correct value")
      profile.lastFrameBufferBinding shouldBe 123
      inside(profile.lastViewPort) { case ViewPort(x, y, width, height) =>
        x shouldBe 1234
        y shouldBe 5678
        width shouldBe 9012
        height shouldBe 3456
      }
    }
    Scenario("Restore state before save any state") {
      Given("a stubbed OpenGL binding and a Profile")
      val binding = createOpenGLStub()
      val richOpenGLBinding = stub[RichOpenGLBinding]
      val profile = createProfile(binding, richOpenGLBinding)

      And("restore it before call save")
      Then("it should thrown exception")
      an[IllegalStateException] shouldBe thrownBy {
        profile.restore()
      }
    }

    Scenario("Restore state when there is saved state") {
      Given("a stubbed OpenGL binding and a Profile")
      val binding = createOpenGLStub()
      val richOpenGLBinding = stub[RichOpenGLBinding]
      val profile = createProfile(binding, richOpenGLBinding)

      And("the OpenGL binding return some value for various parameters")
      import binding.constants._
      addDummyIntOpenGLParameter(richOpenGLBinding, GL_ARRAY_BUFFER_BINDING, 1)
      addDummyIntOpenGLParameter(richOpenGLBinding, GL_ELEMENT_ARRAY_BUFFER_BINDING, 2)
      addDummyIntOpenGLParameter(richOpenGLBinding, GL_CURRENT_PROGRAM, 3)
      addDummyIntOpenGLParameter(richOpenGLBinding, GL_ACTIVE_TEXTURE, 4)
      (richOpenGLBinding.textureBinding2D _).when(GL_TEXTURE0).returns(5)
      (richOpenGLBinding.textureBinding2D _).when(GL_TEXTURE1).returns(6)
      (() => richOpenGLBinding.vertexAttributes).when().returns(Array(true, false, true, false))
      (binding.glIsEnabled _).when(GL_SCISSOR_TEST).returns(true)
      (binding.glIsEnabled _).when(GL_STENCIL_TEST).returns(false)
      (binding.glIsEnabled _).when(GL_DEPTH_TEST).returns(true)
      (binding.glIsEnabled _).when(GL_CULL_FACE).returns(false)
      (binding.glIsEnabled _).when(GL_BLEND).returns(true)
      addDummyIntOpenGLParameter(richOpenGLBinding, GL_FRONT_FACE, 7)
      (() => richOpenGLBinding.colorWriteMask).when().returns(ColorWriteMask(red = true, green = false, blue = true, alpha = false))
      (() => richOpenGLBinding.blendFunction).when().returns(BlendFunction(12, 34, 56, 78))

      And("save the profile")
      profile.save()

      When("restore the profile")
      profile.restore()

      Then("it should call the following method to restore profile")
      inSequence {
        (binding.glUseProgram _).verify(3).once()
      }
    }

  }

  private def addDummyIntOpenGLParameter(richOpenGLBinding: RichOpenGLBinding, pname: Int, value: Int): Unit = {
    (richOpenGLBinding.openGLParameters(_: Int)(_: TypeTag[Int]))
      .when(pname, typeTag[Int])
      .returning(value)

  }

  private def createProfile(binding: OpenGLBinding, richOpenGLBinding: RichOpenGLBinding): Profile = {
    val converter: OpenGLBinding => RichOpenGLBinding = _ => richOpenGLBinding
    new Profile()(binding, converter)
  }

}
