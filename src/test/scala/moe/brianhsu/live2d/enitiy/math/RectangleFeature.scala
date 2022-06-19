package moe.brianhsu.live2d.enitiy.math

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class RectangleFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Basic attributes") {
    Scenario("Access the attributes of a Rectangle") {
      Given("a rectangle object")
      val rectangle = Rectangle(1.0f, 2.0f, 3.0f, 4.0f)

      Then("it should have correct boundary attributes")
      rectangle.leftX shouldBe 1.0f
      rectangle.rightX shouldBe 4.0f
      rectangle.bottomY shouldBe 2.0f
      rectangle.topY shouldBe 6.0f

      And("correct size")
      rectangle.width shouldBe 3.0f
      rectangle.height shouldBe 4.0f

    }
  }

  Feature("Create new Rectangle by expend it") {
    Scenario("Expend a rectangle") {
      Given("a rectangle object")
      val rectangle = Rectangle(1.0f, 2.0f, 3.0f, 4.0f)

      When("expend it")
      val expended = rectangle.expand(7.0f, 9.0f)

      Then("expended rectangle should have correct attributes")
      expended.leftX shouldBe -6.0f
      expended.rightX shouldBe 11.0f
      expended.bottomY shouldBe -7.0f
      expended.topY shouldBe 15.0f
      expended.width shouldBe 17.0f
      expended.height shouldBe 22.0f
    }
  }
}
