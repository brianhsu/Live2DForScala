package moe.brianhsu.live2d.enitiy.avatar.effect.data.physics

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ParameterTypeFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  Feature("Get parameter type from string") {
    Scenario("The parameter type is valid") {
      val expectation = Table(
        ("typeInString", "expectedType"),
        (           "X", ParameterType.X),
        (           "Y", ParameterType.Y),
        (       "Angle", ParameterType.Angle),
      )

      forAll(expectation) { (typeInString, expectedType) =>
        Given(s"A string $typeInString")
        When("Get parameter type from it")
        val parameterType = ParameterType(typeInString)

        Then(s"the parameter type shouldBe $expectedType")
        parameterType shouldBe expectedType

      }
    }

    Scenario("The parameter type is invalid") {

      Given("A invalid type string")
      val typeInString = "invalid"

      When("Get parameter type from it")
      Then("a UnsupportedOperationException exception should be thrown")
      an[UnsupportedOperationException] should be thrownBy {
        ParameterType(typeInString)
      }

    }

  }

}
