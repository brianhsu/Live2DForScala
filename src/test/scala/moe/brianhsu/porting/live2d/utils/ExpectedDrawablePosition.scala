package moe.brianhsu.porting.live2d.utils

import scala.io.Source

case class ExpectedDrawablePosition(id: String, index: Int, x: Float, y: Float)

object ExpectedDrawablePosition {
  def getList: List[ExpectedDrawablePosition] = {
    val fileName = System.getProperty("os.name") match {
      case "Linux" => "expectation/drawablePositionListLinux.txt"
      case "Mac OS X" => "expectation/drawablePositionListMacOSX.txt"
      case platform if platform.contains("Windows") => "expectation/drawablePositionListWindows.txt"
    }

    val lines = Source.fromResource(fileName).getLines().drop(1)

    lines.map { line =>
      val Array(id, index, x, y) = line.split(" ")
      ExpectedDrawablePosition(id, index.toInt, x.toFloat, y.toFloat)
    }.toList
  }
}

