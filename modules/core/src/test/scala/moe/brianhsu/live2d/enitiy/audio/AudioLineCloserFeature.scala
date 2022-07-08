package moe.brianhsu.live2d.enitiy.audio

import moe.brianhsu.utils.mock.AudioMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import javax.sound.sampled.{AudioFormat, DataLine}

class AudioLineCloserFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with AudioMock {

  Feature("Close the audio stream") {
    Scenario("Receive the end call") {
      Given("an AudioLineCloser and a stubbed DataLine")
      val stubbedLine = stub[DataLine]
      val audioInputStream = stub[MockableAudioInputStream]
      val audioLineCloser = new AudioLineCloser(stubbedLine)

      When("calling the end method")
      audioLineCloser.end(audioInputStream)

      Then("the stubbedLine should be closed")
      inSequence {
        (() => stubbedLine.drain()).verify().once()
        (() => stubbedLine.stop()).verify().once()
        (() => stubbedLine.close()).verify().once()
      }
    }
  }

  Feature("Do nothing when receive AudioEvent") {
    Scenario("Receive AudioEvent") {
      Given("an AudioLineCloser")
      val stubbedLine = stub[DataLine]
      val audioLineCloser = new AudioLineCloser(stubbedLine)

      When("calling the process method")
      Then("nothing should happen and there is no exception")
      noException should be thrownBy {
        audioLineCloser.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))
      }

    }
  }

}
