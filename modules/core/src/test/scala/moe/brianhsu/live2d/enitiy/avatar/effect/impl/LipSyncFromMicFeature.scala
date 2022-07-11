package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.audio.{AudioDispatcher, AudioRMSCalculator}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueAdd
import moe.brianhsu.utils.mock.AudioMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

import javax.sound.sampled._

class LipSyncFromMicFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TryValues with AudioMock {

  private val lipSyncIds: List[String] = List("LipSyncId1", "LipSyncId2")

  Feature("Create LipSyncFromMic using avatar setting and mixer") {
    Scenario("Create LipSyncFromMic from avatar with lipSyncIds") {
      Given("an avatar setting with lipSyncIds")
      val avatarSettings = Settings(null, Nil, None, None, Nil, lipSyncIds, Map.empty, Map.empty, Nil)

      And("a stubbed mixer")
      val audioFormat = new AudioFormat(44100, 16, 1, true,false)
      val mixer = stub[Mixer]
      val targetDataLine = stub[TargetDataLine]
      val targetLineInfo = new DataLine.Info(classOf[TargetDataLine], audioFormat)
      (() => mixer.getTargetLineInfo).when().returns(Array(targetLineInfo))
      (mixer.getLine _).when(*).returns(targetDataLine)
      (() => targetDataLine.getFormat).when().returns(audioFormat)
      (targetDataLine.read _).when(*, *, *).returns(-1)

      When("create a LipSyncFromMic from them")
      val lipSyncHolder = LipSyncFromMic(avatarSettings, mixer, 3.5f, forceEvenNoSetting = false)

      Then("it should create LipSyncFromMic object successfully")
      lipSyncHolder.isSuccess shouldBe true

      And("the lip id should have the same value as in the avatar setting")
      lipSyncHolder.success.value.lipSyncIds should contain theSameElementsInOrderAs lipSyncIds

      And("the weight in the LipSyncFromMic should have correct value")
      lipSyncHolder.success.value.weight shouldBe 3.5

      And("the targetDataLine should be opened and started")
      (targetDataLine.open: AudioFormat => Unit).verify(*).once()
      (() => targetDataLine.start()).verify().once()

      lipSyncHolder.foreach(_.stop())
    }

    Scenario("Create LipSyncFromMic from avatar without lipSyncIds and force lip sync") {
      Given("an avatar setting")
      val avatarSettings = Settings(null, Nil, None, None, Nil, Nil, Map.empty, Map.empty, Nil)

      And("a stubbed mixer")
      val audioFormat = new AudioFormat(44100, 16, 1, true,false)
      val mixer = stub[Mixer]
      val targetDataLine = stub[TargetDataLine]
      val targetLineInfo = new DataLine.Info(classOf[TargetDataLine], audioFormat)
      (() => mixer.getTargetLineInfo).when().returns(Array(targetLineInfo))
      (mixer.getLine _).when(*).returns(targetDataLine)
      (() => targetDataLine.getFormat).when().returns(audioFormat)
      (targetDataLine.read _).when(*, *, *).returns(-1)

      When("create a LipSyncFromMic from them")
      val lipSyncHolder = LipSyncFromMic(avatarSettings, mixer, 3.5f, forceEvenNoSetting = true)

      Then("it should create LipSyncFromMic object successfully")
      lipSyncHolder.isSuccess shouldBe true

      And("the lip sync id should have the default value ParamMouthOpenY")
      lipSyncHolder.success.value.lipSyncIds should contain theSameElementsInOrderAs List("ParamMouthOpenY")

      And("the weight in the LipSyncFromMic should have correct value")
      lipSyncHolder.success.value.weight shouldBe 3.5

      And("the targetDataLine should be opened and started")
      (targetDataLine.open: AudioFormat => Unit).verify(*).once()
      (() => targetDataLine.start()).verify().once()

      lipSyncHolder.foreach(_.stop())
    }

    Scenario("Create LipSyncFromMic from avatar without lipSyncIds and NOT force lip sync") {
      Given("an avatar setting")
      val avatarSettings = Settings(null, Nil, None, None, Nil, Nil, Map.empty, Map.empty, Nil)

      And("a stubbed mixer")
      val audioFormat = new AudioFormat(44100, 16, 1, true,false)
      val mixer = stub[Mixer]
      val targetDataLine = stub[TargetDataLine]
      val targetLineInfo = new DataLine.Info(classOf[TargetDataLine], audioFormat)
      (() => mixer.getTargetLineInfo).when().returns(Array(targetLineInfo))
      (mixer.getLine _).when(*).returns(targetDataLine)
      (() => targetDataLine.getFormat).when().returns(audioFormat)
      (targetDataLine.read _).when(*, *, *).returns(-1)

      When("create a LipSyncFromMic from them")
      val lipSyncHolder = LipSyncFromMic(avatarSettings, mixer, 3.5f, forceEvenNoSetting = false)

      Then("it should create LipSyncFromMic object successfully")
      lipSyncHolder.isSuccess shouldBe true

      And("the lip sync id should be Nil")
      lipSyncHolder.success.value.lipSyncIds shouldBe Nil

      And("the weight in the LipSyncFromMic should have correct value")
      lipSyncHolder.success.value.weight shouldBe 3.5

      And("the targetDataLine should be opened and started")
      (targetDataLine.open: AudioFormat => Unit).verify(*).once()
      (() => targetDataLine.start()).verify().once()

      lipSyncHolder.foreach(_.stop())
    }

    Scenario("Create LipSyncFromMic failed") {
      Given("an avatar setting")
      val avatarSettings = Settings(null, Nil, None, None, Nil, lipSyncIds, Map.empty, Map.empty, Nil)

      And("a stubbed mixer that does not return any TargetDataLine")
      val mixer = stub[Mixer]
      (() => mixer.getTargetLineInfo).when().returns(Array())

      When("create a LipSyncFromMic from them")
      val lipSyncHolder = LipSyncFromMic(avatarSettings, mixer, 3.5f, forceEvenNoSetting = false)

      Then("it should be a failure")
      lipSyncHolder.isFailure shouldBe true
      lipSyncHolder.failure.exception shouldBe a [NoSuchElementException]
    }

  }

