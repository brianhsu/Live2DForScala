package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.effect.{AddOperation, UpdateOperation}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class FaceDirectionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory{

  Feature("Calculate the face direction parameter operations") {
    Scenario("Update parameters according to DirectionCalculator") {
      Given("A mocked direction calculator")
      val directionCalculator = mock[FaceDirectionCalculator]
      (directionCalculator.updateFrameTimeInfo _).expects(1.0f, 33.0f)
      (directionCalculator.currentFaceCoordinate _).expects().returning((0.2f, 0.5f))

      And("a FaceDirection effect")
      val faceDirection = new FaceDirection(directionCalculator)

      When("calculate operation")
      val operations = faceDirection.calculateOperations(1.0f, 33.0f)

      Then("it should return correct operations")
      operations should contain theSameElementsInOrderAs List(
        AddOperation("ParamAngleX", 6.0f),
        AddOperation("ParamAngleY", 15.0f),
        AddOperation("ParamAngleY", -3.0f),
        AddOperation("ParamBodyAngleX", 2.0f),
        UpdateOperation("ParamEyeBallX", 0.2f),
        UpdateOperation("ParamEyeBallY", 0.5f)
      )
    }

  }
}
