package moe.brianhsu.live2d.enitiy.audio

import moe.brianhsu.utils.mock.AudioMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import javax.sound.sampled.AudioFormat

class AudioStreamCloserFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with AudioMock {

  Feature("Close the audio stream") {
    Scenario("Receive the end call") {
      Given("an AudioStreamCloser and a stubbed AudioInputStream")
      val audioInputStream = stub[MockableAudioInputStream]
      val audioStreamCloser = new AudioStreamCloser

      When("calling the end method")
      audioStreamCloser.end(audioInputStream)

      Then("the AudioInputStream should be closed")
      (() => audioInputStream.close()).verify().once()
    }


  }

  Feature("Do nothing when receive AudioEvent") {
    Scenario("Receive AudioEvent") {
      Given("an AudioStreamCloser")
      val audioStreamCloser = new AudioStreamCloser

      When("calling the process method")
      Then("nothing should happen and there is no exception")
      noException should be thrownBy {
        audioStreamCloser.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))
      }

    }
  }

}
