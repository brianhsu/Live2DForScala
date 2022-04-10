package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.{EventCallback, FinishedCallback}
import moe.brianhsu.live2d.enitiy.avatar.motion.Motion
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation.{ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate}
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, OptionValues}

class MotionManagerFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with OptionValues {

  private val model: Live2DModel = mock[Live2DModel]

  Feature("Start motion") {
    Scenario("Start base Motion") {
      Given("a base motion and a motion manager")
      val motion = stub[Motion]
      val motionManager = new MotionManager

      When("start the base motion")
      val transformedMotion = motionManager.startMotion(motion)

      Then("it should return a MotionWithTransition")
      transformedMotion shouldBe a[MotionWithTransition]

      And("the base motion should be the motion passed in to manager")
      transformedMotion.baseMotion shouldBe motion
    }

    Scenario("Start a MotionWithTransition") {
      Given("a MotionWithTransition and a motion manager")
      val motionWithTransition = new MotionWithTransition(stub[Motion])
      val motionManager = new MotionManager

      When("start the base motion")
      val transformedMotion = motionManager.startMotion(motionWithTransition)

      Then("it should return a MotionWithTransition")
      transformedMotion shouldBe a[MotionWithTransition]

      And("the returned motion should be the same motion we passed into manager")
      transformedMotion shouldBe motionWithTransition

    }

    Scenario("Start multiple MotionWithTransition") {
      Given("several MotionWithTransition and a motion manager")
      val motionWithTransition1 = stub[MotionWithTransition]
      val motionWithTransition2 = stub[MotionWithTransition]
      val motionWithTransition3 = stub[MotionWithTransition]

      val motionManager = new MotionManager

      When("start the base motion")
      motionManager.startMotion(motionWithTransition1)
      motionManager.startMotion(motionWithTransition2)
      motionManager.startMotion(motionWithTransition3)

      Then("the motion list in MotionManager should have correct values")
      motionManager.currentMotions should contain theSameElementsInOrderAs  List(
        motionWithTransition1,
        motionWithTransition2,
        motionWithTransition3
      )
    }

  }

  Feature("Set event callback for all MotionWithTransition in queue") {
    Scenario("Set event callback before motion started") {
      Given("several MotionWithTransition and a MotionManager")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]
      val motion4 = stub[MotionWithTransition]
      val motionManger = new MotionManager

      When("set callback on manager")
      val mockedCallback = stub[EventCallback]
      motionManger.eventCallbackHolder = Some(mockedCallback)

      And("start three motions")
      motionManger.startMotion(motion1)
      motionManger.startMotion(motion2)
      motionManger.startMotion(motion3)

      Then("First three motion should all have a callback set")
      motion1.eventCallbackHolder shouldBe Some(mockedCallback)
      motion2.eventCallbackHolder shouldBe Some(mockedCallback)
      motion3.eventCallbackHolder shouldBe Some(mockedCallback)

      And("Last motion should not have any callback set")
      motion4.eventCallbackHolder shouldBe None
    }

    Scenario("Set event callback after motion is started") {
      Given("several MotionWithTransition and a MotionManager")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]
      val motionManger = new MotionManager

      And("start three motions")
      motionManger.startMotion(motion1)
      motionManger.startMotion(motion2)
      motionManger.startMotion(motion3)

      When("set callback on manager")
      val mockedCallback = stub[EventCallback]
      motionManger.eventCallbackHolder = Some(mockedCallback)

