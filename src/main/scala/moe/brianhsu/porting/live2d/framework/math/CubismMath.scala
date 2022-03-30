package moe.brianhsu.porting.live2d.framework.math


object CubismMath {
  var Pi = 3.1415926535897932384626433832795f

  def radianToDirection(totalAngle: Float): CubismVector = {
    CubismVector(
      Math.sin(totalAngle).toFloat,
      Math.cos(totalAngle).toFloat
    )
  }

  def degreesToRadian(degrees: Float): Float = (degrees / 180.0f) * Pi

  def directionToRadian(from: CubismVector, to: CubismVector): Float = {
    val q1 = Math.atan2(to.y, to.x).toFloat
    val q2 = Math.atan2(from.y, from.x).toFloat
    var ret = q1 - q2

    while (ret < -Pi) {
      ret += Pi * 2.0f
    }

    while (ret > Pi) {
      ret -= Pi * 2.0f
    }

    ret
  }

}
