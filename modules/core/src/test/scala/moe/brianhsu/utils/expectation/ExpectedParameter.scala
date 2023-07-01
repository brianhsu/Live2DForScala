package moe.brianhsu.utils.expectation

import scala.io.Source

case class ExpectedParameter(id: String, current: Float, default: Float, min: Float, max: Float)

object ExpectedParameter {
  def getExpectedKeyValues: Map[String, List[Float]] = {
    var idToKeyValues = Map.empty[String, List[Float]]
    val lines = Source.fromResource("expectation/parameterKeyValueList.txt").getLines()

    lines.foreach { line =>
      val columns = line.split(" ").toList
      val id = columns.head
      val keyValues = columns.drop(1).map(_.toFloat)

      idToKeyValues += (id -> keyValues)
    }

    idToKeyValues
  }

  def getExpectedParameters: List[ExpectedParameter] = {
    val lines = Source.fromResource("expectation/parameterList.txt").getLines()
    lines.map { line =>
      val Array(id, current, default, min, max) = line.split(" ")
      ExpectedParameter(id, current.toFloat, default.toFloat, min.toFloat, max.toFloat)
    }.toList
  }
}
