package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.{EventCallback, FinishedCallback}
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.{ParameterValueAdd, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class MotionWithTransitionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with OptionValues {

  private val model: Live2DModel = mock[Live2DModel]

  Feature("Delegated to base motion but with modified weight") {
    Scenario("Base motion does not have duration and no fading") {
      Given("a base Motion without duration and has not fade in / fade out")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(None)
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(0f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0f))

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *, *, *, *).returning(Nil)

      And("it will return a mocked list of operations at second frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *, *, *, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      Then("it should correctly delegated calculateOperations to base motion")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f) shouldBe Nil
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f) shouldBe mockedOperations

      And("it should calculate correct new weight")
      val expectedWeightFor1stFrame = 1.0f
      val expectedWeightFor2ndFrame = 1.0f
      (baseMotion.calculateOperations _).verify(model, 0f, 0f, expectedWeightFor1stFrame, 0f, 0f, None).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.33f, 0.33f, expectedWeightFor2ndFrame, 0f, 0f, None).once().returning(Nil)
    }

    Scenario("Base motion does not have duration but has fading time") {
      Given("a base Motion without duration and has fade in / fade out time as 1.5 / 2.0 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(None)
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(1.5f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(2.0f))

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *, *, *, *).returning(Nil)

      And("it will return a mocked list of operations at second frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *, *, *, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      Then("it should correctly delegated calculateOperations to base motion")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f) shouldBe Nil
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f) shouldBe mockedOperations

      And("it should calculate correct new weight")
      val expectedWeightFor1stFrame = 0.0f
      val expectedWeightFor2ndFrame = 0.11474338f
      (baseMotion.calculateOperations _).verify(model, 0f, 0f, expectedWeightFor1stFrame, 0f, 0f, None).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.33f, 0.33f, expectedWeightFor2ndFrame, 0f, 0f, None).once().returning(Nil)
    }

    Scenario("Base motion have duration 1 seconds and has fading time") {
      Given("a base Motion with 1 second duration and has fade in / fade out time as 1.5 / 2.0 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(1.5f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(2.0f))

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *, *, *, *).returning(Nil)

      And("it will return a mocked list of operations at following frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.66f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.99f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 1.32f, 0.33f, *, *, *, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      Then("it should correctly delegated calculateOperations to base motion and has correct isFinished value")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f) shouldBe Nil
      transitionMotion.isFinished shouldBe false

      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f) shouldBe mockedOperations
      transitionMotion.isFinished shouldBe false

      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f) shouldBe mockedOperations
      transitionMotion.isFinished shouldBe false

      transitionMotion.calculateOperations(model, 0.99f, 0.33f, 1.0f) shouldBe mockedOperations
      transitionMotion.isFinished shouldBe false

      transitionMotion.calculateOperations(model, 1.32f, 0.33f, 1.0f) shouldBe mockedOperations
      transitionMotion.isFinished shouldBe true

      And("if it's already passed the motion duration, it should just return operations just do nothing")
      transitionMotion.calculateOperations(model, 1.65f, 0.33f, 1.0f) shouldBe Nil
      transitionMotion.isFinished shouldBe true

      And("it should calculate correct new weight")
      val expectedWeightFor1stFrame = 0.0f
      val expectedWeightFor2ndFrame = 0.028946387f
      val expectedWeightFor3rdFrame = 0.02829091f
      val expectedWeightFor4thFrame = 0.00004570529199554585f
      val expectedWeightFor5thFrame = 0.0f

      (baseMotion.calculateOperations _).verify(model, 0f, 0f, expectedWeightFor1stFrame, 0f, 0f, Some(1.0f)).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.33f, 0.33f, expectedWeightFor2ndFrame, 0f, 0f, Some(1.0f)).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.66f, 0.33f, expectedWeightFor3rdFrame, 0f, 0f, Some(1.0f)).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.99f, 0.33f, expectedWeightFor4thFrame, 0f, 0f, Some(1.0f)).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 1.32f, 0.33f, expectedWeightFor5thFrame, 0f, 0f, Some(1.0f)).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 1.65f, 0.33f, *, 0f, 0f, Some(1.0f)).never()
    }
  }

  Feature("Finished event and callback handling") {
    Scenario("The motion is not finished") {
      Given("a base Motion with 1 second duration and has fade in / fade out time as 1.5 / 2.0 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(1.5f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(2.0f))

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *, *, *, *).returning(Nil)

      And("it will return a mocked list of operations at following frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.66f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.99f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 1.32f, 0.33f, *, *, *, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      And("set a event callback")
      val mockedCallback = stub[FinishedCallback]
      transitionMotion.finishedCallbackHolder = Some(mockedCallback)

      When("calculate operations before event is finished")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f)

      Then("the callback should not be called at all")
      (mockedCallback.apply _).verify(*).never()
    }

    Scenario("The motion is finished and triggered event callback") {
      Given("a base Motion with 1 second duration and has fade in / fade out time as 1.5 / 2.0 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(1.5f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(2.0f))

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *, *, *, *).returning(Nil)

      And("it will return a mocked list of operations at following frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.66f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.99f, 0.33f, *, *, *, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 1.32f, 0.33f, *, *, *, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      And("set a event callback")
      val mockedCallback = stub[FinishedCallback]
      transitionMotion.finishedCallbackHolder = Some(mockedCallback)

      When("calculate operations that make event finished")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.99f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 1.32f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 1.65f, 0.33f, 1.0f)

      Then("the callback should not be called at all")
      (mockedCallback.apply _).verify(*).once()
    }
  }

  Feature("Motion event and event callback handling") {
    Scenario("Not fire any events yet") {
      Given("a base Motion with 1 second duration and has two defined event, at 0.34 and 0.67 seconds")
      val eventList = List(MotionEvent("event1", 0.34f), MotionEvent("event2", 0.67f))
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(0.0f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.0f))
      (() => baseMotion.events).when().returning(eventList)

      And("a stubbed event callback")
      val mockedCallback: EventCallback = stub[EventCallback]

      And("create a MotionWithTransition based on this motion and set event callback")
      val transitionMotion = new MotionWithTransition(baseMotion)
      transitionMotion.eventCallbackHolder = Some(mockedCallback)

      And("calculate first two frame's operation at 0.0, 0.33f seconds")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)

      Then("no callback should not be called")
      (mockedCallback.apply _).verify(*, *).never()
    }

    Scenario("Fire only first events") {
      Given("a base Motion with 1 second duration and has two defined event, at 0.34 and 0.67 seconds")
      val eventList = List(MotionEvent("event1", 0.34f), MotionEvent("event2", 0.67f))
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(0.0f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.0f))
      (() => baseMotion.events).when().returning(eventList)

      And("a stubbed event callback")
      val mockedCallback: EventCallback = stub[EventCallback]

      And("create a MotionWithTransition based on this motion and set event callback")
      val transitionMotion = new MotionWithTransition(baseMotion)
      transitionMotion.eventCallbackHolder = Some(mockedCallback)

      And("calculate first three frame's operation at 0.0, 0.33, 0.66 seconds")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f)

      Then("callback should be called on first event")
      (mockedCallback.apply _).verify(transitionMotion, MotionEvent("event1", 0.34f)).once()
    }

    Scenario("Fire all events") {
      Given("a base Motion with 1 second duration and has two defined event, at 0.34 and 0.67 seconds")
      val eventList = List(MotionEvent("event1", 0.34f), MotionEvent("event2", 0.67f))
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(Some(0.0f))
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.0f))
      (() => baseMotion.events).when().returning(eventList)

      And("a stubbed event callback")
      val mockedCallback: EventCallback = stub[EventCallback]

      And("create a MotionWithTransition based on this motion and set event callback")
      val transitionMotion = new MotionWithTransition(baseMotion)
      transitionMotion.eventCallbackHolder = Some(mockedCallback)

      And("calculate first four frame's operation at 0.0, 0.33, 0.66, 0.99 seconds")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.99f, 0.33f, 1.0f)

      Then("callback should called on all events")
      (mockedCallback.apply _).verify(transitionMotion, MotionEvent("event1", 0.34f)).once()
      (mockedCallback.apply _).verify(transitionMotion, MotionEvent("event2", 0.67f)).once()

    }

  }

  Feature("Force fade out before planned fade out") {
    Scenario("Set force fade out flag") {
      Given("a MotionWithTransition")
      val motionWithTransition = new MotionWithTransition(mock[Motion])

      And("the default force fade out flag is false")
      motionWithTransition.isForceToFadeOut shouldBe false

      When("mark it as force fade out")
      motionWithTransition.markAsForceFadeOut()

      Then("the force fade out flag should be enabled")
      motionWithTransition.isForceToFadeOut shouldBe true
    }

    Scenario("Force fade out take place after the base motion is already fading out") {
      Given("a base motion with duration 2 seconds and fade out time 0.5 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(2))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(None)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.5f))

      And("a MotionWithTransition based on that motion")
      val motionWithTransition = new MotionWithTransition(baseMotion)

      And("start the motion at first frame")
      motionWithTransition.calculateOperations(model, 0, 0, 1.0f)

      And("the original end time should be 2.0")
      val originalEndTime = motionWithTransition.getEndTimeInSecondsForUnitTest
      originalEndTime.value shouldBe 2.0

      When("force fade out at 1.6 second, which is after original fade out time (2.0 - 0.5 = 1.5)")
      motionWithTransition.startFadeOut(1.6f)

      Then("the end time should not be touched")
      motionWithTransition.getEndTimeInSecondsForUnitTest shouldBe originalEndTime

    }

    Scenario("Force fade out take place before the base motion is already fading out") {
      Given("a base motion with duration 2 seconds and fade out time 0.5 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(2))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(None)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.5f))

      And("a MotionWithTransition based on that motion")
      val motionWithTransition = new MotionWithTransition(baseMotion)

      And("start the motion at first frame")
      motionWithTransition.calculateOperations(model, 0, 0, 1.0f)

      And("the original end time should be 2.0")
      val originalEndTime = motionWithTransition.getEndTimeInSecondsForUnitTest
      originalEndTime.value shouldBe 2.0

      When("force fade out at 0.5 second, which is before original fade out time (2.0 - 0.5 = 1.5)")
      val currentTotalElapsedTime = 0.6f
      motionWithTransition.startFadeOut(currentTotalElapsedTime)

      Then("the new end time should be current elapsed time + fade out time")
      val expectedNewEndTime = currentTotalElapsedTime + baseMotion.fadeOutTimeInSeconds.get
      motionWithTransition.getEndTimeInSecondsForUnitTest.value shouldBe expectedNewEndTime
    }

  }

  Feature("Update startTimeInSeconds / fadeInStartTimeInSeconds when loop") {
    Scenario("The base motion is loop but not loopFadeIn") {
      Given("a base motion with duration 2 seconds and fade out time 0.5 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(2))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(None)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.5f))
      (() => baseMotion.isLoop).when().returning(true)

      And("a MotionWithTransition based on that motion")
      val motionWithTransition = new MotionWithTransition(baseMotion)

      And("start the motion at first frame")
      motionWithTransition.calculateOperations(model, 0, 0, 1.0f)

      val originalStartTime = motionWithTransition.getStartTimeInSecondsForUnitTest
      val originalFadeInStartTime = motionWithTransition.getFadeInStartTimeInSecondsForUnitTest
      And("the original start time should be 0.0")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 0.0

      When("update the motion at 1.5 second")
      motionWithTransition.calculateOperations(model, 1.5f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe originalStartTime
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

      When("update the motion at 1.99 second")
      motionWithTransition.calculateOperations(model, 1.99f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe originalStartTime
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

      When("update the motion at 2 second")
      motionWithTransition.calculateOperations(model, 2f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should update to current time")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 2.0f
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

      When("update the motion at 2.5 second")
      motionWithTransition.calculateOperations(model, 2f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 2.0f
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

      When("update the motion at 4 second")
      motionWithTransition.calculateOperations(model, 4f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 4.0f
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

    }
    Scenario("The base motion is loop and loopFadeIn") {
      Given("a base motion with duration 2 seconds and fade out time 0.5 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(2))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(None)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(Some(0.5f))
      (() => baseMotion.isLoop).when().returning(true)
      (() => baseMotion.isLoopFadeIn).when().returning(true)

      And("a MotionWithTransition based on that motion")
      val motionWithTransition = new MotionWithTransition(baseMotion)

      And("start the motion at first frame")
      motionWithTransition.calculateOperations(model, 0, 0, 1.0f)

      val originalStartTime = motionWithTransition.getStartTimeInSecondsForUnitTest
      val originalFadeInStartTime = motionWithTransition.getFadeInStartTimeInSecondsForUnitTest
      And("the original start time should be 0.0")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 0.0

      When("update the motion at 1.5 second")
      motionWithTransition.calculateOperations(model, 1.5f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe originalStartTime
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

      When("update the motion at 1.99 second")
      motionWithTransition.calculateOperations(model, 1.99f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe originalStartTime
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe originalFadeInStartTime

      When("update the motion at 2 second")
      motionWithTransition.calculateOperations(model, 2f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should update to current time")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 2.0f
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe 2.0f

      When("update the motion at 2.5 second")
      motionWithTransition.calculateOperations(model, 2f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 2.0f
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe 2.0f

      When("update the motion at 4 second")
      motionWithTransition.calculateOperations(model, 4f, 0, 1.0f)

      Then("the startTime / fadeInStartTime should not be touched")
      motionWithTransition.getStartTimeInSecondsForUnitTest shouldBe 4.0f
      motionWithTransition.getFadeInStartTimeInSecondsForUnitTest shouldBe 4.0f

    }

  }


}
