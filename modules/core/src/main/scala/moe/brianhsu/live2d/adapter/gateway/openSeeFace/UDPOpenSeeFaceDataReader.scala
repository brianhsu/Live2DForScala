package moe.brianhsu.live2d.adapter.gateway.openSeeFace

import moe.brianhsu.live2d.adapter.gateway.openSeeFace.UDPOpenSeeFaceDataReader.{DatagramPacketSize, TotalPoints}
import moe.brianhsu.live2d.boundary.gateway.openSeeFace.OpenSeeFaceDataReader
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData.{EyeOpenness, Features, Quaternion, Resolution}
import moe.brianhsu.live2d.exception.OpenSeeFaceException

import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress}
import java.nio.{ByteBuffer, ByteOrder}
import scala.util.Using

object UDPOpenSeeFaceDataReader {
  private val TotalPoints = 68
  private val DatagramPacketSize =
    8 + // Time
      4 + // ID
      2 * 4 + // Resolution
      2 * 4 + // Eye Openness
      1 + // Got 3D Points
      4 + // fit3DError
      4 * 4 + // Raw Quaternion
      3 * 4 + // Raw Euler
      3 * 4 + // Translation
      TotalPoints * 4 + // Confidence
      TotalPoints * 2 * 4  + // Landmarks
      (TotalPoints + 2) * 3 * 4 + // 3D points
      14 * 4 // 14 Features

}

class UDPOpenSeeFaceDataReader(socket: DatagramSocket, hostname: String, port: Int)(implicit manager: Using.Manager) extends OpenSeeFaceDataReader {

  private val buffer = new Array[Byte](DatagramPacketSize)
  private val packet = new DatagramPacket(buffer, DatagramPacketSize)

  def this(hostname: String, port: Int)(implicit manager: Using.Manager) = this(new DatagramSocket(null), hostname, port)

  manager.acquire(this)

  override def open(): Unit = {
    try {
      socket.bind(new InetSocketAddress(hostname, port))
    } catch {
      case e: Exception => throw new OpenSeeFaceException(s"Cannot connect to UDP socket ($hostname, $port)", e)
    }
  }

  override def readData(): OpenSeeFaceData = {
    socket.receive(packet)

    val time = convertToDouble(buffer.slice(0, 8))
    val id = convertToInt(buffer.slice(8, 12))
    val (width, height) = convertToFloatTuple2(buffer.slice(12, 20))
    val (rightEyeOpenness, leftEyeOpenness) = convertToFloatTuple2(buffer.slice(20, 28))
    val resolution = Resolution(width, height)
    val eyeOpenness = EyeOpenness(rightEyeOpenness, leftEyeOpenness)
    val got3DPoints = convertToBoolean(buffer.slice(28, 29))
    val fit3DError = convertToFloat(buffer.slice(29, 33))
    val rawQuaternion = convertToQuaternion(buffer.slice(33, 49))
    val rawEuler = convertToEuler(buffer.slice(49, 61))
    val translation = convertToTranslation(buffer.slice(61, 73))
    val confidence = (0 until TotalPoints).map { index =>
      val offsetStart = 73 + index * 4
      val offsetEnd = offsetStart + 4
      convertToFloat(buffer.slice(offsetStart, offsetEnd))
    }.toList

    val landmarks = (0 until TotalPoints).map { index =>
      val offsetStart = 345 + index * 8
      val offsetEnd = offsetStart + 8
      convertToFloatTuple2(buffer.slice(offsetStart, offsetEnd))
    }.toList

    // 889
    val points3D = (0 until TotalPoints + 2).map { index =>
      val offsetStart = 889 + index * 12
      val offsetEnd = offsetStart + 12
      convertTo3DPoints(buffer.slice(offsetStart, offsetEnd))
    }.toList

    val features = convertToFeatures(buffer.slice(1729, 1785))

    OpenSeeFaceData(
      time, id, resolution, eyeOpenness, got3DPoints,
      fit3DError, rawQuaternion, rawEuler, translation,
      confidence, landmarks, points3D, features
    )
  }

  override def close(): Unit = {
    socket.close()
  }

  private def convertToFeatures(bytes: Array[Byte]): Features = {
    Features(
      convertToFloat(bytes.slice(0, 4)),
      convertToFloat(bytes.slice(4, 8)),
      convertToFloat(bytes.slice(8, 12)),
      convertToFloat(bytes.slice(12, 16)),
      convertToFloat(bytes.slice(16, 20)),
      convertToFloat(bytes.slice(20, 24)),
      convertToFloat(bytes.slice(24, 28)),
      convertToFloat(bytes.slice(28, 32)),
      convertToFloat(bytes.slice(32, 36)),
      convertToFloat(bytes.slice(36, 40)),
      convertToFloat(bytes.slice(40, 44)),
      convertToFloat(bytes.slice(44, 48)),
      convertToFloat(bytes.slice(48, 52)),
      convertToFloat(bytes.slice(52, 56))
    )
  }

  private def convertToDouble(bytes: Array[Byte]): Double = {
    ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder()).asDoubleBuffer().get()
  }

  private def convertToFloat(bytes: Array[Byte]): Float = {
    ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder()).asFloatBuffer().get()
  }

  private def convertToInt(bytes: Array[Byte]): Int = {
    ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder()).asIntBuffer().get()
  }

  private def convertToFloatTuple2(bytes: Array[Byte]): (Float, Float) = {
    val x = convertToFloat(bytes.take(4))
    val y = convertToFloat(bytes.slice(4, 8))
    (x, y)
  }

  private def convertToEuler(bytes: Array[Byte]): (Float, Float, Float) = {
    val x = convertToFloat(bytes.take(4))
    val y = convertToFloat(bytes.slice(4, 8))
    val z = convertToFloat(bytes.slice(8, 12))
    (x, -y, z)
  }

  private def convertTo3DPoints(bytes: Array[Byte]): (Float, Float, Float) = {
    val x = convertToFloat(bytes.take(4))
    val y = convertToFloat(bytes.slice(4, 8))
    val z = convertToFloat(bytes.slice(8, 12))
    (x, -y, z)
  }

  private def convertToTranslation(bytes: Array[Byte]): (Float, Float, Float) = {
    val x = convertToFloat(bytes.take(4))
    val y = convertToFloat(bytes.slice(4, 8))
    val z = convertToFloat(bytes.slice(8, 12))
    (-y, x, -z)
  }

  private def convertToQuaternion(bytes: Array[Byte]): Quaternion[Float] = {
    val x = convertToFloat(bytes.take(4))
    val y = convertToFloat(bytes.slice(4, 8))
    val z = convertToFloat(bytes.slice(8, 12))
    val w = convertToFloat(bytes.slice(12, 16))

    Quaternion(x, y, z, w)
  }

  private def convertToBoolean(bytes: Array[Byte]): Boolean = {
    bytes.headOption.exists(_ != 0)
  }

}
