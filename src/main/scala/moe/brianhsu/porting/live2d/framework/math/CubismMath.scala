package moe.brianhsu.porting.live2d.framework.math


object CubismMath {
  var Pi = 3.1415926535897932384626433832795f

  def radianToDirection(totalAngle: Float): CubismVector = {
    val ret = CubismVector()
    ret.X = Math.sin(totalAngle).toFloat
    ret.Y = Math.cos(totalAngle).toFloat
    ret
  }

  def degreesToRadian(degrees: Float): Float = (degrees / 180.0f) * Pi

  def directionToRadian(from: CubismVector, to: CubismVector): Float = {
    val q1 = Math.atan2(to.Y, to.X).toFloat
    val q2 = Math.atan2(from.Y, from.X).toFloat
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
