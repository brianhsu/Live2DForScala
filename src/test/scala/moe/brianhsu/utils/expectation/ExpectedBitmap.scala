package moe.brianhsu.utils.expectation

import scala.io.Source

object ExpectedBitmap {
  def getBitmap(filename: String): List[Byte] = {
    Source.fromResource(filename)
      .getLines()
      .map(_.toByte)
      .toList
  }

}
