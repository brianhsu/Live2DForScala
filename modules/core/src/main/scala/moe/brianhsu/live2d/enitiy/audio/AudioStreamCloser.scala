package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.AudioInputStream

class AudioStreamCloser extends AudioProcessor {

  override def start(): Unit = {}

  override def onProcess(event: AudioEvent): Unit = {}

  override def end(audioInputStream: AudioInputStream): Unit = {
    audioInputStream.close()
  }
}
