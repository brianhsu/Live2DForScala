package moe.brianhsu.porting.live2d.framework.math

case class CubismVector(var X: Float = 0, var Y: Float = 0) {

  def +(that: CubismVector): CubismVector = {
    CubismVector(this.X + that.X, this.Y + that.Y)
  }

  def -(that: CubismVector): CubismVector = {
    CubismVector(this.X - that.X, this.Y - that.Y)
  }

  def *(scalar: Float): CubismVector = {
    CubismVector(this.X * scalar, this.Y * scalar)
  }

  def /(scalar: Float): CubismVector = {
    CubismVector(this.X / scalar, this.Y / scalar)
  }

  def +=(rhs: CubismVector): CubismVector = {
    X += rhs.X
    Y += rhs.Y
    this
  }
  def -=(rhs: CubismVector): CubismVector = {
    X -= rhs.X
    Y -= rhs.Y
    this
  }

  def *=(rhs: CubismVector): CubismVector = {
    X *= rhs.X
    Y *= rhs.Y
    this
  }

  def /=(rhs: CubismVector): CubismVector = {
    X /= rhs.X
    Y /= rhs.Y
    this
  }

  def *=(scalar: Float): CubismVector = {
    X *= scalar
    Y *= scalar
    this
  }

  def /=(scalar: Float): CubismVector = {
    X /= scalar
    Y /= scalar
    this
  }

  def normalize(): Unit = {
    val length = Math.pow((X * X) + (Y * Y), 0.5f).toFloat

    X = X / length
    Y = Y / length
  }
}
