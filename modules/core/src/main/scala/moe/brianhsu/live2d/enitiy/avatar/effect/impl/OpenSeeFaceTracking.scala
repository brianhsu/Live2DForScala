package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.openSeeFace.OpenSeeFaceDataReader
import moe.brianhsu.live2d.enitiy.avatar.effect.data.OpenSeeFaceDataConverter
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceTracking.TrackingTaps
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.OpenSeeFaceTracking._

import scala.util.Using

object OpenSeeFaceTracking {
  val DefaultTrackingTaps: TrackingTaps = TrackingTaps(7, 7, 7, 3, 3, 3, 3, 1, 1, 7)
}

class OpenSeeFaceTracking(dataReader: OpenSeeFaceDataReader,
                          idleTimeoutInMs: Int,
                          trackingTaps: TrackingTaps = DefaultTrackingTaps,
                          dataConverter: OpenSeeFaceDataConverter = new OpenSeeFaceDataConverter) extends FaceTracking(trackingTaps) {

  private[impl] val readerThread: ReaderThread = new ReaderThread

  def start(): Unit = {
    readerThread.start()
  }

  def stop(): Unit = {
    readerThread.shouldBeStopped = true
    readerThread.join()
  }

  private[impl] class ReaderThread extends Thread {
    var shouldBeStopped: Boolean = false

    override def run(): Unit = {

      Using.resource(dataReader) { reader =>

        reader.open()

        var lastUpdateTime = System.currentTimeMillis

        while (!shouldBeStopped) {
          val trackingNodeHolder = reader
            .readData()
            .map { data =>
              val leftEyePreviousNodes = trackingNoes.take(trackingTaps.leftEyeOpenness)
              val rightEyePreviousNodes = trackingNoes.take(trackingTaps.rightEyeOpenness)

              dataConverter.convert(data, leftEyePreviousNodes, rightEyePreviousNodes)
            }

          trackingNodeHolder.foreach { node =>
            trackingNoes = (node :: trackingNoes).take(trackingTaps.maxTaps)
            lastUpdateTime = System.currentTimeMillis
          }

          if (System.currentTimeMillis - lastUpdateTime > idleTimeoutInMs) {
            trackingNoes = Nil
          }
        }
      }
    }
  }

}
