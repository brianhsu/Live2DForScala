package moe.brianhsu.live2d.enitiy.audio

import moe.brianhsu.utils.mock.AudioMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import javax.sound.sampled.{AudioFormat, AudioSystem, FloatControl, SourceDataLine}

class AudioPlayerFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with AudioMock {

  Feature("Create AudioPlayer using factory") {
    Scenario("Create AudioPlayer from only AudioInputStream") {
      Given("an AudioInputStream")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/32.wav"))

      When("create an AudioPlayer using factory")
      val audioPlayer = AudioPlayer(audioInputStream, 36)

      Then("it should correct successfully")
      audioPlayer shouldBe a[AudioPlayer]

      And("the volume should be same as in the factory")
      audioPlayer.volume shouldBe 36
    }
  }

  Feature("Control volume") {
    Scenario("Set default volume when processor is started") {
      Given("a stubbed SourceDataLine with stubbed volume control")
      val volumeControl = stub[FloatControl]
      val sourceDataLine = stub[SourceDataLine]
      val defaultVolume = 50
      (sourceDataLine.getControl _).when(FloatControl.Type.MASTER_GAIN).returns(volumeControl)

      And("create an AudioPlayer from that")
      val audioPlayer = new AudioPlayer(sourceDataLine, defaultVolume)

      When("the AudioPlayer is started")
      audioPlayer.start()

      Then("the volume control should receive a set to 50")
      (volumeControl.setValue _).verify(20.0f * Math.log10(defaultVolume / 100.0).toFloat)
    }

    Scenario("Update volume") {
      Given("a stubbed SourceDataLine with stubbed volume control")
      val volumeControl = stub[FloatControl]
      val sourceDataLine = stub[SourceDataLine]

      (sourceDataLine.getControl _).when(FloatControl.Type.MASTER_GAIN).returns(volumeControl)

      And("an AudioPlayer with default volume 50")
      val defaultVolume = 50
      val audioPlayer = new AudioPlayer(sourceDataLine, defaultVolume)

      When("set the volume to 24")
      val targetVolume = 24
      audioPlayer.volume = targetVolume

      And("the volume control should then to volume to target volume 24")
      (volumeControl.setValue _).verify(20.0f * Math.log10(targetVolume / 100.0).toFloat)
    }

  }

  Feature("Close source data line") {
    Scenario("When the process ended") {
      Given("a stubbed SourceDataLine with stubbed volume control")
      val volumeControl = stub[FloatControl]
      val sourceDataLine = stub[SourceDataLine]

      (sourceDataLine.getControl _).when(FloatControl.Type.MASTER_GAIN).returns(volumeControl)

      And("an AudioPlayer")
      val audioPlayer = new AudioPlayer(sourceDataLine, 50)

      When("end the process")
      audioPlayer.end(mock[MockableAudioInputStream])

      Then("the source data line should be drained and closed")
      inSequence {
        (() => sourceDataLine.drain()).verify().once()
        (() => sourceDataLine.close()).verify().once()

      }

    }
  }

  Feature("Send the data to source data line (play it)") {
    Scenario("Receive AudioEvent") {
      Given("a stubbed SourceDataLine with stubbed volume control")
      val volumeControl = stub[FloatControl]
      val sourceDataLine = stub[SourceDataLine]

      (sourceDataLine.getControl _).when(FloatControl.Type.MASTER_GAIN).returns(volumeControl)

      And("an AudioPlayer")
      val audioPlayer = new AudioPlayer(sourceDataLine, 50)

      When("receive two AudioEvents")
      val firstEventBytes = List[Byte](1, 2, 3, 4, 5, 6, 7, 8)
      val secondEventBytes = List[Byte](8, 7, 6, 5, 4, 3, 2, 1)

      audioPlayer.onProcess(AudioEvent(stub[AudioFormat], 8, Nil, firstEventBytes))
      audioPlayer.onProcess(AudioEvent(stub[AudioFormat], 8, Nil, secondEventBytes))

      Then("the source data line should receive two writes")
      inSequence {
        (sourceDataLine.write _)
          .verify(where {(bytes, offset, length) =>
            bytes.toList == firstEventBytes &&
            offset == 0 &&
            length == firstEventBytes.size
          }).once()

        (sourceDataLine.write _)
          .verify(where {(bytes, offset, length) =>
            bytes.toList == secondEventBytes &&
              offset == 0 &&
              length == secondEventBytes.size
          }).once()

      }

    }
  }

}
