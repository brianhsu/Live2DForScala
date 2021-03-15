package moe.brianhsu.live2d.utils

import scala.io.Source

case class ExpectedDrawableMask(id: String, index: Int, maskValue: Short)

object ExpectedDrawableMask {
  def getList: List[ExpectedDrawableMask] = {
    val lines = Source.fromResource("expectation/DrawableMaskList.txt").getLines().drop(1)

    lines.map { line =>
      val Array(id, index, maskValue) = line.split(" ")
      ExpectedDrawableMask(id, index.toInt, maskValue.toShort)
    }.toList
  }

}