      Then("Three motion should all have a callback set")
      motion1.eventCallbackHolder shouldBe Some(mockedCallback)
      motion2.eventCallbackHolder shouldBe Some(mockedCallback)
      motion3.eventCallbackHolder shouldBe Some(mockedCallback)
    }

  }

  Feature("Set finish callback for all MotionWithTransition in queue") {
    Scenario("Set finish callback before motion is started") {
      Given("several MotionWithTransition and a MotionManager")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]
      val motion4 = stub[MotionWithTransition]
      val motionManger = new MotionManager

      When("set callback on manager")
      val mockedCallback = stub[FinishedCallback]
      motionManger.finishedCallbackHolder = Some(mockedCallback)

      And("start three motions")
      motionManger.startMotion(motion1)
      motionManger.startMotion(motion2)
      motionManger.startMotion(motion3)

      Then("First three motion should all have a callback set")
      motion1.finishedCallbackHolder shouldBe Some(mockedCallback)
      motion2.finishedCallbackHolder shouldBe Some(mockedCallback)
      motion3.finishedCallbackHolder shouldBe Some(mockedCallback)

      And("Last motion should not have any callback set")
      motion4.finishedCallbackHolder shouldBe None
    }

    Scenario("Set finish callback after motion is started") {
      Given("several MotionWithTransition and a MotionManager")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]
      val motionManger = new MotionManager


      And("start three motions")
      motionManger.startMotion(motion1)
      motionManger.startMotion(motion2)
      motionManger.startMotion(motion3)

      When("set callback on manager")
      val mockedCallback = stub[FinishedCallback]
      motionManger.finishedCallbackHolder = Some(mockedCallback)

      Then("Three motion should all have a callback set")
      motion1.finishedCallbackHolder shouldBe Some(mockedCallback)
      motion2.finishedCallbackHolder shouldBe Some(mockedCallback)
      motion3.finishedCallbackHolder shouldBe Some(mockedCallback)
    }

  }

  Feature("Force fade out previous motions") {
    Scenario("Start 3 motions") {
      Given("several MotionWithTransition and a MotionManager")
      val motion1 = stub[Motion]
      val motion2 = stub[Motion]
      val motion3 = stub[Motion]
      val motionManger = new MotionManager

      When("start three motion")
      val transitionMotion1 = motionManger.startMotion(motion1)
      val transitionMotion2 = motionManger.startMotion(motion2)
      val transitionMotion3 = motionManger.startMotion(motion3)

      Then("first two motion should be marked as force fade out")
      transitionMotion1.isForceToFadeOut shouldBe true
      transitionMotion2.isForceToFadeOut shouldBe true

      And("the third motion should not be force faded out")
      transitionMotion3.isForceToFadeOut shouldBe false
    }
  }

  Feature("Calculate and chain all operations from all motions") {
    Scenario("Start 3 motion and none of them are finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      And("these motion will return a series operations when calculate operations")
      val mockedOperations1 = List(ParameterValueAdd("id1", 0.2f), ParameterValueMultiply("id2", 0.3f))
      val mockedOperations2 = List(ParameterValueUpdate("id3", 0.4f))
      val mockedOperations3 = List(ParameterValueUpdate("id4", 0.5f), ParameterValueMultiply("id5", 0.6f))
      (motion1.calculateOperations _).when(model, *, *, *).returns(mockedOperations1)
      (motion2.calculateOperations _).when(model, *, *, *).returns(mockedOperations2)
      (motion3.calculateOperations _).when(model, *, *, *).returns(mockedOperations3)

      And("start these motion with a MotionManager")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      When("calculate the operations from motion manager")
      val operations = motionManager.calculateOperations(model, 0, 0, 1)

      Then("operations should be a operations contains all mocked operations")
      operations should contain theSameElementsInOrderAs (mockedOperations1 ++ mockedOperations2 ++ mockedOperations3)

      And("the elements in queue should have all three motions")
      motionManager.currentMotions should contain theSameElementsInOrderAs List(motion1, motion2, motion3)
    }

    Scenario("Start 3 motion and the second motion is finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      And("the second motion is finished")
      (() => motion2.isFinished).when().returns(true)

      And("these motion will return a series operations when calculate operations")
      val mockedOperations1 = List(ParameterValueAdd("id1", 0.2f), ParameterValueMultiply("id2", 0.3f))
      val mockedOperations2 = List(ParameterValueUpdate("id3", 0.4f))
      val mockedOperations3 = List(ParameterValueUpdate("id4", 0.5f), ParameterValueMultiply("id5", 0.6f))
      (motion1.calculateOperations _).when(model, *, *, *).returns(mockedOperations1)
      (motion2.calculateOperations _).when(model, *, *, *).returns(mockedOperations2)
      (motion3.calculateOperations _).when(model, *, *, *).returns(mockedOperations3)

      And("start these motion with a MotionManager")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      When("calculate the operations from motion manager")
      val operations = motionManager.calculateOperations(model, 0, 0, 1)

      Then("operations should be a operations contains all mocked operations")
      operations should contain theSameElementsInOrderAs (mockedOperations1 ++ mockedOperations2 ++ mockedOperations3)

      And("the elements in queue should only contains motion1, motion3")
      motionManager.currentMotions should contain theSameElementsInOrderAs List(motion1, motion3)
    }

  }

  Feature("Start force fade out previous motion when calculate operations") {
    Scenario("All previous motions are finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      (motion1.calculateOperations _).when(model, *, *, *).returns(Nil)
      (motion2.calculateOperations _).when(model, *, *, *).returns(Nil)
      (motion3.calculateOperations _).when(model, *, *, *).returns(Nil)

      And("the second motion is finished")
      (() => motion1.isFinished).when().returns(true)
      (() => motion2.isFinished).when().returns(true)
      (() => motion3.isFinished).when().returns(false)

      And("a MotionManager start three motion")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      When("calculate operations")
      motionManager.calculateOperations(model, 0, 0, 1)

      Then("none of these motion's force fade out should be triggered")
      (motion1.startFadeOut _).verify(*).never()
      (motion2.startFadeOut _).verify(*).never()
      (motion3.startFadeOut _).verify(*).never()
    }

    Scenario("Only second motion is finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      (motion1.calculateOperations _).when(model, *, *, *).returns(Nil)
      (motion2.calculateOperations _).when(model, *, *, *).returns(Nil)
      (motion3.calculateOperations _).when(model, *, *, *).returns(Nil)

      And("the second motion is finished")
      (() => motion1.isFinished).when().returns(false)
      (() => motion2.isFinished).when().returns(true)
      (() => motion3.isFinished).when().returns(false)
      (() => motion1.isForceToFadeOut).when().returns(true)
      (() => motion2.isForceToFadeOut).when().returns(true)
      (() => motion3.isForceToFadeOut).when().returns(false)

      And("a MotionManager start three motion")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      When("calculate operations")
      motionManager.calculateOperations(model, 0, 0, 1)

      Then("none of these motion's force fade out should be triggered")
      (motion1.startFadeOut _).verify(*).atLeastOnce()
      (motion2.startFadeOut _).verify(*).never()
      (motion3.startFadeOut _).verify(*).never()
    }

  }

  Feature("Check if all motion is finished") {
    Scenario("There is no motion in the queue") {
      Given("a MotionManager that has not start any motion")
      val motionManager = new MotionManager

      Then("isAllFinished should be true")
      motionManager.isAllFinished shouldBe true
    }

    Scenario("All motion is not finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      And("a MotionManager that has start these three motion")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      And("these three motion are all not finished")
      (() => motion1.isFinished).when().returns(false)
      (() => motion2.isFinished).when().returns(false)
      (() => motion3.isFinished).when().returns(false)

      Then("isAllFinished should be false")
      motionManager.isAllFinished shouldBe false
    }

    Scenario("Only some motion is finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      And("a MotionManager that has start these three motion")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      And("these only the second motion is finished")
      (() => motion1.isFinished).when().returns(false)
      (() => motion2.isFinished).when().returns(true)
      (() => motion3.isFinished).when().returns(false)

      Then("isAllFinished should be false")
      motionManager.isAllFinished shouldBe false
    }

    Scenario("all motion is finished") {
      Given("several MotionWithTransition")
      val motion1 = stub[MotionWithTransition]
      val motion2 = stub[MotionWithTransition]
      val motion3 = stub[MotionWithTransition]

      And("a MotionManager that has start these three motion")
      val motionManager = new MotionManager
      motionManager.startMotion(motion1)
      motionManager.startMotion(motion2)
      motionManager.startMotion(motion3)

      And("these motions are all finished")
      (() => motion1.isFinished).when().returns(true)
      (() => motion2.isFinished).when().returns(true)
      (() => motion3.isFinished).when().returns(true)

      Then("isAllFinished should be true")
      motionManager.isAllFinished shouldBe true
    }

  }

}
