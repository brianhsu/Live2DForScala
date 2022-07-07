package moe.brianhsu.live2d.enitiy.audio

import org.json4s.native.{Serialization, parseJson}
import org.json4s.{Formats, NoTypeHints}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, ScalaTestVersion}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayOutputStream
import javax.sound.sampled.AudioSystem.getTargetDataLine
import javax.sound.sampled.{AudioFormat, AudioInputStream, AudioSystem, DataLine, Mixer, Port, SourceDataLine, TargetDataLine}
import scala.io.Source
import scala.util.Using

class AudioDispatcherFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {
  private implicit val formats: Formats = Serialization.formats(NoTypeHints)


  Feature("Convert AudioInputStream to AudioEvent correctly") {
    Scenario("Convert 8 bit audio stream with 8 samples per batch") {
      Given("an AudioDispatcher with an 8 bit AudioInputStream")
      And("append a ReadAllSamples processor to it")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/8.wav"))
      val processor = new ReadAllSamples
      val dispatcher = new AudioDispatcher(audioInputStream, 8).appendProcessor(processor)

      When("run the dispatcher")
      dispatcher.run()

      Then("the processed events in the processor should contains the correct data")
      val events = processor.processedEvents
      val expectationFile = Source.fromInputStream(this.getClass.getResourceAsStream("/expectation/sounds/8bit.json"))
      val expectations = Using.resource(expectationFile)(_.getLines().toList)

      events.size shouldBe expectations.size
      expectations.zipWithIndex.foreach { case (expectation, index) =>
        val expectationInJson = parseJson(expectation).extract[Result]
        val audioEvent = events(index)
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.samples should contain theSameElementsInOrderAs expectationInJson.samples
        audioEvent.rawBytes should contain theSameElementsInOrderAs expectationInJson.rawBytes
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.format.getChannels shouldBe 1
        audioEvent.format.getEncoding shouldBe AudioFormat.Encoding.PCM_UNSIGNED
        audioEvent.format.getFrameRate shouldBe 44100.0f
        audioEvent.format.getFrameSize shouldBe 1
        audioEvent.format.getSampleRate shouldBe 44100.0
        audioEvent.format.getSampleSizeInBits shouldBe 8
      }
    }

    Scenario("Convert 16 bit audio stream") {
      Given("an AudioDispatcher with an 16 bit AudioInputStream")
      And("append a ReadAllSamples processor to it")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/16.wav"))
      val processor = new ReadAllSamples
      val dispatcher = new AudioDispatcher(audioInputStream, 8).appendProcessor(processor)

      When("run the dispatcher")
      dispatcher.run()

      Then("the processed events in the processor should contains the correct data")
      val events = processor.processedEvents
      val expectationFile = Source.fromInputStream(this.getClass.getResourceAsStream("/expectation/sounds/16bit.json"))
      val expectations = Using.resource(expectationFile)(_.getLines().toList)

      events.size shouldBe expectations.size
      expectations.zipWithIndex.foreach { case (expectation, index) =>
        val expectationInJson = parseJson(expectation).extract[Result]
        val audioEvent = events(index)
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.samples should contain theSameElementsInOrderAs expectationInJson.samples
        audioEvent.rawBytes should contain theSameElementsInOrderAs expectationInJson.rawBytes
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.format.getChannels shouldBe 1
        audioEvent.format.getEncoding shouldBe AudioFormat.Encoding.PCM_SIGNED
        audioEvent.format.getFrameRate shouldBe 44100.0f
        audioEvent.format.getFrameSize shouldBe 2
        audioEvent.format.getSampleRate shouldBe 44100.0
        audioEvent.format.getSampleSizeInBits shouldBe 16
      }

    }

    Scenario("Convert 24bit audio stream") {
      Given("an AudioDispatcher with an 24 bit AudioInputStream")
      And("append a ReadAllSamples processor to it")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/24.wav"))
      val processor = new ReadAllSamples
      val dispatcher = new AudioDispatcher(audioInputStream, 8).appendProcessor(processor)

      When("run the dispatcher")
      dispatcher.run()

      Then("the processed events in the processor should contains the correct data")
      val events = processor.processedEvents
      val expectationFile = Source.fromInputStream(this.getClass.getResourceAsStream("/expectation/sounds/24bit.json"))
      val expectations = Using.resource(expectationFile)(_.getLines().toList)

      events.size shouldBe expectations.size
      expectations.zipWithIndex.foreach { case (expectation, index) =>
        val expectationInJson = parseJson(expectation).extract[Result]
        val audioEvent = events(index)
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.samples should contain theSameElementsInOrderAs expectationInJson.samples
        audioEvent.rawBytes should contain theSameElementsInOrderAs expectationInJson.rawBytes
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.format.getChannels shouldBe 1
        audioEvent.format.getEncoding shouldBe AudioFormat.Encoding.PCM_SIGNED
        audioEvent.format.getFrameRate shouldBe 44100.0f
        audioEvent.format.getFrameSize shouldBe 3
        audioEvent.format.getSampleRate shouldBe 44100.0
        audioEvent.format.getSampleSizeInBits shouldBe 24
      }
    }

    Scenario("Convert 32bit audio stream") {
      Given("an AudioDispatcher with an 32 bit AudioInputStream")
      And("append a ReadAllSamples processor to it")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/32.wav"))
      val processor = new ReadAllSamples
      val dispatcher = new AudioDispatcher(audioInputStream, 8).appendProcessor(processor)

      When("run the dispatcher")
      dispatcher.run()

      Then("the processed events in the processor should contains the correct data")
      val events = processor.processedEvents
      val expectationFile = Source.fromInputStream(this.getClass.getResourceAsStream("/expectation/sounds/32bit.json"))
      val expectations = Using.resource(expectationFile)(_.getLines().toList)

      events.size shouldBe expectations.size
      expectations.zipWithIndex.foreach { case (expectation, index) =>
        val expectationInJson = parseJson(expectation).extract[Result]
        val audioEvent = events(index)
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.samples should contain theSameElementsInOrderAs expectationInJson.samples
        audioEvent.rawBytes should contain theSameElementsInOrderAs expectationInJson.rawBytes
        audioEvent.totalBytesRead shouldBe expectationInJson.totalBytesRead
        audioEvent.format.getChannels shouldBe 1
        audioEvent.format.getEncoding shouldBe AudioFormat.Encoding.PCM_SIGNED
        audioEvent.format.getFrameRate shouldBe 44100.0f
        audioEvent.format.getFrameSize shouldBe 4
        audioEvent.format.getSampleRate shouldBe 44100.0
        audioEvent.format.getSampleSizeInBits shouldBe 32
      }

    }


    class ReadAllSamples extends AudioProcessor {
      var processedEvents: List[AudioEvent] = Nil

      override def onProcess(event: AudioEvent): Unit = {
        processedEvents ::= event
      }

      override def end(audioInputStream: AudioInputStream): Unit = {
        processedEvents = processedEvents.reverse
      }

      override def start(): Unit = {}
    }
  }

