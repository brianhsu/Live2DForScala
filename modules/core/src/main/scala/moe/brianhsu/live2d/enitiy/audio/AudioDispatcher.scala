package moe.brianhsu.live2d.enitiy.audio

import java.nio.{ByteBuffer, ByteOrder}
import javax.sound.sampled.AudioInputStream

class AudioDispatcher(audioInputStream: AudioInputStream, bufferSampleCount: Int) extends Runnable {
  private var processorList: List[AudioProcessor] = Nil
  private val audioFormat = audioInputStream.getFormat
  private val endian = if (audioFormat.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN
  private val frameSize = audioFormat.getFrameSize
  private val channelCount = audioFormat.getChannels
  private val bitsPerSample = audioFormat.getSampleSizeInBits

  def appendProcessor(processor: AudioProcessor): Unit = {
    processorList = processorList.appended(processor)
  }

  def removeProcessor(processor: AudioProcessor): Unit = {
    processorList = processorList.filterNot(_ == processor)
  }

  override def run(): Unit = {
    val buffer = new Array[Byte](bufferSampleCount * frameSize * channelCount)
    var totalBytesRead: Long = 0

    while (audioInputStream.available() > 0) {
      val bytesRead = audioInputStream.read(buffer)
      val samples = bitsPerSample match {
        case 8  => decode8BitSamples(buffer)
        case 16 => decode16BitSamples(buffer, bytesRead)
        case 24 => decode24BitSamples(buffer, bytesRead)
        case 32 => decode32BitSamples(buffer, bytesRead)
      }
      val event = AudioEvent(audioFormat, samples, buffer.take(bytesRead), totalBytesRead)
      processorList.foreach(_.process(event))
      totalBytesRead += 1
    }

    processorList.foreach(_.end())
  }

  private def decode8BitSamples(buffer: Array[Byte]) = {
    buffer.map(x => ((x - 128) << 24) / Int.MaxValue.toFloat)
  }

  private def decode16BitSamples(buffer: Array[Byte], readInBytes: Int): Array[Float] = {
    val shortByteBuffer = ByteBuffer.allocate(readInBytes)
      .order(endian)
      .put(buffer.take(readInBytes))
      .rewind()
      .asShortBuffer()
    val totalSamples: Int = readInBytes / 2
    val shortArrays = new Array[Short](totalSamples)
    shortByteBuffer.get(shortArrays)
    shortArrays.map(x => (x.toInt << 16) / Int.MaxValue.toFloat)
  }

  private def decode24BitSamples(buffer: Array[Byte], readInBytes: Int): Array[Float] = {

    val totalSamples: Int = readInBytes / 3
    val intArray = new Array[Int](totalSamples)
    val intBuffer = ByteBuffer.allocate(readInBytes + readInBytes / 3).order(endian)
    buffer.take(readInBytes).grouped(3).foreach { bytes =>
      intBuffer.put(0.toByte)
      intBuffer.put(bytes)
    }

    intBuffer.rewind().asIntBuffer().get(intArray)
    intArray.map(_ / Int.MaxValue.toFloat)
  }


  private def decode32BitSamples(buffer: Array[Byte], readInBytes: Int): Array[Float] = {
    val intByteBuffer = ByteBuffer.allocate(readInBytes)
      .order(endian)
      .put(buffer.take(readInBytes))
      .rewind()
      .asIntBuffer()
    val totalSamples: Int = readInBytes / 4
    val intArrays = new Array[Int](totalSamples)
    intByteBuffer.get(intArrays)
    intArrays.map(_ / Int.MaxValue.toFloat)
  }

}
