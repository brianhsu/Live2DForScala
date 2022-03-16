package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.{ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression.{Add, Multiply, Overwrite, Parameter}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ExpressionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  private val model: Live2DModel = mock[Live2DModel]

  Feature("Calculate the Expression update operations") {
    Scenario("There is no expression parameters") {
      When("The passed in expression parameter is an empty list")
      val expression = Expression(None, None, Nil)

      Then("it should return an empty list at any given time")
      expression.calculateOperations(model, 0, 0, 1f, -1.0f, 0, None) shouldBe Nil
      expression.calculateOperations(model, 0.33f, 0.33f, 1, -1.0f, 0, None) shouldBe Nil
      expression.calculateOperations(model, 0.66f, 0.33f, 1, -1.0f, 0, None) shouldBe Nil
      expression.calculateOperations(model, 0.99f, 0.33f, 1, -1.0f, 0, None) shouldBe Nil
    }
    Scenario("There are expression parameters") {
      When("The passed in expression contains different blend type")
      val parameters = List(
        Parameter("id1", Add, 0.5f),
        Parameter("id2", Multiply, 0.6f),
        Parameter("id3", Overwrite, 0.7f),
        Parameter("id4", Add, 0.8f),
        Parameter("id5", Multiply, 0.9f),
        Parameter("id6", Overwrite, 1.0f)
      )
      val expression = Expression(None, None, parameters)

      Then("it should return corresponding operation list at any given time but respect weight")
      expression.calculateOperations(model, 0, 0, 0.1f, -1.0f, 0, None) should contain theSameElementsInOrderAs List(
        ParameterValueAdd("id1", 0.5f, 0.1f),
        ParameterValueMultiply("id2", 0.6f, 0.1f),
        ParameterValueUpdate("id3", 0.7f, 0.1f),
        ParameterValueAdd("id4", 0.8f, 0.1f),
        ParameterValueMultiply("id5", 0.9f, 0.1f),
        ParameterValueUpdate("id6", 1.0f, 0.1f),
      )
      expression.calculateOperations(model, 0.33f, 0.33f, 0.2f, -1.0f, 0, None) should contain theSameElementsInOrderAs List(
        ParameterValueAdd("id1", 0.5f, 0.2f),
        ParameterValueMultiply("id2", 0.6f, 0.2f),
        ParameterValueUpdate("id3", 0.7f, 0.2f),
        ParameterValueAdd("id4", 0.8f, 0.2f),
        ParameterValueMultiply("id5", 0.9f, 0.2f),
        ParameterValueUpdate("id6", 1.0f, 0.2f),
      )
      expression.calculateOperations(model, 0.66f, 0.33f, 0.3f, -1.0f, 0, None) should contain theSameElementsInOrderAs List(
        ParameterValueAdd("id1", 0.5f, 0.3f),
        ParameterValueMultiply("id2", 0.6f, 0.3f),
        ParameterValueUpdate("id3", 0.7f, 0.3f),
        ParameterValueAdd("id4", 0.8f, 0.3f),
        ParameterValueMultiply("id5", 0.9f, 0.3f),
        ParameterValueUpdate("id6", 1.0f, 0.3f),
      )
    }

  }
}
