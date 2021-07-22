package moe.brianhsu.live2d.utils

import scala.io.Source

case class ExpectedDrawablePosition(id: String, index: Int, x: Float, y: Float)

object ExpectedDrawablePosition {
  def getList: List[ExpectedDrawablePosition] = {
    val lines = Source.fromResource("expectation/drawablePositionListLinux.txt").getLines().drop(1)

    lines.map { line =>
      val Array(id, index, x, y) = line.split(" ")
      ExpectedDrawablePosition(id, index.toInt, x.toFloat, y.toFloat)
    }.toList
  }
}

