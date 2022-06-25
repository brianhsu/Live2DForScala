package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueAdd

class FaceDirection(directionCalculator: FaceDirectionCalculator) extends Effect {

  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {
    directionCalculator.updateFrameTimeInfo(totalElapsedTimeInSeconds, deltaTimeInSeconds)

    val (dragX, dragY) = directionCalculator.currentFaceCoordinate

    List(
      ParameterValueAdd("ParamAngleX", dragX * 30),
      ParameterValueAdd("ParamAngleY", dragY * 30),
      ParameterValueAdd("ParamAngleZ", dragX * dragY * -30),
      ParameterValueAdd("ParamBodyAngleX", dragX * 10),
      ParameterValueAdd("ParamEyeBallX", dragX),
      ParameterValueAdd("ParamEyeBallY", dragY),
    )
  }
}
