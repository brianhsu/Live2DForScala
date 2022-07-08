package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.{AudioInputStream, DataLine}

class AudioLineCloser(line: DataLine) extends AudioProcessor {

  override def start(): Unit = {}

  override def onProcess(event: AudioEvent): Unit = {}

  override def end(audioInputStream: AudioInputStream): Unit = {
    line.drain()
    line.stop()
    line.close()
  }
}
