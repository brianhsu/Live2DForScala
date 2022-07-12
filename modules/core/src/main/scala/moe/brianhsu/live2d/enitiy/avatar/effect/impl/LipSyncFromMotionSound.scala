package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.audio.{AudioDispatcher, AudioPlayer, AudioRMSCalculator, AudioStreamCloser}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMotionSound.{DefaultProcessorsFactory, DefaultWeight, Processors, ProcessorsFactory}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import java.io.File
import javax.sound.sampled.AudioSystem
import scala.util.Try

object LipSyncFromMotionSound {
  private val SampleBufferSize = 4
  private val DefaultWeight = 5.0f

  case class Processors(dispatcher: AudioDispatcher, audioRMSCalculator: AudioRMSCalculator, audioPlayer: AudioPlayer, audioStreamCloser: AudioStreamCloser)

  trait ProcessorsFactory {
    def apply(soundFile: String, defaultVolume: Int): Try[Processors]
  }

  object DefaultProcessorsFactory extends ProcessorsFactory {
    override def apply(soundFile: String, volume: Int): Try[Processors] = {
      for {
        audioInputStream <- Try(AudioSystem.getAudioInputStream(new File(soundFile)))
        audioPlayer = AudioPlayer(audioInputStream, volume)
        audioRMSCalculator = new AudioRMSCalculator
        audioCloser = new AudioStreamCloser
        audioDispatcher = new AudioDispatcher(audioInputStream, SampleBufferSize, audioRMSCalculator :: audioPlayer :: audioCloser :: Nil)
      } yield {
        Processors(audioDispatcher, audioRMSCalculator, audioPlayer, audioCloser)
      }
    }
  }
}

class LipSyncFromMotionSound(processorsFactory: ProcessorsFactory, override val lipSyncIds: List[String], defaultVolume: Int = 100) extends LipSync {

  private var mVolume: Int = defaultVolume
  private[impl] var processorsHolder: Option[Processors] = None

  def this(avatarSettings: Settings, defaultVolume: Int) = {
    this(DefaultProcessorsFactory, avatarSettings.lipSyncParameterIds, defaultVolume)
  }

  override def currentRms: Float = processorsHolder.map(_.audioRMSCalculator.currentRMS).getOrElse(0)

  def volume: Int = mVolume

  def volume_=(value: Int): Unit = {
    processorsHolder.foreach(_.audioPlayer.volume = value)
    this.mVolume = value
  }

  override def start(): Unit = {}

  def stop(): Unit = {
    processorsHolder.foreach(_.dispatcher.stop())
  }

  def startWith(soundFileHolder: Option[String]): Unit = {
    processorsHolder.foreach(_.dispatcher.stop())
    processorsHolder = for {
      soundFile <- soundFileHolder
      processors <- processorsFactory(soundFile, mVolume).toOption
    } yield {
      processors
    }
    processorsHolder.foreach(processors => new Thread(processors.dispatcher).start())
  }

  override var weight: Float = DefaultWeight
}
