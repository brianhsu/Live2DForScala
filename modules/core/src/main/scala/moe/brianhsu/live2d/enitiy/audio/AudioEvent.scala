package moe.brianhsu.live2d.enitiy.audio

import javax.sound.sampled.AudioFormat

case class AudioEvent(format: AudioFormat, samples: Array[Float], rawBytes: Array[Byte], totalBytesRead: Long) {
  def calculateRms(): Float = {
    val sumOfSquares: Float = samples.map(x => x * x).sum
    Math.sqrt(sumOfSquares / samples.length).toFloat
  }
}
