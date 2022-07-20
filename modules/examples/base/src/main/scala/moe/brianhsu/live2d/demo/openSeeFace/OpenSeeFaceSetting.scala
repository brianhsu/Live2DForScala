package moe.brianhsu.live2d.demo.openSeeFace

import java.io.File

object OpenSeeFaceSetting {

  private val appFolder = new File(this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath).getParent

  def bundleModelDir: String = s"$appFolder/openSeeFace/models"

  def bundleExecution: String =  {
    System.getProperty("os.name").toLowerCase match {
      case osName if osName.contains("linux") => s"$appFolder/openSeeFace/linux/facetracker"
      case osName if osName.contains("win") => s"$appFolder/openSeeFace/windows/facetracker.exe"
      case osName if osName.contains("mac")  => s"$appFolder/openSeeFace/macos/facetracker"
      case osName => throw new RuntimeException(s"Unknown operating system $osName")
    }
  }


}

trait OpenSeeFaceSetting {
  def getCommand: String
  def getHostname: String
  def getPort: Int
}
