package moe.brianhsu.live2d.demo.openSeeFace

import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver
import moe.brianhsu.live2d.demo.openSeeFace.CameraListing.CameraItem

import scala.jdk.CollectionConverters._
import scala.util.Try

class WindowsCameraListing extends CameraListing {

  override def listing: List[CameraItem] = {
    val webcamDriver = new WebcamDefaultDriver
    webcamDriver.getDevices
      .asScala
      .flatMap(device => Try(CameraItem(device.getName, extractCameraId(device.getName))).toOption)
      .sortBy(_.cameraId)
      .toList
  }

  def extractCameraId(name: String) = {
    name.split(" ").last.toInt
  }
}