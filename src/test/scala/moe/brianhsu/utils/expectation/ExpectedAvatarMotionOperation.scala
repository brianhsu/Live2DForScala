package moe.brianhsu.utils.expectation

import moe.brianhsu.live2d.enitiy.avatar.updater.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate, UpdateOperation}
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

}

case class ExpectedAvatarMotionOperation(input: Input, output: Output)
