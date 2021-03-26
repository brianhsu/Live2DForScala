package moe.brianhsu.live2d.framework.math


object Matrix4x4 {
  val NumberOfElements = 4 * 4

  def multiply(a: Array[Float], b: Array[Float], dst: Array[Float]): Unit = {
    val c = Array[Float](
      0.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 0.0f, 0.0f
    )

    val n = 4

    for (i <- 0 until n) {
      for (j <- 0 until n) {
        for (k <- 0 until n) {
          c(j + i * 4) += a(k + i * 4) * b(j + k * 4)
        }
      }
    }

    for (i <- 0 until NumberOfElements) {
      dst(i) = c(i)
    }

  }
}
class Matrix4x4 {

  import Matrix4x4.NumberOfElements
  import Matrix4x4.multiply

  protected val tr = createIdentity()

  def createIdentity(): Array[Float] = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      0.0f, 0.0f, 0.0f, 1.0f
  )

  def loadIdentity(): Unit = setMatrix(createIdentity())

  def getArray(): Array[Float] = tr

  def translateRelative(x: Float, y: Float): Unit = {
    val tr1 = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      x,    y,    0.0f, 1.0f
    )

    multiply(tr1, this.tr, this.tr)
  }

  def translate(x: Float, y: Float): Unit = {
    this.tr(12) = x
    this.tr(13) = y
  }

  def scaleRelative(x: Float, y: Float): Unit = {
    val tr1 = Array(
      x,      0.0f,   0.0f, 0.0f,
      0.0f,   y,      0.0f, 0.0f,
      0.0f,   0.0f,   1.0f, 0.0f,
      0.0f,   0.0f,   0.0f, 1.0f
    )

    multiply(tr1, this.tr, this.tr)
  }

  def scale(x: Float, y: Float): Unit = {
    this.tr(0) = x
    this.tr(5) = y
  }

  def transformX(src: Float): Float = this.tr(0) * src + this.tr(12)

  def invertTransformX(src: Float): Float = (src - this.tr(12)) / this.tr(0)

  def transformY(src: Float): Float = this.tr(5) * src + this.tr(13)

  def invertTransformY(src: Float): Float = (src - this.tr(13)) / this.tr(5)

  def setMatrix(tr: Array[Float]): Unit = {
    for (i <- 0 until NumberOfElements) {
      this.tr(i) = tr(i)
    }
  }

  def getScaleX(): Float = this.tr(0)
  def getScaleY(): Float = this.tr(5)
  def getTranslateX: Float = this.tr(12)
  def getTranslateY: Float = this.tr(13)
  def translateX(x: Float): Unit = {
    this.tr(12) = x
  }

  def translateY(y: Float): Unit = {
    this.tr(13) = y
  }

  def multiplyByMatrix(matrix4x4: Matrix4x4): Unit = {
    multiply(matrix4x4.getArray(), this.tr, this.tr)
  }
}
