package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.adapter.gateway.model
import moe.brianhsu.live2d.exception.ParameterInvalidException
import moe.brianhsu.porting.live2d.utils.NativeMemoryUtils
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class CPointerParameterFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  Feature("Accessing current data from C Pointer") {
    Scenario("Reading current value from C memory") {
      Given("a pointer to a C memory that is a float number")
      val expectedValue = 123.456f
      val pointer = NativeMemoryUtils.createPointerToFloat(expectedValue)

      When("create a Parameter from that pointer")
      val parameter = CPointerParameter(pointer, "parameterId", 0, 1000.0f, 456.0f)

      Then("it should able to read correct opacity value")
      parameter.current shouldBe expectedValue
    }

    Scenario("Write valid current value to C memory") {
      Given("a Parameter associated with a C memory")
      val pointer = NativeMemoryUtils.createPointerToFloat(0)
      val parameter = model.CPointerParameter(pointer, "parameterId", 0, 100, 0)

      When("update current value of a Parameter")
      parameter.doUpdateValue(12.3f)

      Then("the value of the native memory should have the updated value")
      pointer.getFloat(0) shouldBe 12.3f
    }

    Scenario("Write value that is under minimum or over maximum value to C memory") {
      Given("a Parameter associated with a C memory that minimum value is 100")
      val parameterId = "parameterId"
      val min = 100.0f
      val max = 200.0f
      val pointer = NativeMemoryUtils.createPointerToFloat(0)
      val parameter = model.CPointerParameter(pointer, parameterId, min, max, default = 150)

      When("update current value that is lower than minimum or greater than maximum value")
      Then("it should throw exception")
      List(12.3f, 567.8f).foreach { updatedValue =>
        the[ParameterInvalidException] thrownBy {
          parameter.doUpdateValue(updatedValue)
        } should have message expectedExceptionMessage(updatedValue, parameterId, min, max)
      }
    }

    def expectedExceptionMessage(value: Float, id: String, min: Float, max: Float) = {
      s"The assigned parameter value $value of $id is invalid, the range should be in [$min, $max]"
    }

  }

}
