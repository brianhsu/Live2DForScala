package moe.brianhsu.live2d.enitiy.math

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class RadianFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  Feature("Conversion between different presentation") {
    Scenario("Convert radian to direction") {
      Given("A radian of Pi/2")
      val radian = 2 / Math.PI.toFloat

      When("Calculate the direction")
      val direction = Radian.radianToDirection(radian)

      Then("the direction should be [0.59448075,0.8041099]")
      direction shouldBe EuclideanVector(0.59448075f, 0.8041099f)
    }

    Scenario("Convert degrees to radian") {
      Given("A 45 degrees")
      val degree = 45f

      When("Calculate the radian")
      val radian = Radian.degreesToRadian(degree)

      Then("the radian should be (45 / 180) * PI = 0.7853982")
      radian shouldBe 0.7853982f
    }

    Scenario("Convert direction to radian") {

      val table = Table(
        ("fromX", "fromY", "toX", "toY", "expectedRadian"),
        (-7.7418494225f, 6.8967475891f, -5.2785611153f, -0.4705476761f, 0.8166394234f),
        (5.9953489304f, -9.1437149048f, -4.0759410858f, 0.3384246826f, -2.2339940071f),
        (-7.7543020248f, -1.7547092438f, 3.0471973419f, 7.3717079163f, -2.1853108406f),
        (-2.4772644043f, 5.9745445251f, 3.3241186142f, -7.9970016479f, -3.1407105923f),
        (-3.0989208221f, 7.5104064941f, -8.2099905014f, -3.3221697807f, 1.5639691353f),
        (1.7601766586f, -0.8896055222f, -1.9164009094f, 3.8149881363f, 2.5042738914f),
        (-2.0231509209f, -4.1363835335f, 5.8012351990f, -4.1380586624f, 1.4060940742f),
        (-1.8703069687f, 2.6639375687f, 0.1647663116f, 0.4160194397f, -0.9892232418f),
      )

      forAll(table) { (fromX, fromY, toX, toY, expectedRadian) =>
        Given(s"a vector [$fromX, $fromY] to [$toX, $toY]")
        val from = EuclideanVector(fromX, fromY)
        val to = EuclideanVector(toX, toY)

        When("calculate radian")
        val radian = Radian.directionToRadian(from, to)

        Then(s"the result should be $expectedRadian")
        radian shouldBe expectedRadian

      }
    }
  }
}
