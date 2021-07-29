package moe.brianhsu.porting.live2d.utils

import scala.io.Source

case class ExpectedDrawableIndex(id: String, index: Int, value: Short)

object ExpectedDrawableIndex {
  def getList: List[ExpectedDrawableIndex] = {
    val lines = Source.fromResource("expectation/drawableIndexList.txt").getLines().drop(1)

    lines.map { line =>
      val Array(id, index, x) = line.split(" ")
      ExpectedDrawableIndex(id, index.toInt, x.toShort)
    }.toList
  }

}