package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.ParameterValueUpdate
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.scalamock.scalatest.proxy.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class EyeBlinkFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  private val model: Live2DModel = null

  Feature("Calculate the eye blink parameter operations") {

    Scenario("Use default eye blink parameters without any randomness") {
      Given("An avatar setting contains two eye openness parameter")
      val eyeBlinkParameterIds = List("EyeOpen1", "EyeOpen2")
      val avatarSettings = Settings(null, Nil, None, eyeBlinkParameterIds, Map.empty, Map.empty, Nil)

      When("Using parameter without randomness")
      val eyeBlink = new EyeBlink(avatarSettings, EyeBlink.Parameters(4.0f, () => 1.0f, 0.1f, 0.05f, 0.15f))

      And("Calculate operation of first frame (EyeBlink.Init -> EyeBlink.Interval)")
      val firstFrameOperation = eyeBlink.calculateOperations(model, 0, 0.33f)
      firstFrameOperation should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 1.0f), ParameterValueUpdate("EyeOpen2",1.0f)
      )

      And("Calculate operation after 2 second (EyeBlink.Interval -> EyeBlink.Interval)")
      val operationAt2Second = eyeBlink.calculateOperations(model, 2.0f, 0.33f)
      operationAt2Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 1.0f), ParameterValueUpdate("EyeOpen2",1.0f)
      )

      And("Calculate operation after 7.1f second (EyeBlink.Interval -> EyeBlink.Closing)")
      val operationAt7_1Second = eyeBlink.calculateOperations(model, 7.5f, 0.33f)
      operationAt7_1Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 1.0f), ParameterValueUpdate("EyeOpen2",1.0f)
      )

      And("Calculate operation after 7.235 second (EyeBlink.Closing -> EyeBlink.Closing)")
      val operationAt7_235Second = eyeBlink.calculateOperations(model, 7.235f, 0.33f)
      operationAt7_235Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 3.6499987f), ParameterValueUpdate("EyeOpen2", 3.6499987f)
      )

      And("Calculate operation after 7.63 second (EyeBlink.Closing -> EyeBlink.Closed)")
      val operationAt7_63Second = eyeBlink.calculateOperations(model, 7.63f, 0.33f)
      operationAt7_63Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 0.0f), ParameterValueUpdate("EyeOpen2", 0.0f)
      )

      And("Calculate operation after 7.65 second (EyeBlink.Closed -> EyeBlink.Closed)")
      val operationAt7_65Second = eyeBlink.calculateOperations(model, 7.65f, 0.33f)
      operationAt7_65Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 0.0f), ParameterValueUpdate("EyeOpen2", 0.0f)
      )

      And("Calculate operation after 7.7 second (EyeBlink.Closed -> EyeBlink.Opening)")
      val operationAt7_7Second = eyeBlink.calculateOperations(model, 7.7f, 0.33f)
      operationAt7_7Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 0.0f), ParameterValueUpdate("EyeOpen2", 0.0f)
      )

      And("Calculate operation after 7.75 second (EyeBlink.Opening -> EyeBlink.Opening)")
      val operationAt7_75Second = eyeBlink.calculateOperations(model, 7.75f, 0.33f)
      operationAt7_75Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 0.3333346f), ParameterValueUpdate("EyeOpen2", 0.3333346f)
      )

      And("Calculate operation after 7.9 second (EyeBlink.Opening -> EyeBlink.Interval)")
      val operationAt7_79Second = eyeBlink.calculateOperations(model, 7.9f, 0.33f)
      operationAt7_79Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 1.0f), ParameterValueUpdate("EyeOpen2", 1.0f)
      )

      And("Calculate operation after 8 second (EyeBlink.Interval -> EyeBlink.Interval)")
      val operationAt7_8Second = eyeBlink.calculateOperations(model, 8.0f, 0.33f)
      operationAt7_8Second should contain theSameElementsInOrderAs List(
        ParameterValueUpdate("EyeOpen1", 1.0f), ParameterValueUpdate("EyeOpen2", 1.0f)
      )

    }

  }
}
