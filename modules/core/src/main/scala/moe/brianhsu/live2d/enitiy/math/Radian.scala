package moe.brianhsu.live2d.enitiy.math

object Radian {
  private val PI = Math.PI.toFloat

  def radianToDirection(radian: Float): EuclideanVector = {
    EuclideanVector(
      Math.sin(radian).toFloat,
      Math.cos(radian).toFloat
    )
  }

  def degreesToRadian(degrees: Float): Float = (degrees / 180.0f) * PI

  def directionToRadian(from: EuclideanVector, to: EuclideanVector): Float = {
    val q1 = Math.atan2(to.y, to.x).toFloat
    val q2 = Math.atan2(from.y, from.x).toFloat
    var result = q1 - q2

    while (result < -PI) {
      result += PI * 2.0f
    }

    while (result > PI) {
      result -= PI * 2.0f
    }

    result
  }

}
