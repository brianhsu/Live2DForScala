package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.audio.{AudioDispatcher, AudioRMSCalculator, AudioStreamCloser}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMic.DefaultWeight
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import javax.sound.sampled._
import scala.util.Try

object LipSyncFromMic {
  private val DefaultWeight = 5f

  def availableMixers = {
    AudioSystem.getMixerInfo
      .map(AudioSystem.getMixer)
      .filter(isInputMixer)
      .toList
  }

  def isInputMixer(mixer: Mixer): Boolean = {
    mixer.getTargetLineInfo
      .map(lineInfo => mixer.getLine(lineInfo))
      .exists(_.isInstanceOf[TargetDataLine])
  }

  def apply(avatarSettings: Settings, mixer: Mixer): Try[LipSyncFromMic] = Try {
    val line = mixer.getTargetLineInfo()
      .map(mixer.getLine)
      .filter(_.isInstanceOf[TargetDataLine])
      .map(_.asInstanceOf[TargetDataLine])
      .head

    //val format = new AudioFormat(44100, 16, 1, true, false)

    line.open()

    line.start()

    val audioInputStream = new AudioInputStream(line)
    val audioRMSCalculator = new AudioRMSCalculator
    val audioStreamCloser = new AudioStreamCloser
    val info = new DataLine.Info(classOf[SourceDataLine], audioInputStream.getFormat)
    val dispatcher = new AudioDispatcher(audioInputStream, 16, audioRMSCalculator :: audioStreamCloser :: Nil)

    new LipSyncFromMic(
      avatarSettings.lipSyncParameterIds, dispatcher,
      audioRMSCalculator
    )
  }
}

class LipSyncFromMic(override val lipSyncIds: List[String],
                     audioDispatcher: AudioDispatcher,
                     audioRMSCalculator: AudioRMSCalculator) extends LipSync {

  new Thread(audioDispatcher).start()

  override def currentRms: Float = audioRMSCalculator.currentRMS

  def stop(): Unit = {
    audioDispatcher.stop()
  }

  override var weight: Float = DefaultWeight
}
