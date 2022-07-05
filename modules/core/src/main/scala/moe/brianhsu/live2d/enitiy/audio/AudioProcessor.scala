package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.AudioInputStream

trait AudioProcessor {

  private var isStarted: Boolean = false

  def start(): Unit

  def process(event: AudioEvent): Unit = {
    if (!isStarted) {
      start()
      this.isStarted = true
    }
    onProcess(event)
  }

  def onProcess(event: AudioEvent): Unit
  def end(audioInputStream: AudioInputStream): Unit
}
