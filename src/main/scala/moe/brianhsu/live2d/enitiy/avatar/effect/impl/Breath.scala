package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.{UpdateOperation, Effect, ParameterValueAdd}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import scala.math.sin

object Breath {
  case class Parameter(parameterId: String, offset: Float, peak: Float, cycle: Float, weight: Float)

  private val defaultEffect = List(
    Parameter("ParamAngleX", 0.0f, 15.0f, 6.5345f, 0.5f),
    Parameter("ParamAngleY", 0.0f, 8.0f, 3.5345f, 0.5f),
    Parameter("ParamAngleZ", 0.0f, 10.0f, 5.5345f, 0.5f),
    Parameter("ParamBodyAngleX", 0.0f, 4.0f, 15.5345f, 0.5f),
    Parameter("ParamBreath", 0.5f, 0.5f, 3.2345f, 0.5f)
  )
}

class Breath (parameters: List[Breath.Parameter] = Breath.defaultEffect) extends Effect {

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {
    val perimeter = totalElapsedTimeInSeconds * 2.0f * Math.PI.toFloat
    parameters.map { parameter =>
      val value = parameter.offset + (parameter.peak * sin(perimeter / parameter.cycle).toFloat)
      ParameterValueAdd(parameter.parameterId, value, parameter.weight)
    }
  }

}
