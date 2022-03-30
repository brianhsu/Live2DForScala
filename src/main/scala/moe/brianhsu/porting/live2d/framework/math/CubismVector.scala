package moe.brianhsu.porting.live2d.framework.math

case class CubismVector(x: Float = 0, y: Float = 0) {

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

  def normalize(): CubismVector = {
    val length = Math.pow((x * x) + (y * y), 0.5f).toFloat
    CubismVector(x / length, y / length)
  }
}
