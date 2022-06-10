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
}
