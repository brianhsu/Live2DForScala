package moe.brianhsu.live2d.enitiy.audio

import org.json4s.native.Serialization
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, PrintWriter}
import javax.sound.sampled.{AudioInputStream, AudioSystem}

class AudioDispatcherFeature extends AnyFeatureSpec with Matchers with GivenWhenThen {

  Feature("Convert AudioInputStream to AudioEvent correctly") {
    Scenario("Convert 8 bit audio stream with 8 samples per batch") {
      Given("an AudioDispatcher with an 8 bit AudioInpuStream")
      val audioInputStream = AudioSystem.getAudioInputStream(new File("modules/core/src/test/resources/sounds/8.wav"))
      val dispatcher = new AudioDispatcher(audioInputStream, 8)

      And("append a ReadAllSamples processor to it")
      val processor = new ReadAllSamples
      dispatcher.appendProcessor(processor)

      When("run the dispatcher")
      dispatcher.run()

      Then("the processed events in the processor should contains the correct data")
      val events = processor.processedEvents
      events.size shouldBe 7632
      println(events(0).format)
      pending
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
        import org.json4s.native.Serialization.{read, write}

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
