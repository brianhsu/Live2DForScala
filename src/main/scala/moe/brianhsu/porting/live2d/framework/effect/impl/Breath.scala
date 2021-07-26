package moe.brianhsu.porting.live2d.framework.effect.impl

import Breath.{Parameter, defaultEffect}
import moe.brianhsu.live2d.adapter.gateway.model.CubismLive2DModel
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.framework.effect.Effect

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

class Breath (parameters: List[Parameter] = defaultEffect) extends Effect {
  private var currentTimeInSeconds: Float = 0.0f

  override def updateParameters(model: Live2DModel, deltaTimeInSeconds: Float): Unit = {
    currentTimeInSeconds += deltaTimeInSeconds
    val perimeter = currentTimeInSeconds * 2.0f * Math.PI.toFloat
    parameters.foreach { parameter =>
      val value = parameter.offset + (parameter.peak * sin(perimeter / parameter.cycle).toFloat)
      model.parameters.get(parameter.parameterId).foreach(_.add(value, parameter.weight))
    }
  }
}
