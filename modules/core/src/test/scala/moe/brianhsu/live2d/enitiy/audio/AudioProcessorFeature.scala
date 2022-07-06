package moe.brianhsu.live2d.enitiy.audio

import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import javax.sound.sampled.{AudioFormat, AudioInputStream}

class AudioProcessorFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {

  Feature("Call start method when first processing event") {
    Scenario("Does not process event at all") {
      Given("A mocked processor")
      val processor = new MockedProcessor

      Then("the start called count should be 0")
      processor.startCalledCount shouldBe 0
    }

    Scenario("Process event only one time") {
      Given("A mocked processor")
      val processor = new MockedProcessor

      And("process an event")
      processor.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))

      Then("the start called count should be 1")
      processor.startCalledCount shouldBe 1
    }

    Scenario("Process event multiple time") {
      Given("A mocked processor")
      val processor = new MockedProcessor

      And("process multiple events")
      processor.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))
      processor.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))
      processor.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))
      processor.process(AudioEvent(stub[AudioFormat], 0, Nil, Nil))

      Then("the start called count should still be 1")
      processor.startCalledCount shouldBe 1
    }

  }

  class MockedProcessor extends AudioProcessor {
    var startCalledCount = 0

    override def start(): Unit = startCalledCount += 1

    override def onProcess(event: AudioEvent): Unit = {}

    override def end(audioInputStream: AudioInputStream): Unit = {}
  }

}
