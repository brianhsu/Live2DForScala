package moe.brianhsu.porting

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.porting.live2d.renderer.opengl.Profile
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ProfileFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock {
  Feature("Singleton by OpenGL binding") {
    Scenario("Get instance with same OpenGL binding") {
      Given("Given a stubbed OpenGL binding")
      val binding = createOpenGLStub()

      When("Create a profile from that binding")
      val thisProfile = Profile.getInstance(binding)

      And("Create another profile from that binding")
      val thatProfile = Profile.getInstance(binding)

      Then("This two profile should be same object instance")
      thisProfile should be theSameInstanceAs thatProfile
    }

    Scenario("Get instance with different OpenGL binding") {
      Given("Given a stubbed OpenGL binding")
      val thisBinding = createOpenGLStub()

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
      val binding = createProfileOpenGLBinding()
      val profile = Profile.getInstance(binding)

      When("When save")
      profile.save()

      Then("it should enable texture")
      import binding.openGLConstants._
      (binding.glActiveTexture _).verify(GL_TEXTURE1).once()
      (binding.glActiveTexture _).verify(GL_TEXTURE0).once()

    }
  }

  private def createProfileOpenGLBinding(): OpenGLBinding = {
    val binding = createOpenGLStub()
    import binding.openGLConstants._

    addGetIntegervBinding(binding, GL_ARRAY_BUFFER_BINDING, 0, 1234)
    addGetIntegervBinding(binding, GL_ELEMENT_ARRAY_BUFFER_BINDING, 1, 5678)
    addGetIntegervBinding(binding, GL_CURRENT_PROGRAM, 2, 9012)
    addGetIntegervBinding(binding, GL_ACTIVE_TEXTURE, 3, 3456)
    addGetIntegervBinding(binding, GL_TEXTURE_BINDING_2D, 0, 789).noMoreThanOnce()
    addGetIntegervBinding(binding, GL_TEXTURE_BINDING_2D, 0, 1234).noMoreThanOnce()

    binding
  }

  private def addGetIntegervBinding(binding: OpenGLBinding, pname: Int, index: Int, expectedValue: Int) = {
    (binding.glGetIntegerv: (Int, Array[Int], Int) => Unit)
      .when(pname, *, index)
      .onCall((_, array, index) => array(index) = expectedValue)
  }
}
