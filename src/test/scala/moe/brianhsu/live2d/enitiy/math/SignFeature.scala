package moe.brianhsu.live2d.enitiy.math

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class SignFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  Feature("Calculate the sign of a float value") {
    Scenario("The value is negative value") {
      When("calculate the sign of -123.4f")
      val sign = Sign(-123.4f)

      Then("it should be a Negative")
      sign shouldBe Negative
    }

    Scenario("The value is zero") {
      When("calculate the sign of 0.0f")
      val sign = Sign(0.0f)

      Then("it should be a Neutral")
      sign shouldBe Neutral
    }

    Scenario("The value is a positive value") {
      When("calculate the sign of 123.4f")
      val sign = Sign(123.4f)

      Then("it should be a Positive")
      sign shouldBe Positive
    }

  }
}
