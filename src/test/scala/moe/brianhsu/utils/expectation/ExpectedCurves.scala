package moe.brianhsu.utils.expectation

import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.{Model, Parameter, PartOpacity}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.SegmentType.{Bezier, BezierCardanoInterpretation, InverseStepped, Linear, Stepped}
import moe.brianhsu.live2d.enitiy.avatar.motion.data.{CurveTarget, MotionCurve, SegmentType}
import org.json4s.{CustomSerializer, Formats, JString, NoTypeHints}
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization

import scala.io.Source
import scala.util.Using

object ExpectedCurves {
  def fromFile(filename: String): List[MotionCurve] = {
    val source = Source.fromFile(filename)
    val testData = Using.resource(source) { source => source.getLines().toList }
    testData.map(line => parse(line).extract[MotionCurve])
  }


  object TargetTypeSerializer extends CustomSerializer[CurveTarget](_ => (
    {
      case JString("Model") => Model
      case JString("Parameter") => Parameter
      case JString("PartOpacity") => PartOpacity
    },
    {
      case Model => JString("Model")
      case Parameter => JString("Parameter")
      case PartOpacity => JString("PartOpacity")
    }
  ))
  object SegmentTypeSerializer extends CustomSerializer[SegmentType](_ => (
    {
      case JString("Linear") => Linear
      case JString("Bezier") => Bezier
      case JString("BezierCardanoInterpretation") => BezierCardanoInterpretation
      case JString("Stepped") => Stepped
      case JString("InverseStepped") => InverseStepped
    },
    {
      case Linear => JString("Linear")
      case Bezier => JString("Bezier")
      case BezierCardanoInterpretation => JString("BezierCardanoInterpretation")
      case Stepped => JString("Stepped")
      case InverseStepped => JString("InverseStepped")

    }
  ))

  private implicit val format: Formats = Serialization.formats(NoTypeHints) + TargetTypeSerializer + SegmentTypeSerializer

}
