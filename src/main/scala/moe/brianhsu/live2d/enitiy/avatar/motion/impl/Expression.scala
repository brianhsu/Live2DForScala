package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.{UpdateOperation, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression.{Add, Multiply, Overwrite}
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object Expression {
  sealed trait BlendType
  case object Add extends BlendType
  case object Multiply extends BlendType
  case object Overwrite extends BlendType

  case class Parameter(parameterId: String, blendType: BlendType, value: Float)
}

case class Expression(fadeInTimeInSeconds: Option[Float],
                      fadeOutTimeInSeconds: Option[Float],
                      parameters: List[Expression.Parameter]) extends Motion {

  override val isLoop: Boolean = false
  override val isLoopFadeIn: Boolean = false
  override val events: List[MotionEvent] = Nil
  override val durationInSeconds: Option[Float] = None

  override def calculateOperations(model: Live2DModel,
                                   totalElapsedTimeInSeconds: Float,
                                   deltaTimeInSeconds: Float,
                                   weight: Float,
                                   startTimeInSeconds: Float,
                                   fadeInStartTimeInSeconds: Float,
                                   endTimeInSeconds: Option[Float]): List[UpdateOperation] = {

    parameters.map { parameter =>
      parameter.blendType match {
        case Add       => ParameterValueAdd(parameter.parameterId, parameter.value, weight)
        case Multiply  => ParameterValueMultiply(parameter.parameterId, parameter.value, weight)
        case Overwrite => ParameterValueUpdate(parameter.parameterId, parameter.value, weight)
      }
    }
  }
}
