package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.audio.{AudioDispatcher, AudioPlayer, AudioRMSCalculator}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMotionSound.{DefaultWeight, SampleBufferSize}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import java.io.File
import javax.sound.sampled.AudioSystem
import scala.util.Try

object LipSyncFromMotionSound {
  private val SampleBufferSize = 44100 / 60 // 44100Hz at 60 fps
  private val DefaultWeight = 5.0f
}

class LipSyncFromMotionSound(avatarSettings: Settings, private var mVolume: Int = 100) extends LipSync {

  private val audioRMSCalculator = new AudioRMSCalculator
  private var rmsCalculatorHolder: Option[AudioPlayer] = None
  private var audioDispatcherHolder: Option[AudioDispatcher] = None

  def volume: Int = mVolume

  override def lipSyncIds: List[String] = avatarSettings.lipSyncParameterIds
  override def currentRms: Float = audioRMSCalculator.currentRMS

  def volume_=(value: Int): Unit = {
    rmsCalculatorHolder.foreach(_.updateVolume(value))
    this.mVolume = value
  }

  def stop(): Unit = {
    audioDispatcherHolder.foreach(_.stop())
  }

  def startWith(soundFileHolder: Option[String]): Unit = {
    audioDispatcherHolder.foreach(_.stop())
    for {
      soundFile <- soundFileHolder
      audioInputStream <- Try(AudioSystem.getAudioInputStream(new File(soundFile)))
      audioPlayer = AudioPlayer(audioInputStream, mVolume)
      audioDispatcher = new AudioDispatcher(audioInputStream, SampleBufferSize, audioRMSCalculator :: audioPlayer :: Nil)
    } {
      this.rmsCalculatorHolder = Some(audioPlayer)
      this.audioDispatcherHolder = Some(audioDispatcher)
      val thread = new Thread(audioDispatcher)
      thread.start()
    }
  }

  override var weight: Float = DefaultWeight
}
