package moe.brianhsu.live2d.enitiy.math

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class EasingFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  Feature("Calculate the easing sine") {
    Scenario("The value is under 0.0") {
      When("The passed in value is under 0.0")
      val easingValue = Easing.sine(-1)

      Then("it should just become 0.0f")
      easingValue shouldBe 0.0f
    }

    Scenario("The value exceed 1.0") {
      When("The passed in value exceed 1.0")
      val easingValue = Easing.sine(1.1f)

      Then("it should just become 1.0f")
      easingValue shouldBe 1.0f
    }

    Scenario("The value is inside range 0.0 ~ 1.0f") {

      val dataTable = Table(
        ("value", "expectedResult"),
        (0.1f, 0.02447173f),
        (0.2f, 0.0954915f),
        (0.3f, 0.20610738f),
        (0.4f, 0.34549153f),
        (0.5f, 0.5f),
        (0.6f, 0.65450853f),
        (0.7f, 0.7938926f),
        (0.8f, 0.9045085f),
        (0.9f, 0.97552824f),
        (1.0f, 1.0f)
      )

      forAll(dataTable) { (value, expectedResult) =>
        When(s"The passed in value is $value")
        val easingValue = Easing.sine(value)

        Then(s"it should calculated correct value, which is ${expectedResult}")
        easingValue shouldBe expectedResult
      }
    }

  }
}
