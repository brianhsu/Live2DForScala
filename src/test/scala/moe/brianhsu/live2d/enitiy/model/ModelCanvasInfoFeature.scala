package moe.brianhsu.live2d.enitiy.model

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ModelCanvasInfoFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Validation of pixelPerUnit") {
    Scenario("pixelPerUnit is equal to zero") {

      Given("A CanvasInfo with pixelPerUnit is 0")
      Then("it should throw an AssertionError")
      val error = the[AssertionError] thrownBy {
        ModelCanvasInfo(0, 0, (0, 0), 0)
      }
      And("should have message pixelPerUnit should > 0")
      error should have message "assertion failed: pixelPerUnit should > 0"
    }

    Scenario("pixelPerUnit is less then zero") {

      Given("A CanvasInfo with pixelPerUnit is -1")
      Then("it should throw an AssertionError")
      val error = the[AssertionError] thrownBy {
        ModelCanvasInfo(0, 0, (0, 0), -1)
      }
      And("should have message pixelPerUnit should > 0")
      error should have message "assertion failed: pixelPerUnit should > 0"
    }
  }

  Feature("Calculate the height / width in unit of a canvas") {
    Scenario("The pixelPerUnit=1") {
      Given("A CanvasInfo with pixelPerUnit=1")
      val canvasInfo = ModelCanvasInfo(
        widthInPixel = 1920, heightInPixel = 1080,
        originInPixel = (0, 0),
        pixelPerUnit = 1
      )

      When("request the width / height of that canvas")
      val width = canvasInfo.width
      val height = canvasInfo.height

      Then("they should equal to widthInPixel / heightInPixel")
      width shouldBe 1920
      height shouldBe 1080
    }

    Scenario("The pixelPerUnit=2") {
      Given("A CanvasInfo with pixelPerUnit=2")
      val canvasInfo = ModelCanvasInfo(
        widthInPixel = 1920, heightInPixel = 1080,
        originInPixel = (0, 0),
        pixelPerUnit = 2
      )

      When("request the width / height of that canvas")
      val width = canvasInfo.width
      val height = canvasInfo.height

      Then("they should equal to widthInPixel / heightInPixel in half")
      width shouldBe 960
      height shouldBe 540
    }

  }
}
