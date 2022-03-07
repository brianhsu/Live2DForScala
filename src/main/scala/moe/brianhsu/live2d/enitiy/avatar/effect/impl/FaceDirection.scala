package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.effect.{AddOperation, FunctionalEffect, ParameterOperation, UpdateOperation}

class FaceDirection(directionCalculator: FaceDirectionCalculator) extends FunctionalEffect {

  def calculateOperations(currentTimeInSeconds: Float, deltaTimeInSeconds: Float): List[ParameterOperation] = {
    directionCalculator.updateFrameTimeInfo(currentTimeInSeconds, deltaTimeInSeconds)

    val (dragX, dragY) = directionCalculator.currentFaceCoordinate

    List(
      AddOperation("ParamAngleX", dragX * 30),
      AddOperation("ParamAngleY", dragY * 30),
      AddOperation("ParamAngleY", dragX * dragY * -30),
      AddOperation("ParamBodyAngleX", dragX * 10),
      UpdateOperation("ParamEyeBallX", dragX),
      UpdateOperation("ParamEyeBallY", dragY),
    )
  }
}