  Feature("Append / remove audio processor") {
    Scenario("Append processor") {
      Given("two stubbed processor")
      val processor1 = stub[AudioProcessor]
      val processor2 = stub[AudioProcessor]

      And("an AudioDispatcher with an 32 bit AudioInputStream")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/32.wav"))
      val dispatcher = new AudioDispatcher(audioInputStream, 8)

      And("append these two processors to dispatcher")
      val newDispatcher = dispatcher.appendProcessor(processor1).appendProcessor(processor2)

      Then("the new dispatcher should be another instance")
      newDispatcher should not be theSameInstanceAs (dispatcher)

      When("run the new dispatcher")
      newDispatcher.run()

      Then("both processor should be called")
      inSequence {
        (processor1.process _).verify(*).atLeastOnce()
        (processor2.process _).verify(*).atLeastOnce()
        (processor1.end _).verify(audioInputStream).once()
        (processor2.end _).verify(audioInputStream).once()
      }
    }

    Scenario("Remove processor") {

      Given("an AudioDispatcher with an 32 bit AudioInputStream with three default processors")
      val processor1 = stub[AudioProcessor]
      val processor2 = stub[AudioProcessor]
      val processor3 = stub[AudioProcessor]
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/32.wav"))
      val dispatcher = new AudioDispatcher(audioInputStream, 8, processor1 :: processor2 :: processor3 :: Nil)

      And("remove the second processor from dispatcher")
      val newDispatcher = dispatcher.removeProcessor(processor2)

      Then("the new dispatcher should be another instance")
      newDispatcher should not be theSameInstanceAs (dispatcher)

      When("run the new dispatcher")
      newDispatcher.run()

      Then("only processor 1 and 3 should be called")
      inSequence {
        (processor1.process _).verify(*).atLeastOnce()
        (processor2.process _).verify(*).never()
        (processor3.process _).verify(*).atLeastOnce()

        (processor1.end _).verify(audioInputStream).once()
        (processor2.end _).verify(audioInputStream).never()
        (processor3.end _).verify(audioInputStream).once()
      }

    }
  }

  Feature("Stop processing") {
    Scenario("Stop processing before call AudioDispatcher.run()") {
      Given("two stubbed processor")
      val processor1 = stub[AudioProcessor]
      val processor2 = stub[AudioProcessor]

      And("an AudioDispatcher with an 32 bit AudioInputStream with those processors")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/32.wav"))
      val dispatcher = new AudioDispatcher(audioInputStream, 8, processor1 :: processor2 :: Nil)

      And("stop the dispatcher first")
      dispatcher.stop()

      When("run the dispatcher")
      dispatcher.run()

      Then("both processor should be only called with end() method")
      inSequence {
        (processor1.process _).verify(*).never()
        (processor2.process _).verify(*).never()
        (processor1.end _).verify(audioInputStream).once()
        (processor2.end _).verify(audioInputStream).once()
      }
    }

    Scenario("Stop processing after call AudioDispatcher.run()") {
      Given("a stubbed processor that will sleep 200 milliseconds every run")
      val processor = stub[AudioProcessor]
      (processor.process _).when(*).onCall { _: AudioEvent => Thread.sleep(200) }

      And("an AudioDispatcher with an 32 bit AudioInputStream with those processors")
      val audioInputStream = AudioSystem.getAudioInputStream(this.getClass.getResourceAsStream("/sounds/32.wav"))
      val dispatcher = new AudioDispatcher(audioInputStream, 8, processor :: Nil)

      And("run the dispatcher in another thread")
      val thread = new Thread(dispatcher)
      thread.start()

      And("stop it after 500 milliseconds")
      Thread.sleep(500)
      dispatcher.stop()

      When("the thread is stopped")
      thread.join()

      Then("the processor.process should be only called three times")
      info("first time - at 0.0 second")
      info("second time - at 200 milliseconds")
      info("third time - at 400 milliseconds")
      (processor.process _).verify(*).repeat(3).times()


    }

  }

}

case class Result(totalBytesRead: Long, samples: List[Float], rawBytes: List[Byte])
