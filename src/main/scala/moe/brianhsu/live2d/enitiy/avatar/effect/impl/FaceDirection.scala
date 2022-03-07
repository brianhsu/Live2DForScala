package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.FaceDirection.DirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.effect.{AddOperation, FunctionalEffect, ParameterOperation, UpdateOperation}

object FaceDirection {
  trait DirectionCalculator {
    def updateFrameTimeInfo(totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): Unit
    def currentFaceCoordinate: (Float, Float)
  }
}

class FaceDirection(directionCalculator: DirectionCalculator) extends FunctionalEffect {

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
