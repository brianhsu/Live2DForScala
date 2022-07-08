package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.audio.{AudioDispatcher, AudioPlayer, AudioRMSCalculator, AudioStreamCloser}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMotionSound.{DefaultProcessorsFactory, Processors, ProcessorsFactory}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueAdd
import moe.brianhsu.utils.AudioOutputTest
import moe.brianhsu.utils.mock.AudioMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

import java.io.FileNotFoundException
import javax.sound.sampled.{AudioFormat, AudioInputStream}
import scala.util.Success

class LipSyncFromMotionSoundFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TryValues with AudioMock {

  private val model: Live2DModel = mock[Live2DModel]
  private val lipSyncIds: List[String] = List("LipSyncId1", "LipSyncId2")

  Feature("DefaultProcessorsFactory should create Processors correctly") {
    Scenario("Create processors from non exist sound file") {
      When("create the processors from non exist sound file")
      val processorsHolder = DefaultProcessorsFactory("NonExists.wav", 50)

      Then("it should return a failure")
      processorsHolder.isFailure shouldBe true
      processorsHolder.failure.exception shouldBe a[FileNotFoundException]
    }

    Scenario("Create processors from exist sound file", AudioOutputTest) {
      When("create the processors from exist sound file")
      val processorsHolder = DefaultProcessorsFactory("src/test/resources/sounds/16.wav", 50)

      Then("it should return a failure")
      processorsHolder.isSuccess shouldBe true
      val Processors(dispatcher, audioRMSCalculator, audioPlayer, audioStreamCloser) = processorsHolder.success.value
      dispatcher.processors should contain theSameElementsInOrderAs List(audioRMSCalculator, audioPlayer, audioStreamCloser)
      audioPlayer.volume shouldBe 50
      audioRMSCalculator should not be null
      audioStreamCloser should not be null
    }

    Scenario("Create the LipFromMotionSound from avatar settings") {
      Given("an avatar setting")
      val avatarSettings = Settings(null, Nil, None, None, Nil, lipSyncIds, Map.empty, Map.empty, Nil)

      When("create a LipSyncFromMotionSound from it")
      val lipSync = new LipSyncFromMotionSound(avatarSettings, 100)

      Then("the lipSyncId should be correct")
      lipSync.lipSyncIds should contain theSameElementsInOrderAs (lipSyncIds)

    }

  }

  Feature("Control volume") {
    Scenario("Control volume before processors is set") {
      Given("a set of processors and a LipSyncFromMotionSound")
      val processors = createStubbedProcessors()
      val factory = stub[ProcessorsFactory]
      val lipSyncFromMotionSound = new LipSyncFromMotionSound(factory, Nil, 65)

      Then("the current volume should be 65")
      lipSyncFromMotionSound.volume shouldBe 65

      When("set the volume")
      lipSyncFromMotionSound.volume = 23

      Then("the current volume should changed to 23")
      lipSyncFromMotionSound.volume shouldBe 23

      And("the volume in the AudioPlayer should not be touched")
      (processors.audioPlayer.volume_= _).verify(*).never()
    }

    Scenario("Control volume after processors is set") {
      Given("a set of processors and a LipSyncFromMotionSound")
      val processors = createStubbedProcessors()
      val factory = stub[ProcessorsFactory]
      val lipSyncFromMotionSound = new LipSyncFromMotionSound(factory, Nil, 65)

      Then("the current volume should be 65")
      lipSyncFromMotionSound.volume shouldBe 65

      And("set the processors in the LipSync effect")
      lipSyncFromMotionSound.processorsHolder = Some(processors)

      When("set the volume")
      lipSyncFromMotionSound.volume = 23

      Then("the current volume should changed to 23")
      lipSyncFromMotionSound.volume shouldBe 23

      And("the volume in the AudioPlayer should also be changed")
      (processors.audioPlayer.volume_= _).verify(23).once()

    }

  }

