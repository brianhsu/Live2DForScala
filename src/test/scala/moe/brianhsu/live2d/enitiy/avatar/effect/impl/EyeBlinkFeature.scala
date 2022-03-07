package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.UpdateOperation
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class EyeBlinkFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Calculate the eye blink parameter operations") {

    Scenario("Use default eye blink parameters without any randomness") {
      Given("An avatar setting contains two eye openness parameter")
      val eyeBlinkParameterIds = List("EyeOpen1", "EyeOpen2")
      val avatarSettings = Settings(null, Nil, None, eyeBlinkParameterIds, Map.empty, Map.empty, Nil)

      When("Using parameter without randomness")
      val eyeBlink = new EyeBlink(avatarSettings, EyeBlink.Parameters(4.0f, 1.0f, 0.1f, 0.05f, 0.15f))

      And("Calculate operation of first frame (EyeBlink.Init -> EyeBlink.Interval)")
      val firstFrameOperation = eyeBlink.calculateOperations(0, 0.33f)
      firstFrameOperation should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 1.0f), UpdateOperation("EyeOpen2",1.0f)
      )

      And("Calculate operation after 2 second (EyeBlink.Interval -> EyeBlink.Interval)")
      val operationAt2Second = eyeBlink.calculateOperations(2.0f, 0.33f)
      operationAt2Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 1.0f), UpdateOperation("EyeOpen2",1.0f)
      )

      And("Calculate operation after 7.1f second (EyeBlink.Interval -> EyeBlink.Closing)")
      val operationAt7_1Second = eyeBlink.calculateOperations(7.5f, 0.33f)
      operationAt7_1Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 1.0f), UpdateOperation("EyeOpen2",1.0f)
      )

      And("Calculate operation after 7.235 second (EyeBlink.Closing -> EyeBlink.Closing)")
      val operationAt7_235Second = eyeBlink.calculateOperations(7.235f, 0.33f)
      operationAt7_235Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 3.6499987f), UpdateOperation("EyeOpen2", 3.6499987f)
      )

      And("Calculate operation after 7.63 second (EyeBlink.Closing -> EyeBlink.Closed)")
      val operationAt7_63Second = eyeBlink.calculateOperations(7.63f, 0.33f)
      operationAt7_63Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 0.0f), UpdateOperation("EyeOpen2", 0.0f)
      )

      And("Calculate operation after 7.65 second (EyeBlink.Closed -> EyeBlink.Closed)")
      val operationAt7_65Second = eyeBlink.calculateOperations(7.65f, 0.33f)
      operationAt7_65Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 0.0f), UpdateOperation("EyeOpen2", 0.0f)
      )

      And("Calculate operation after 7.7 second (EyeBlink.Closed -> EyeBlink.Opening)")
      val operationAt7_7Second = eyeBlink.calculateOperations(7.7f, 0.33f)
      operationAt7_7Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 0.0f), UpdateOperation("EyeOpen2", 0.0f)
      )

      And("Calculate operation after 7.75 second (EyeBlink.Opening -> EyeBlink.Opening)")
      val operationAt7_75Second = eyeBlink.calculateOperations(7.75f, 0.33f)
      operationAt7_75Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 0.3333346f), UpdateOperation("EyeOpen2", 0.3333346f)
      )

      And("Calculate operation after 7.9 second (EyeBlink.Opening -> EyeBlink.Interval)")
      val operationAt7_79Second = eyeBlink.calculateOperations(7.9f, 0.33f)
      operationAt7_79Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 1.0f), UpdateOperation("EyeOpen2", 1.0f)
      )

      And("Calculate operation after 8 second (EyeBlink.Interval -> EyeBlink.Interval)")
      val operationAt7_8Second = eyeBlink.calculateOperations(8.0f, 0.33f)
      operationAt7_8Second should contain theSameElementsInOrderAs List(
        UpdateOperation("EyeOpen1", 1.0f), UpdateOperation("EyeOpen2", 1.0f)
      )

    }

  }
}
