package moe.brianhsu.utils.mock

import org.scalamock.scalatest.MockFactory

import javax.sound.sampled.{AudioFormat, DataLine, Mixer, TargetDataLine}

trait MixerMock {
  this: MockFactory =>

  def createStubbedMixer(): (Mixer, TargetDataLine) = {
    val audioFormat = new AudioFormat(44100, 16, 1, true,false)
    val mixer = stub[Mixer]
    val targetDataLine = stub[TargetDataLine]
    val targetLineInfo = new DataLine.Info(classOf[TargetDataLine], audioFormat)
    (() => mixer.getTargetLineInfo).when().returns(Array(targetLineInfo))
    (mixer.getLine _).when(*).returns(targetDataLine)
    (() => targetDataLine.getFormat).when().returns(audioFormat)
    (targetDataLine.read _).when(*, *, *).returns(-1)

    (mixer, targetDataLine)
  }

}
