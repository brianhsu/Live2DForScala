package moe.brianhsu.porting.live2d.framework.math

case class CubismVector(var x: Float = 0, var y: Float = 0) {

  def +(that: CubismVector): CubismVector = {
    CubismVector(this.x + that.x, this.y + that.y)
  }

  def -(that: CubismVector): CubismVector = {
    CubismVector(this.x - that.x, this.y - that.y)
  }

  def *(scalar: Float): CubismVector = {
    CubismVector(this.x * scalar, this.y * scalar)
  }

  def /(scalar: Float): CubismVector = {
    CubismVector(this.x / scalar, this.y / scalar)
  }

  def +=(rhs: CubismVector): CubismVector = {
    x += rhs.x
    y += rhs.y
    this
  }
  def -=(rhs: CubismVector): CubismVector = {
    x -= rhs.x
    y -= rhs.y
    this
  }

  def *=(rhs: CubismVector): CubismVector = {
    x *= rhs.x
    y *= rhs.y
    this
  }

  def /=(rhs: CubismVector): CubismVector = {
    x /= rhs.x
    y /= rhs.y
    this
  }

  def *=(scalar: Float): CubismVector = {
    x *= scalar
    y *= scalar
    this
  }

  def /=(scalar: Float): CubismVector = {
    x /= scalar
    y /= scalar
    this
  }

  def normalize(): Unit = {
    val length = Math.pow((x * x) + (y * y), 0.5f).toFloat

    x = x / length
    y = y / length
  }
}
