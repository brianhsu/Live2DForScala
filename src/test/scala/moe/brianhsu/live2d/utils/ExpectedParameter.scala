package moe.brianhsu.live2d.utils

import scala.io.Source

case class ExpectedParameter(id: String, current: Float, default: Float, min: Float, max: Float)

object ExpectedParameter {
  def getExpectedParameters: List[ExpectedParameter] = {
    val lines = Source.fromResource("expectation/parameterList.txt").getLines()
    lines.map { line =>
      val Array(id, current, default, min, max) = line.split(" ")
      ExpectedParameter(id, current.toFloat, default.toFloat, min.toFloat, max.toFloat)
    }.toList
  }
}
