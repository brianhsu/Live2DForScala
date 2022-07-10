package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceTracking.{TrackingNode, TrackingTaps}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueUpdate
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class FaceTrackingFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  private val model: Live2DModel = mock[Live2DModel]

  Feature("Calculate the face tracking operations") {
    Scenario("There is no face tracking node exists") {
      When("the FaceTracking does not have any exist tacking node")
      val faceTracking = new FixedFaceTracking(Nil)

      Then("it should return an empty list at any given time")
      faceTracking.calculateOperations(model, 0, 0) shouldBe Nil
    }

    Scenario("There are 12 tracking nodes") {
      Given("12 tracking nodes")
      val trackingNodes = List(
        TrackingNode(0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.09f),
        TrackingNode(0.11f, 0.12f, 0.13f, 0.14f, 0.15f, 0.16f, 0.17f, 0.18f, 0.19f),
        TrackingNode(0.21f, 0.22f, 0.23f, 0.24f, 0.25f, 0.26f, 0.27f, 0.28f, 0.29f),
        TrackingNode(0.31f, 0.32f, 0.23f, 0.34f, 0.35f, 0.36f, 0.37f, 0.38f, 0.39f),
        TrackingNode(0.41f, 0.42f, 0.43f, 0.44f, 0.45f, 0.46f, 0.47f, 0.48f, 0.49f),
        TrackingNode(0.51f, 0.52f, 0.53f, 0.54f, 0.55f, 0.56f, 0.57f, 0.58f, 0.59f),
        TrackingNode(0.61f, 0.62f, 0.63f, 0.64f, 0.65f, 0.66f, 0.67f, 0.68f, 0.69f),
        TrackingNode(0.71f, 0.72f, 0.73f, 0.74f, 0.75f, 0.76f, 0.77f, 0.78f, 0.79f),
        TrackingNode(0.81f, 0.82f, 0.83f, 0.84f, 0.85f, 0.86f, 0.87f, 0.88f, 0.89f),
        TrackingNode(0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f),
        TrackingNode(1.01f, 1.02f, 1.03f, 1.04f, 1.05f, 0.16f, 1.07f, 1.08f, 1.09f),
        TrackingNode(1.11f, 1.12f, 1.13f, 1.14f, 1.15f, 1.16f, 1.17f, 1.18f, 1.19f),
      )

      And("a FaceTracking based on those tracking nodes")
      val faceTracking = new FixedFaceTracking(trackingNodes)

      Then("it should return an list contains the expected operations at any given time")
      val expectedOperations = List(
        ParameterValueUpdate("ParamAngleX", 0.10999999f),
        ParameterValueUpdate("ParamAngleY", 0.17f),
        ParameterValueUpdate("ParamAngleZ", 0.21f),
        ParameterValueUpdate("ParamEyeLOpen", 0.29f),
        ParameterValueUpdate("ParamEyeROpen", 0.34999996f),
        ParameterValueUpdate("ParamMouthOpenY", 0.41000003f),
        ParameterValueUpdate("ParamMouthForm", 0.47f),
        ParameterValueUpdate("ParamEyeLSmile", 0.53000003f),
        ParameterValueUpdate("ParamEyeRSmile", 0.59f)
      )

      faceTracking.calculateOperations(model, 0, 0) should contain theSameElementsInOrderAs expectedOperations
      faceTracking.calculateOperations(model, 0.33f, 0.33f) should contain theSameElementsInOrderAs expectedOperations
      faceTracking.calculateOperations(model, 0.66f, 0.33f) should contain theSameElementsInOrderAs expectedOperations
    }
  }

  private val DefaultFaceTrackingTaps = TrackingTaps(3, 4, 5, 6, 7, 8, 9, 10, 11, 11)

  class FixedFaceTracking(currentTrackingNoes: List[TrackingNode]) extends FaceTracking(DefaultFaceTrackingTaps) {
    this.trackingNoes = currentTrackingNoes
  }

}
