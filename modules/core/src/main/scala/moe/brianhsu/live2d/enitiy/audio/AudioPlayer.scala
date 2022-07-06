package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.{AudioInputStream, AudioSystem, DataLine, FloatControl, SourceDataLine}

object AudioPlayer {
  def apply(audio: AudioInputStream, defaultVolume: Int): AudioPlayer = {
    val info = new DataLine.Info(classOf[SourceDataLine], audio.getFormat)
    val line = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]
    new AudioPlayer(line, defaultVolume)
  }
}

class AudioPlayer(sourceDataLine: SourceDataLine, private[this] var mVolume: Int) extends AudioProcessor {

  def volume: Int = mVolume

  def volume_=(volume: Int): Unit = {
    sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN)
      .asInstanceOf[FloatControl]
      .setValue(20.0f * Math.log10(volume / 100.0).toFloat)
  }

  override def start(): Unit = {
    sourceDataLine.open()
    sourceDataLine.start()

    this.volume = volume
  }

  override def onProcess(event: AudioEvent): Unit = {
    sourceDataLine.write(event.rawBytes.toArray, 0, event.rawBytes.length)
  }

  override def end(audioInputStream: AudioInputStream): Unit = {
    sourceDataLine.drain()
    sourceDataLine.close()
  }
}