  Feature("Start with a sound file") {
    Scenario("There is already exist processors and start without sound file") {
      Given("a set of processors and a LipSyncFromMotionSound")
      val oldProcessors = createStubbedProcessors()
      val newProcessors = createStubbedProcessors()
      val factory = stub[ProcessorsFactory]

      (factory.apply _).when(*, *).returns(Success(newProcessors))

      val lipSyncFromMotionSound = new LipSyncFromMotionSound(factory, Nil, 65)

      And("set the processors in the LipSync effect")
      lipSyncFromMotionSound.processorsHolder = Some(oldProcessors)

      When("start with no sound file")
      lipSyncFromMotionSound.startWith(None)

      Then("the old dispatcher should be stopped")
      (oldProcessors.dispatcher.stop _).verify().once()

      And("the new dispatcher should not be started")
      (newProcessors.dispatcher.run _).verify().never()

    }

    Scenario("There is already exist processors and start with sound file") {
      Given("a set of processors and a LipSyncFromMotionSound")
      val oldProcessors = createStubbedProcessors()
      val newProcessors = createStubbedProcessorsWithRealDispatcher()
      val factory = stub[ProcessorsFactory]

      (factory.apply _).when(*, *).returns(Success(newProcessors))

      val lipSyncFromMotionSound = new LipSyncFromMotionSound(factory, Nil, 65)

      And("set the processors in the LipSync effect")
      lipSyncFromMotionSound.processorsHolder = Some(oldProcessors)

      When("start with no sound file")
      lipSyncFromMotionSound.startWith(Some("soundFile"))

      Then("the old dispatcher should be stopped")
      (oldProcessors.dispatcher.stop _).verify().once()

      And("after 500 ms (wait the new dispatcher thread to start)")
      Thread.sleep(500)

      And("the new dispatcher should not be started")
      newProcessors.dispatcher.asInstanceOf[MockedAudioDispatcher].isStarted shouldBe true

    }

  }
  Feature("Calculate the update operations") {
    Scenario("Calculate the update operations") {
      Given("an stubbed processors factory and LipSyncFromMotionSound")
      val factory = stub[ProcessorsFactory]
      val lipSync = new LipSyncFromMotionSound(factory, lipSyncIds)

      And("a stubbed audioRMSCalculator that will return RMS")
      val audioRMSCalculator = stub[AudioRMSCalculator]
      (() => audioRMSCalculator.currentRMS).when().returns(1.23f)

      And("the audioRMSCalculator is assigned to the Lip Sync Effect")
      lipSync.processorsHolder = Some(Processors(stub[AudioDispatcher], audioRMSCalculator, stub[AudioPlayer], stub[AudioStreamCloser]))

      When("calculate the update operation")
      val operation1 = lipSync.calculateOperations(model, 1, 1)

      Then("the operation should add lip sync parameter correctly")
      operation1 should contain theSameElementsInOrderAs List(ParameterValueAdd("LipSyncId1", 1.23f, 5.0f), ParameterValueAdd("LipSyncId2", 1.23f, 5.0f))

    }

  }

  Feature("Stop the AudioDispatcher") {
    Scenario("Processors is not set") {
      Given("a set of processors and a LipSyncFromMotionSound")
      val processors = createStubbedProcessors()
      val factory = stub[ProcessorsFactory]
      val lipSyncFromMotionSound = new LipSyncFromMotionSound(factory, Nil, 65)

      When("stop the lip sync")
      lipSyncFromMotionSound.stop()

      And("the AudioDispatcher should not be touched")
      (processors.dispatcher.stop _).verify().never()
    }

    Scenario("Processors is set") {
      Given("a set of processors and a LipSyncFromMotionSound")
      val processors = createStubbedProcessors()
      val factory = stub[ProcessorsFactory]
      val lipSyncFromMotionSound = new LipSyncFromMotionSound(factory, Nil, 65)

      And("set the processors in the LipSync effect")
      lipSyncFromMotionSound.processorsHolder = Some(processors)

      When("stop the lip sync")
      lipSyncFromMotionSound.stop()

      And("the AudioDispatcher should not be stopped")
      (processors.dispatcher.stop _).verify().once()

    }

  }
  private def createStubbedProcessorsWithRealDispatcher(): Processors = {
    val audioFormat = new AudioFormat(44100, 8, 1, false, false)
    val audioInputStream = stub[MockableAudioInputStream]
    (audioInputStream.getFormat _).when().returns(audioFormat)
    val audioDispatcher = new MockedAudioDispatcher(audioInputStream)
    val audioPlayer = stub[AudioPlayer]
    val audioStreamCloser = stub[AudioStreamCloser]
    val audioRMSCalculator = stub[AudioRMSCalculator]
    Processors(audioDispatcher, audioRMSCalculator, audioPlayer, audioStreamCloser)
  }


  private def createStubbedProcessors(): Processors = {
    val audioDispatcher = stub[AudioDispatcher]
    val audioPlayer = stub[AudioPlayer]
    val audioStreamCloser = stub[AudioStreamCloser]
    val audioRMSCalculator = stub[AudioRMSCalculator]
    Processors(audioDispatcher, audioRMSCalculator, audioPlayer, audioStreamCloser)
  }

  class MockedAudioDispatcher(audioInputStream: AudioInputStream) extends AudioDispatcher(audioInputStream, 0, Nil) {
    var isStarted = false
    override def run(): Unit = {
      this.isStarted = true
    }
  }

}