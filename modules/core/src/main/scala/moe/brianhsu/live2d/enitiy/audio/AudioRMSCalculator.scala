package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.AudioInputStream

class AudioRMSCalculator extends AudioProcessor {
  private var mCurrentRMS: Float = 0.0f

  def currentRMS: Float = mCurrentRMS

  override def start(): Unit = {}

  override def onProcess(event: AudioEvent): Unit = {
    if (event.samples.nonEmpty) {
      this.mCurrentRMS = Math.sqrt(event.samples.map(x => x * x).sum / event.samples.length).toFloat
    } else {
      this.mCurrentRMS = 0
    }
  }

  override def end(audioInputStream: AudioInputStream): Unit = {
    this.mCurrentRMS = 0
  }
}
