package moe.brianhsu.live2d.enitiy.math

case class EuclideanVector(x: Float = 0, y: Float = 0) {

  def +(that: EuclideanVector): EuclideanVector = {
    EuclideanVector(this.x + that.x, this.y + that.y)
  }

  def -(that: EuclideanVector): EuclideanVector = {
    EuclideanVector(this.x - that.x, this.y - that.y)
  }

  def *(scalar: Float): EuclideanVector = {
    EuclideanVector(this.x * scalar, this.y * scalar)
  }

  def /(scalar: Float): EuclideanVector = {
    EuclideanVector(this.x / scalar, this.y / scalar)
  }

  def normalize(): EuclideanVector = {
    val length = Math.pow((x * x) + (y * y), 0.5f).toFloat
    EuclideanVector(x / length, y / length)
  }
}
