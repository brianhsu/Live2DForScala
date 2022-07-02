package moe.brianhsu.live2d.sound

import java.io.{File, PrintWriter}
import java.nio.{ByteBuffer, ByteOrder}
import javax.sound.sampled.{AudioFormat, AudioInputStream, AudioSystem, Control, DataLine, SourceDataLine}





class AudioPlayer(audioFormat: AudioFormat) extends AudioProcessor {


  private val info = new DataLine.Info(classOf[SourceDataLine], audioFormat, 32 * 10)
  private val sourceLine = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]

  sourceLine.open(audioFormat)
  sourceLine.start()
  var count = 0

  override def process(event: AudioEvent): Unit = {
    val xs = event.rawBytes.grouped(event.format.getFrameSize).toList

    //xs.foreach(b => sourceLine.write(b, 0, b.length))

    println("raw:" + event.rawBytes.length)
    println("buffer:" + sourceLine.getLineInfo)

    sourceLine.write(event.rawBytes, 0, event.rawBytes.length)
    //println(event.calculateRms())
  }

  override def end(): Unit = {
    sourceLine.drain()
    sourceLine.close()
  }
}

object AudioProcessor {
  def main(args: Array[String]): Unit = {
    val audioInputStream = AudioSystem.getAudioInputStream(new File("24.wav"))
    val audioProcessor = new AudioDispatcher(audioInputStream, 306)
    println(audioInputStream.getFormat.getSampleSizeInBits)
    audioProcessor.appendProcessor(new AudioPlayer(audioInputStream.getFormat))
    audioProcessor.run()
  }
}
