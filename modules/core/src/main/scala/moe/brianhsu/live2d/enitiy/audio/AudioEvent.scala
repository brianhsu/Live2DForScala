package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.AudioFormat

case class AudioEvent(format: AudioFormat, totalBytesRead: Long, samples: List[Float], rawBytes: List[Byte])