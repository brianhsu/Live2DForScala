package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.AddOperation
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class BreathFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Calculate the breath parameter operations") {
    Scenario("There is no breath parameter passed in") {
      When("The passed in breath parameter is an empty list")
      val breath = new Breath(Nil)

      Then("it should return an empty list at any given time")
      breath.calculateOperations(0, 0) shouldBe Nil
      breath.calculateOperations(0.33f, 0.33f) shouldBe Nil
      breath.calculateOperations(0.66f, 0.33f) shouldBe Nil
      breath.calculateOperations(0.99f, 0.33f) shouldBe Nil
    }

    Scenario("Use default breath parameters") {
      When("Using default breath parameters")
      val breath = new Breath()

      Then("it should return correct operation at first frame")
      breath.calculateOperations(0, 0) should contain theSameElementsInOrderAs List(
        AddOperation("ParamAngleX", 0.0f, 0.5f),
        AddOperation("ParamAngleY", 0.0f, 0.5f),
        AddOperation("ParamAngleZ", 0.0f, 0.5f),
        AddOperation("ParamBodyAngleX", 0.0f, 0.5f),
        AddOperation("ParamBreath", 0.5f, 0.5f)
      )

      Then("it should return correct operation at second frame")
      breath.calculateOperations(0.33f, 0.33f) should contain theSameElementsInOrderAs List(
        AddOperation("ParamAngleX", 4.680156f, 0.5f),
        AddOperation("ParamAngleY", 4.428475f, 0.5f),
        AddOperation("ParamAngleZ", 3.6593857f, 0.5f),
        AddOperation("ParamBodyAngleX", 0.53231204f, 0.5f),
        AddOperation("ParamBreath", 0.7990156f, 0.5f)
      )

      Then("it should return correct operation at third frame")
      breath.calculateOperations(0.66f, 0.33f) should contain theSameElementsInOrderAs List(
        AddOperation("ParamAngleX", 8.893033f, 0.5f),
        AddOperation("ParamAngleY", 7.376154f, 0.5f),
        AddOperation("ParamAngleZ", 6.8111343f, 0.5f),
        AddOperation("ParamBodyAngleX", 1.0551548f, 0.5f),
        AddOperation("ParamBreath", 0.97930574f, 0.5f)
      )

    }

  }
}
