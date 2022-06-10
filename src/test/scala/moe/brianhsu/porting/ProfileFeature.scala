package moe.brianhsu.porting

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.porting.live2d.renderer.opengl.Profile
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ProfileFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {
  Feature("Singleton by OpenGL binding") {
    Scenario("Get instance with same OpenGL binding") {
      Given("Given a stubbed OpenGL binding")
      val binding = stub[OpenGLBinding]

      When("Create a profile from that binding")
      val thisProfile = Profile.getInstance(binding)

      And("Create another profile from that binding")
      val thatProfile = Profile.getInstance(binding)

      Then("This two profile should be same object instance")
      thisProfile should be theSameInstanceAs thatProfile
    }

    Scenario("Get instance with different OpenGL binding") {
      Given("Given a stubbed OpenGL binding")
      val thisBinding = stub[OpenGLBinding]

      When("Create a profile from that binding")
      val thisProfile = Profile.getInstance(thisBinding)

      And("Create another profile from another binding")
      val thatBinding = stub[OpenGLBinding]
      val thatProfile = Profile.getInstance(thatBinding)

      Then("This two profile should be same object instance")
      thisProfile should not be theSameInstanceAs (thatProfile)
    }
  }

  Feature("Save profile") {
    Scenario("Save profile") {
      Given("Given a stubbed OpenGL binding and a Profile")
      val binding = createStubbedOpenGLBinding()
      val profile = Profile.getInstance(binding)

      When("When save")
      profile.save()

      Then("it should enable texture")
      (binding.glActiveTexture _).verify(binding.GL_TEXTURE1).once()
      (binding.glActiveTexture _).verify(binding.GL_TEXTURE0).once()

    }
  }

  private def createStubbedOpenGLBinding(): OpenGLBinding = {
    val binding = stub[OpenGLBinding]

    (() => binding.GL_ARRAY_BUFFER_BINDING).when().returns(1)
    (() => binding.GL_ELEMENT_ARRAY_BUFFER_BINDING).when().returns(2)
    (() => binding.GL_CURRENT_PROGRAM).when().returns(3)
    (() => binding.GL_ACTIVE_TEXTURE).when().returns(4)
    (() => binding.GL_TEXTURE1).when().returns(5)
    (() => binding.GL_TEXTURE0).when().returns(6)
    (() => binding.GL_TEXTURE_BINDING_2D).when().returns(7)

    addGetIntegervBinding(binding, binding.GL_ARRAY_BUFFER_BINDING, 0, 1234)
    addGetIntegervBinding(binding, binding.GL_ELEMENT_ARRAY_BUFFER_BINDING, 1, 5678)
    addGetIntegervBinding(binding, binding.GL_CURRENT_PROGRAM, 2, 9012)
    addGetIntegervBinding(binding, binding.GL_ACTIVE_TEXTURE, 3, 3456)
    addGetIntegervBinding(binding, binding.GL_TEXTURE_BINDING_2D, 0, 789).noMoreThanOnce()
    addGetIntegervBinding(binding, binding.GL_TEXTURE_BINDING_2D, 0, 1234).noMoreThanOnce()

    binding
  }

  private def addGetIntegervBinding(binding: OpenGLBinding, pname: Int, index: Int, expectedValue: Int) = {
    (binding.glGetIntegerv: (Int, Array[Int], Int) => Unit)
      .when(pname, *, index)
      .onCall((_, array, index) => array(index) = expectedValue)
  }
}
