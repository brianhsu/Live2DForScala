package moe.brianhsu.live2d.enitiy.model.parameters

import moe.brianhsu.live2d.enitiy.model.parameter.ParameterType
import moe.brianhsu.live2d.exception.UnknownParameterTypeValueException
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ParameterTypeFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {
  Feature("Convert type value to ParameterType object") {
    Scenario("Convert known type value to correct ParameterType") {
      val table = Table(
        ("value", "expected"),
        (0, ParameterType.Normal),
        (1, ParameterType.BlendShape)
      )

      forAll(table) { case (value, expectedType) =>
        When(s"convert value=$value to ParameterType")
        val parameterType = ParameterType(value)

        Then(s"it should be $expectedType")
        parameterType shouldBe expectedType
      }
    }

    Scenario("Convert unknown type value") {
      When("convert unknown value to parameter")
      Then("it should throw UnknownParameterTypeValueException")
      an[UnknownParameterTypeValueException] should be thrownBy {
        ParameterType(123)
      }
    }
  }

}
