package moe.brianhsu.live2d.demo.openSeeFace

import moe.brianhsu.live2d.demo.openSeeFace.CameraListing.CameraItem

object CameraListing {
  case class CameraItem(title: String, cameraId: Int)

  def createByOS(): CameraListing = {
    System.getProperty("os.name").toLowerCase match {
      case osName if osName.contains("linux") => new LinuxCameraListing
      case osName if osName.contains("win") => new WindowsCameraListing
      case osName if osName.contains("mac")  => new MacOSCameraListing
      case osName => throw new RuntimeException(s"Unknown operating system $osName")
    }
  }
}

trait CameraListing {
  def listing: List[CameraItem]
}

