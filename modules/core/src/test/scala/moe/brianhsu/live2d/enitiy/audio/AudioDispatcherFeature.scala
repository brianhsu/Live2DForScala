package moe.brianhsu.live2d.enitiy.audio

import org.json4s.native.{Serialization, parseJson}
import org.json4s.{Formats, NoTypeHints}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, PrintWriter}
import javax.sound.sampled.{AudioFormat, AudioSystem}
import scala.io.Source
import scala.util.Using

class AudioDispatcherFeature extends AnyFeatureSpec with Matchers with GivenWhenThen {
  private implicit val formats: Formats = Serialization.formats(NoTypeHints)


  Feature("Convert AudioInputStream to AudioEvent correctly") {
    Scenario("Convert 8 bit audio stream with 8 samples per batch") {
      Given("an AudioDispatcher with an 8 bit AudioInputStream")
      val audioInputStream = AudioSystem.getAudioInputStream(new File("src/test/resources/sounds/8.wav"))
      val dispatcher = new AudioDispatcher(audioInputStream, 8)

      And("append a ReadAllSamples processor to it")
      val processor = new ReadAllSamples
      dispatcher.appendProcessor(processor)

      When("run the dispatcher")
      dispatcher.run()

      Then("the processed events in the processor should contains the correct data")
      val events = processor.processedEvents
      val expectationFile = Source.fromFile("src/test/resources/expectation/sounds/8bit.json")
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
      pending
    }
    Scenario("Convert 24bit audio stream") {
      pending
    }
    Scenario("Convert 32bit audio stream") {
      pending
    }

    class ReadAllSamples extends AudioProcessor {
      var processedEvents: List[AudioEvent] = Nil

      override def process(event: AudioEvent): Unit = {
        processedEvents ::= event
      }

      override def end(): Unit = {
        processedEvents = processedEvents.reverse
      }
    }
    class WriteAllSamplesToFile(file: String) extends AudioProcessor {

      private val printWriter = new PrintWriter(file)
      override def process(event: AudioEvent): Unit = {
        import org.json4s._
        import org.json4s.native.Serialization
        import org.json4s.native.Serialization.write

        implicit val formats: Formats = Serialization.formats(NoTypeHints)
        printWriter.println(write(Result(event.totalBytesRead, event.samples, event.rawBytes)))
      }

      override def end(): Unit = {
        printWriter.close()

      }
    }

  }
}
case class Result(totalBytesRead: Long, samples: List[Float], rawBytes: List[Byte])
