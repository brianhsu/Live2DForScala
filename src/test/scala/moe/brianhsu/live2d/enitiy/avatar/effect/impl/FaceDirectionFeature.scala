package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.updater.ParameterValueAdd
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class FaceDirectionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  private val model: Live2DModel = mock[Live2DModel]

  Feature("Calculate the face direction parameter operations") {
    Scenario("Update parameters according to DirectionCalculator") {
      Given("A mocked direction calculator")
      val directionCalculator = mock[FaceDirectionCalculator]

      (directionCalculator.updateFrameTimeInfo _).expects(1.0f, 33.0f)
      (() => directionCalculator.currentFaceCoordinate).expects().returning((0.2f, 0.5f))

      And("a FaceDirection effect")
      val faceDirection = new FaceDirection(directionCalculator)

      When("calculate operation")
      val operations = faceDirection.calculateOperations(model, 1.0f, 33.0f)

      Then("it should return correct operations")
      operations should contain theSameElementsInOrderAs List(
        ParameterValueAdd("ParamAngleX", 6.0f),
        ParameterValueAdd("ParamAngleY", 15.0f),
        ParameterValueAdd("ParamAngleY", -3.0f),
        ParameterValueAdd("ParamBodyAngleX", 2.0f),
        ParameterValueAdd("ParamEyeBallX", 0.2f),
        ParameterValueAdd("ParamEyeBallY", 0.5f)
      )
    }

  }
}
