package moe.brianhsu.live2d.framework.effect

import moe.brianhsu.live2d.framework.model.Live2DModel

import scala.math.sin

class Breath(parameters: List[BreathParameter]) {
  private var currentTimeInSeconds: Float = 0.0f

  def updateParameters(model: Live2DModel, deltaTimeInSeconds: Float): Unit = {
    currentTimeInSeconds += deltaTimeInSeconds
    val t = currentTimeInSeconds * 2.0f * Math.PI.toFloat
    parameters.foreach { parameter =>
      val value = parameter.offset + (parameter.peak * sin(t / parameter.cycle).toFloat)
      model.addParameterValue(parameter.parameterId, value, parameter.weight)
    }
  }
}
