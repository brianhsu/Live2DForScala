package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.audio.{AudioDispatcher, AudioLineCloser, AudioRMSCalculator, AudioStreamCloser}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMic.DefaultWeight
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import javax.sound.sampled._
import scala.util.Try

object LipSyncFromMic {
  private val DefaultWeight = 5f
  private val InputAudioFormat = new AudioFormat(44100, 16, 1, true,false)

  def findInputMixers() = {
    AudioSystem.getMixerInfo
      .map(AudioSystem.getMixer)
      .filter(_.isLineSupported(new DataLine.Info(classOf[TargetDataLine], InputAudioFormat)))
      .toList
  }

  def apply(avatarSettings: Settings, mixer: Mixer, weight: Float, forceEvenNoSetting: Boolean): Try[LipSyncFromMic] = Try {
    val line = mixer.getTargetLineInfo()
      .map(mixer.getLine)
      .filter(_.isInstanceOf[TargetDataLine])
      .map(_.asInstanceOf[TargetDataLine])
      .head

    line.open(InputAudioFormat)
    line.start()

    val audioInputStream = new AudioInputStream(line)
    val audioRMSCalculator = new AudioRMSCalculator
    val audioStreamCloser = new AudioStreamCloser
    val audioLineCloser = new AudioLineCloser(line)
    val dispatcher = new AudioDispatcher(audioInputStream, 2500, audioRMSCalculator :: audioStreamCloser :: audioLineCloser :: Nil)

    val lipSyncIds = avatarSettings.lipSyncParameterIds match {
      case Nil if forceEvenNoSetting => List("ParamMouthOpenY")
      case ids => ids
    }

    val lipSyncFromMic = new LipSyncFromMic(lipSyncIds, dispatcher, audioRMSCalculator)

    lipSyncFromMic.weight = weight
    lipSyncFromMic
  }
}

class LipSyncFromMic(override val lipSyncIds: List[String],
                     audioDispatcher: AudioDispatcher,
                     audioRMSCalculator: AudioRMSCalculator) extends LipSync {

  private val thread = new Thread(audioDispatcher)

  thread.start()

  override def currentRms: Float = audioRMSCalculator.currentRMS

  def stop(): Unit = {
    audioDispatcher.stop()
    thread.join()
  }

  override var weight: Float = DefaultWeight
}
