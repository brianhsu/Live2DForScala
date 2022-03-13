package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.{ParameterValueAdd, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class MotionWithTransitionFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  private val model: Live2DModel = mock[Live2DModel]

  Feature("Delegated to base motion but with modified weight") {
    Scenario("Base motion does not have duration and no fading") {
      Given("A base Motion without duration and has not fade in / fade out")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(None)
      (() => baseMotion.fadeInTimeInSeconds).when().returning(0f)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(0f)

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *).returning(Nil)

      And("it will return a mocked list of operations at second frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      Then("it should correctly delegated calculateOperations to base motion")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f) shouldBe Nil
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f) shouldBe mockedOperations

      And("it should calculate correct new weight")
      val expectedWeightFor1stFrame = 1.0f
      val expectedWeightFor2ndFrame = 1.0f
      (baseMotion.calculateOperations _).verify(model, 0f, 0f, expectedWeightFor1stFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.33f, 0.33f, expectedWeightFor2ndFrame).once().returning(Nil)
    }

    Scenario("Base motion does not have duration but has fading time") {
      Given("A base Motion without duration and has fade in / fade out time as 1.5 / 2.0 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(None)
      (() => baseMotion.fadeInTimeInSeconds).when().returning(1.5f)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(2.0f)

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *).returning(Nil)

      And("it will return a mocked list of operations at second frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *).returning(mockedOperations)

      When("create a MotionWithTransition based on this motion")
      val transitionMotion = new MotionWithTransition(baseMotion)

      Then("it should correctly delegated calculateOperations to base motion")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f) shouldBe Nil
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f) shouldBe mockedOperations

      And("it should calculate correct new weight")
      val expectedWeightFor1stFrame = 0.0f
      val expectedWeightFor2ndFrame = 0.11474338f
      (baseMotion.calculateOperations _).verify(model, 0f, 0f, expectedWeightFor1stFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.33f, 0.33f, expectedWeightFor2ndFrame).once().returning(Nil)
    }

    Scenario("Base motion have duration 1 seconds and has fading time") {
      Given("A base Motion with 1 second duration and has fade in / fade out time as 1.5 / 2.0 second")
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(1.5f)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(2.0f)

      And("it will return Nil when first time it's operation being calculated")
      (baseMotion.calculateOperations _).when(model, 0f, 0f, *).returning(Nil)

      And("it will return a mocked list of operations at following frame")
      val mockedOperations = List(ParameterValueAdd("id1", 0.1f), ParameterValueUpdate("id2", 0.2f))
      (baseMotion.calculateOperations _).when(model, 0.33f, 0.33f, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.66f, 0.33f, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 0.99f, 0.33f, *).returning(mockedOperations)
      (baseMotion.calculateOperations _).when(model, 1.32f, 0.33f, *).returning(mockedOperations)

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

      (baseMotion.calculateOperations _).verify(model, 0f, 0f, expectedWeightFor1stFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.33f, 0.33f, expectedWeightFor2ndFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.66f, 0.33f, expectedWeightFor3rdFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 0.99f, 0.33f, expectedWeightFor4thFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 1.32f, 0.33f, expectedWeightFor5thFrame).once().returning(Nil)
      (baseMotion.calculateOperations _).verify(model, 1.65f, 0.33f, *).never()
    }
  }

  Feature("Motion event and event callback handling") {
    Scenario("Not fire any events yet") {
      Given("A base Motion with 1 second duration and has two defined event, at 0.34 and 0.67 seconds")
      val a = List(MotionEvent("event1", 0.34f), MotionEvent("event2", 0.67f))
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(0.0f)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(0.0f)
      (() => baseMotion.events).when().returning(a)

      println(baseMotion.events)

      And("a stubbed event callback")
      val mockedCallback: Callback = stub[Callback]

      And("create a MotionWithTransition based on this motion and set event callback")
      val transitionMotion = new MotionWithTransition(baseMotion)
      transitionMotion.setEventCallback(mockedCallback)

      And("calculate first two frame's operation at 0.0, 0.33f seconds")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)

      Then("no callback should not be called")
      (mockedCallback.apply _).verify(*, *).never()
    }

    Scenario("Fire only first events") {
      Given("A base Motion with 1 second duration and has two defined event, at 0.34 and 0.67 seconds")
      val a = List(MotionEvent("event1", 0.34f), MotionEvent("event2", 0.67f))
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(0.0f)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(0.0f)
      (() => baseMotion.events).when().returning(a)

      println(baseMotion.events)

      And("a stubbed event callback")
      val mockedCallback: Callback = stub[Callback]

      And("create a MotionWithTransition based on this motion and set event callback")
      val transitionMotion = new MotionWithTransition(baseMotion)
      transitionMotion.setEventCallback(mockedCallback)

      And("calculate first three frame's operation at 0.0, 0.33, 0.66 seconds")
      transitionMotion.calculateOperations(model, 0, 0, 1.0f)
      transitionMotion.calculateOperations(model, 0.33f, 0.33f, 1.0f)
      transitionMotion.calculateOperations(model, 0.66f, 0.33f, 1.0f)

      Then("callback should be called on first event")
      (mockedCallback.apply _).verify(transitionMotion, MotionEvent("event1", 0.34f)).once()
    }

    Scenario("Fire all events") {
      Given("A base Motion with 1 second duration and has two defined event, at 0.34 and 0.67 seconds")
      val a = List(MotionEvent("event1", 0.34f), MotionEvent("event2", 0.67f))
      val baseMotion = stub[Motion]
      (() => baseMotion.durationInSeconds).when().returning(Some(1))
      (() => baseMotion.fadeInTimeInSeconds).when().returning(0.0f)
      (() => baseMotion.fadeOutTimeInSeconds).when().returning(0.0f)
      (() => baseMotion.events).when().returning(a)

      println(baseMotion.events)

      And("a stubbed event callback")
      val mockedCallback: Callback = stub[Callback]

      And("create a MotionWithTransition based on this motion and set event callback")
      val transitionMotion = new MotionWithTransition(baseMotion)
      transitionMotion.setEventCallback(mockedCallback)

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


}
