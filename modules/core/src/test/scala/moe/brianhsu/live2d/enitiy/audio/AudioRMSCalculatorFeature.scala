package moe.brianhsu.live2d.enitiy.audio

import moe.brianhsu.utils.mock.AudioMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import javax.sound.sampled.AudioFormat

class AudioRMSCalculatorFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with AudioMock {

  Feature("Calculate audio RMS") {
    Scenario("There is no samples at all") {
      Given("an AudioRMS calculator")
      val audioRMSCalculator = new AudioRMSCalculator

      And("an audio event contains no sample")
      val audioEvent = AudioEvent(stub[AudioFormat], 0, Nil, Nil)

      When("calculate the RMS of that audio event")
      audioRMSCalculator.process(audioEvent)

      Then("the current RMS should be 0")
      audioRMSCalculator.currentRMS shouldBe 0
    }

    Scenario("All samples inside the AudioEvent is 0") {
      Given("an AudioRMS calculator")
      val audioRMSCalculator = new AudioRMSCalculator

      And("an audio event that all samples are 0.0f")
      val audioEvent = AudioEvent(stub[AudioFormat], 0, List(0.0f, 0.0f, 0.0f, 0.0f, 0.0f), Nil)

      When("calculate the RMS of that audio event")
      audioRMSCalculator.process(audioEvent)

      Then("the current RMS should be 0")
      audioRMSCalculator.currentRMS shouldBe 0
    }

    Scenario("All samples inside the AudioEvent is the same") {
      Given("an AudioRMS calculator")
      val audioRMSCalculator = new AudioRMSCalculator

      And("an audio event that all samples are 1.5f")
      val audioEvent = AudioEvent(stub[AudioFormat], 0, List(1.5f, 1.5f, 1.5f, 1.5f, 1.5f), Nil)

      When("calculate the RMS of that audio event")
      audioRMSCalculator.process(audioEvent)

      Then("the current RMS should be 1.5")
      audioRMSCalculator.currentRMS shouldBe 1.5
    }

    Scenario("All samples inside the AudioEvent are different") {
      Given("an AudioRMS calculator")
      val audioRMSCalculator = new AudioRMSCalculator

      And("an audio event that every value in the sample are different")
      val audioEvent = AudioEvent(stub[AudioFormat], 0, List(0.0f, 1.5f, 10.5f, 1.1f, 1.3f), Nil)

      When("calculate the RMS of that audio event")
      audioRMSCalculator.process(audioEvent)

      Then("the current RMS should have correct value sqrt(sumOfSquare / sample length)")
      audioRMSCalculator.currentRMS shouldBe 4.804165f
    }

  }

  Feature("The end of process should reset RMS to zero") {
    Scenario("End of process") {
      Given("an AudioRMS calculator")
      val audioRMSCalculator = new AudioRMSCalculator

      And("process an audio event that every value in the sample are different")
      val audioEvent = AudioEvent(stub[AudioFormat], 0, List(0.0f, 1.5f, 10.5f, 1.1f, 1.3f), Nil)
      audioRMSCalculator.process(audioEvent)

      When("calling the end() method")
      audioRMSCalculator.end(mock[MockableAudioInputStream])

      Then("the RMS should be 0")
      audioRMSCalculator.currentRMS shouldBe 0

    }
  }

}
