package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.live2d.enitiy.avatar.motion.data.SegmentEvaluation.{BezierEvaluate, BezierEvaluateCardanoInterpretation, InverseSteppedEvaluate, LinearEvaluate, SteppedEvaluate}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class SegmentEvaluationFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {
  Feature("LinearEvaluate") {
    Scenario("The time of first point is before second point") {
      Given("an array of MotionPoint that has two point")
      val points = Array(
        MotionPoint(time = 1.2f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f)
      )

      When("calculate the value using LinearEvaluate with time = 0")
      val value1 = LinearEvaluate(points, 0)
      Then("it should return the first point's value")
      value1 shouldBe 3.4f

      When("calculate the value using LinearEvaluate with time = 1.5")
      val value2 = LinearEvaluate(points, 1.5f)
      Then("it should return the correct value")
      value2 shouldBe 3.7f

    }

    Scenario("The time of first point is after second point") {
      Given("an array of MotionPoint that has two point")
      val points = Array(
        MotionPoint(time = 5.6f, value = 3.4f),
        MotionPoint(time = 1.2f, value = 7.8f)
      )

      When("calculate the value using LinearEvaluate with time = 0")
      val value1 = LinearEvaluate(points, 0)
      Then("it should return the first point's value")
      value1 shouldBe 9.0f

      When("calculate the value using LinearEvaluate with time = 1.5")
      val value2 = LinearEvaluate(points, 1.5f)
      Then("it should return the correct value")
      value2 shouldBe 7.5000005f

    }

  }

  Feature("BezierEvaluate") {
    Scenario("The time of first point is before 4th point") {
      Given("an array of MotionPoint that has four point")
      val points = Array(
        MotionPoint(time = 1.2f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f),
        MotionPoint(time = 9.0f, value = 1.2f),
        MotionPoint(time = 3.4f, value = 5.6f)
      )

      When("calculate the value using BezierEvaluate with time = 0")
      val value1 = BezierEvaluate(points, 0)
      Then("it should return the first point's value")
      value1 shouldBe 3.4f

      When("calculate the value using BezierEvaluate with time = 2.0")
      val value2 = BezierEvaluate(points, 2.0f)
      Then("it should return the correct value")
      value2 shouldBe 4.8942146f
    }

    Scenario("The time of first point is after 4th point") {
      Given("an array of MotionPoint that has four point")
      val points = Array(
        MotionPoint(time = 3.4f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f),
        MotionPoint(time = 9.0f, value = 1.2f),
        MotionPoint(time = 1.2f, value = 5.6f)
      )

      When("calculate the value using BezierEvaluate with time = 0")
      val value1 = BezierEvaluate(points, 0)
      Then("it should return the first point's value")
      value1 shouldBe 26.188427f

      When("calculate the value using BezierEvaluate with time = 1.5")
      val value2 = BezierEvaluate(points, 1.5f)
      Then("it should return the correct value")
      value2 shouldBe 4.3578515f
    }

  }
  Feature("BezierEvaluateCardanoInterpretation") {
    Scenario("The time of first point is before 4th point") {
      Given("an array of MotionPoint that has four point")
      val points = Array(
        MotionPoint(time = 1.2f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f),
        MotionPoint(time = 9.0f, value = 1.2f),
        MotionPoint(time = 3.4f, value = 5.6f)
      )

      When("calculate the value using BezierEvaluateCardanoInterpretation with time = 0")
      val value1 = BezierEvaluateCardanoInterpretation(points, 0)
      Then("it should return the first point's value")
      value1 shouldBe 3.4f

      When("calculate the value using BezierEvaluateCardanoInterpretation with time = 2.0")
      val value2 = BezierEvaluateCardanoInterpretation(points, 2.0f)
      Then("it should return the correct value")
      value2 shouldBe 4.09314f
    }

    Scenario("The time of first point is after 4th point") {
      Given("an array of MotionPoint that has four point")
      val points = Array(
        MotionPoint(time = 3.4f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f),
        MotionPoint(time = 9.0f, value = 1.2f),
        MotionPoint(time = 1.2f, value = 5.6f)
      )

      When("calculate the value using BezierEvaluateCardanoInterpretation with time = 0")
      val value1 = BezierEvaluateCardanoInterpretation(points, 0)
      Then("it should return the first point's value")
      value1 shouldBe 5.5999994f

      When("calculate the value using BezierEvaluateCardanoInterpretation with time = 1.5")
      val value2 = BezierEvaluateCardanoInterpretation(points, 1.5f)
      Then("it should return the correct value")
      value2 shouldBe 5.433134f
    }

  }

  Feature("SteppedEvaluate") {
    Scenario("calculate the value") {
      Given("an array of MotionPoint that has two point")
      val points = Array(
        MotionPoint(time = 1.2f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f)
      )

      When("calculate the value using SteppedEvaluate with time = 0")
      val value1 = SteppedEvaluate(points, 0)

      Then("the returned value should be the first point's value")
      value1 shouldBe points(0).value

      When("calculate the value using SteppedEvaluate with time = 1")
      val value2 = SteppedEvaluate(points, 1)

      Then("the returned value should still be the first point's value")
      value2 shouldBe points(0).value
    }
  }

  Feature("InverseSteppedEvaluate") {
    Scenario("calculate the value") {
      Given("an array of MotionPoint that has two point")
      val points = Array(
        MotionPoint(time = 1.2f, value = 3.4f),
        MotionPoint(time = 5.6f, value = 7.8f)
      )

      When("calculate the value using InverseSteppedEvaluate with time = 0")
      val value1 = InverseSteppedEvaluate(points, 0)

      Then("the returned value should be the second point's value")
      value1 shouldBe points(1).value

      When("calculate the value using InverseSteppedEvaluate with time = 1")
      val value2 = InverseSteppedEvaluate(points, 1)

      Then("the returned value should still be the second point's value")
      value2 shouldBe points(1).value
    }
  }

}
