package moe.brianhsu.live2d.enitiy.math

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class EuclideanVectorFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Arithmetic operations") {
    Scenario("Add two vector") {
      Given("a [123.4, 456.7] and a [432.1, 678.9] vector")
      val a = EuclideanVector(123.4f, 456.7f)
      val b = EuclideanVector(431.1f, 678.9f)

      When("calculate a + b")
      val c = a + b

      Then("the result should be [554.5, 1135.6001]")
      c shouldBe EuclideanVector(554.5f, 1135.6001f)

      When("calculate b + a")
      val d = b + a

      Then("the result should be [554.5, 1135.6001]")
      d shouldBe EuclideanVector(554.5f, 1135.6001f)

    }

    Scenario("Subtract two vector") {
      Given("a [123.4, 456.7] and a [432.1, 678.9] vector")
      val a = EuclideanVector(123.4f, 456.7f)
      val b = EuclideanVector(431.1f, 678.9f)

      When("calculate a - b")
      val c = a - b

      Then("the result should be [-307.7, -222.20001]")
      c shouldBe EuclideanVector(-307.7f, -222.20001f)

      When("calculate b - a")
      val d = b -a

      Then("the result should be [307.7, 222.20001]")
      d shouldBe EuclideanVector(307.7f, 222.20001f)
    }

    Scenario("Multiple by a factor") {
      Given("a [123.4, 456.7] vector")
      val a = EuclideanVector(123.4f, 456.7f)

      When("calculate a * 5")
      val b = a * 5

      Then("the result should be [617.0, 2283.5]")
      b shouldBe EuclideanVector(617.0f, 2283.5f)
    }

    Scenario("Divided by a factor") {
      Given("a [123.4, 456.7] vector")
      val a = EuclideanVector(123.4f, 456.7f)

      When("calculate a / 5")
      val b = a / 5

      Then("the result should be [24.68, 91.340004]")
      b shouldBe EuclideanVector(24.68f, 91.340004f)
    }

    Scenario("Normalize a vector") {
      Given("a [123.4, 456.7] vector")
      val a = EuclideanVector(123.4f, 456.7f)

      When("normalize it")
      val normalized = a.normalize()

      Then("the result should be [0.26084512, 0.9653806]")
      normalized shouldBe EuclideanVector(0.26084512f, 0.9653806f)
    }

  }
}
