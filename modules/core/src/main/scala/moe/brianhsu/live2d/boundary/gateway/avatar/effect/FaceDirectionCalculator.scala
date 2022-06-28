package moe.brianhsu.live2d.boundary.gateway.avatar.effect

/**
 * Calculator interface for face direction
 */
trait FaceDirectionCalculator {
  /**
   * Update frame time information.
   *
   * Extended class could use this information to calculate the new face direction
   * vector.
   *
   * @param totalElapsedTimeInSeconds The total elapsed time since first frame.
   * @param deltaTimeInSeconds        The elapsed time since last frame.
   */
  def updateFrameTimeInfo(totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): Unit

  /**
   * Get the face direction coordinate.
   *
   * User could use this value to adjust the parameter of Live2D model.
   *
   * Both x / y will between -1.0 to 1.0
   *
   * @return The (x, y) coordinate of face direction.
   */
  def currentFaceCoordinate: (Float, Float)
}
