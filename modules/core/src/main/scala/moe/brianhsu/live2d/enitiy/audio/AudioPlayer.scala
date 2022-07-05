package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.{AudioInputStream, AudioSystem, DataLine, FloatControl, SourceDataLine}

object AudioPlayer {
  def apply(audio: AudioInputStream, defaultVolume: Int): AudioPlayer = {
    val info = new DataLine.Info(classOf[SourceDataLine], audio.getFormat)
    val line = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]
    new AudioPlayer(line, audio, defaultVolume)
  }
}

class AudioPlayer(line: SourceDataLine, audio: AudioInputStream, defaultVolume: Int) extends AudioProcessor {

  line.open()
  line.start()
  updateVolume(defaultVolume)

  def updateVolume(volume: Int): Unit = {
    line.getControl(FloatControl.Type.MASTER_GAIN)
      .asInstanceOf[FloatControl]
      .setValue(20.0f * Math.log10(volume / 100.0).toFloat)
  }

  override def process(event: AudioEvent): Unit = {
    line.write(event.rawBytes.toArray, 0, event.rawBytes.length)
  }

  override def end(): Unit = {
    line.drain()
    audio.close()
  }
}
