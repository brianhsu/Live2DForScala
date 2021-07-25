package moe.brianhsu.porting.live2d.utils

import scala.io.Source

case class ExpectedDrawableCoordinate(id: String, index: Int, x: Float, y: Float)

object ExpectedDrawableCoordinate {
  def getList: List[ExpectedDrawableCoordinate] = {
    val lines = Source.fromResource("expectation/drawableCoordList.txt").getLines().drop(1)

    lines.map { line =>
      val Array(id, index, x, y) = line.split(" ")
      ExpectedDrawableCoordinate(id, index.toInt, x.toFloat, y.toFloat)
    }.toList
  }

}