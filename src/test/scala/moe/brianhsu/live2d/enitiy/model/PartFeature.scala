package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.live2d.enitiy.model
import moe.brianhsu.porting.live2d.utils.NativeMemoryUtils
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class PartFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  Feature("Accessing opacity data from C Pointer") {
    Scenario("Reading opacity value from C memory") {
      Given("a pointer to a C memory that is a float number")
      val expectedOpacity = 123.456f
      val pointer = NativeMemoryUtils.createPointerToFloat(expectedOpacity)

      When("create a Part from that pointer")
      val part = model.Part(pointer, "partId", None)

      Then("it should able to read correct opacity value")
      part.opacity shouldBe expectedOpacity
    }

    Scenario("Write opacity value to C memory") {
      Given("a Part associated with a C memory")
      val pointer = NativeMemoryUtils.createPointerToFloat(0)
      val part = Part(pointer, "partId", None)

      When("set opacity of a Part")
      part.opacity = 567.123f

      Then("the value of the native memory should have the updated value")
      pointer.getFloat(0) shouldBe 567.123f
    }
  }

}
