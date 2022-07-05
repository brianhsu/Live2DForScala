package moe.brianhsu.utils.mock

import javax.sound.sampled.{AudioFormat, AudioInputStream}

trait AudioMock {
  class MockableAudioInputStream extends AudioInputStream(null, new AudioFormat(44100.0f, 8, 1, false, false), 123)
}
