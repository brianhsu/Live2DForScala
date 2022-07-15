package moe.brianhsu.live2d.demo.openSeeFace

import moe.brianhsu.live2d.demo.openSeeFace.CameraListing.CameraItem
import moe.brianhsu.live2d.demo.openSeeFace.LinuxCameraListing.Video4Linux

import java.nio.file.{Files, Path, Paths}
import scala.io.Source
import scala.jdk.StreamConverters.StreamHasToScala
import scala.util.{Try, Using}

object LinuxCameraListing {

  case class Video4Linux(index: Int, isRealInput: Boolean, name: String)
}

class LinuxCameraListing extends CameraListing {

  override def listing: List[CameraItem] = {
    val video4LinuxDirectory = Paths.get("/sys/class/video4linux/")
    val videoSysClassListing: List[Path] = Using(Files.walk(video4LinuxDirectory)) { dirList =>
      dirList.toScala(LazyList).toList
    }.getOrElse(Nil)

    val inputDeviceList = for {
      videoSysClass <- videoSysClassListing
      cardInfo <- readVideo4LinuxMeta(videoSysClass).toOption if cardInfo.isRealInput
    } yield {
      CameraItem(cardInfo.name, cardInfo.index)
    }

    inputDeviceList.sortBy(_.cameraId)
  }

  private def readVideo4LinuxMeta(path: Path): Try[Video4Linux] = Using.Manager { use =>
    val index = path.getFileName.toString.drop("video".length).trim.toInt
    val isRealInput = use(Source.fromFile(path.resolve("index").toFile)).mkString.trim == "0"
    val name = use(Source.fromFile(path.resolve("name").toFile)).mkString.trim

    Video4Linux(index, isRealInput, name)
  }
}