  Feature("Calculate the update operations") {
    Scenario("Calculate the update operations") {

      Given("a stubbed audioRMSCalculator that will return RMS")
      val audioRMSCalculator = stub[AudioRMSCalculator]
      (() => audioRMSCalculator.currentRMS).when().returns(1.23f)

      And("a LipSyncFromMic using it")
      val dispatcher = stub[AudioDispatcher]
      val lipSyncFromMic = new LipSyncFromMic(lipSyncIds, dispatcher, audioRMSCalculator)

      When("calculate the update operation")
      val operation1 = lipSyncFromMic.calculateOperations(mock[Live2DModel], 1, 1)

      Then("the operation should add lip sync parameter correctly")
      operation1 should contain theSameElementsInOrderAs List(ParameterValueAdd("LipSyncId1", 1.23f, 5.0f), ParameterValueAdd("LipSyncId2", 1.23f, 5.0f))

      Thread.sleep(100)

      lipSyncFromMic.stop()

      Then("the dispatcher should be started")
      (() => dispatcher.run()).verify().once()
    }

  }

  Feature("Stop the AudioDispatcher") {
    Scenario("When stop is called") {
      Given("a LipSyncFromMic")
      val dispatcher = stub[AudioDispatcher]
      val audioRMSCalculator = stub[AudioRMSCalculator]
      val lipSyncFromMic = new LipSyncFromMic(lipSyncIds, dispatcher, audioRMSCalculator)

      When("stop the lip sync")
      lipSyncFromMic.stop()

      And("the AudioDispatcher should be stopped")
      (dispatcher.stop _).verify().once()
    }
  }
}