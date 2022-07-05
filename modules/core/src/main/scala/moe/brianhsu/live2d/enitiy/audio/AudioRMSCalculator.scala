package moe.brianhsu.live2d.enitiy.audio

class AudioRMSCalculator extends AudioProcessor {
  private var mCurrentRMS: Float = 0.0f

  def currentRMS: Float = mCurrentRMS

  override def process(event: AudioEvent): Unit = {
    this.mCurrentRMS = Math.sqrt(event.samples.map(x => x * x).sum / event.samples.length).toFloat
  }

  override def end(): Unit = {}
}
