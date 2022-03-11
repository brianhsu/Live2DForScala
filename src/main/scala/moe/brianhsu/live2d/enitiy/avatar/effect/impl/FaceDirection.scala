package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, Effect, ParameterValueAdd, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class FaceDirection(directionCalculator: FaceDirectionCalculator) extends Effect {

  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[EffectOperation] = {
    directionCalculator.updateFrameTimeInfo(totalElapsedTimeInSeconds, deltaTimeInSeconds)

    val (dragX, dragY) = directionCalculator.currentFaceCoordinate

    List(
      ParameterValueAdd("ParamAngleX", dragX * 30),
      ParameterValueAdd("ParamAngleY", dragY * 30),
      ParameterValueAdd("ParamAngleY", dragX * dragY * -30),
      ParameterValueAdd("ParamBodyAngleX", dragX * 10),
      ParameterValueUpdate("ParamEyeBallX", dragX),
      ParameterValueUpdate("ParamEyeBallY", dragY),
    )
  }
}
