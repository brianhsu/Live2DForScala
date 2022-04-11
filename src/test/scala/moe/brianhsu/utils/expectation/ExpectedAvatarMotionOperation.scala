package moe.brianhsu.utils.expectation

import moe.brianhsu.live2d.usecase.updater.UpdateOperation
import moe.brianhsu.live2d.usecase.updater.UpdateOperation.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.utils.expectation.ExpectedAvatarMotionOperation.{Input, Output}
import org.json4s.{Formats, ShortTypeHints}
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization

import scala.io.Source
import scala.util.Using

object ExpectedAvatarMotionOperation {
  def fromFile(filename: String): List[ExpectedAvatarMotionOperation] = {
    val dataFile = Source.fromFile(filename)
    Using.resource(dataFile) { _.getLines().toList.map(parseLog) }
  }

  private def parseLog(line: String): ExpectedAvatarMotionOperation = parse(line).extract[ExpectedAvatarMotionOperation]
  private implicit val formats: Formats = Serialization.formats(ShortTypeHints(
    List(
      classOf[ParameterValueAdd],
      classOf[ParameterValueMultiply],
      classOf[ParameterValueUpdate],
      classOf[FallbackParameterValueAdd],
      classOf[FallbackParameterValueUpdate],
      classOf[PartOpacityUpdate],
    )
  ))
  case class Output(operations: List[UpdateOperation])
  case class Input(totalElapsedTimeInSeconds: Float,
                   deltaTimeInSeconds: Float, weight: Float,
                   startTimeInSeconds: Float,
                   fadeInStartTimeInSeconds: Float,
                   endTimeInSeconds: Option[Float],
                   parameters: Map[String, Float])

  def main(args: Array[String]): Unit = {
    val xs = List(
      ParameterValueAdd("aa", 10.0f, 0.6f),
      ParameterValueUpdate("bb", 5.0f, 1.0f)
    )
    val t = org.json4s.native.Serialization.write(xs)
    println(t)
  }
}

case class ExpectedAvatarMotionOperation(input: Input, output: Output)
