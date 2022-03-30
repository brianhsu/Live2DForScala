package moe.brianhsu.porting.live2d.framework.math

case class CubismVector2(var X: Float = 0, var Y: Float = 0) {

  def +(that: CubismVector2): CubismVector2 = {
    CubismVector2(this.X + that.X, this.Y + that.Y)
  }

  def -(that: CubismVector2): CubismVector2 = {
    CubismVector2(this.X - that.X, this.Y - that.Y)
  }

  def *(scalar: Float): CubismVector2 = {
    CubismVector2(this.X * scalar, this.Y * scalar)
  }

  def /(scalar: Float): CubismVector2 = {
    CubismVector2(this.X / scalar, this.Y / scalar)
  }

  def +=(rhs: CubismVector2): CubismVector2 = {
    X += rhs.X
    Y += rhs.Y
    this
  }
  def -=(rhs: CubismVector2): CubismVector2 = {
    X -= rhs.X
    Y -= rhs.Y
    this
  }

  def *=(rhs: CubismVector2): CubismVector2 = {
    X *= rhs.X
    Y *= rhs.Y
    this
  }

  def /=(rhs: CubismVector2): CubismVector2 = {
    X /= rhs.X
    Y /= rhs.Y
    this
  }

  def *=(scalar: Float): CubismVector2 = {
    X *= scalar
    Y *= scalar
    this
  }

  def /=(scalar: Float): CubismVector2 = {
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
