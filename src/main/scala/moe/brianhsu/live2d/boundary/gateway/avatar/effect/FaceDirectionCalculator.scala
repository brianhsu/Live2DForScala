package moe.brianhsu.live2d.boundary.gateway.avatar.effect

trait FaceDirectionCalculator {
  def updateFrameTimeInfo(totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): Unit
  def currentFaceCoordinate: (Float, Float)
}
