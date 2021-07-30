package moe.brianhsu.live2d.enitiy.math.matrix

object Matrix4x4 {
  val NumberOfElements: Int = 4 * 4
  def createIdentity(): Array[Float] = Array[Float](
    1.0f, 0.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f, 0.0f,
    0.0f, 0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
  )
}

trait Matrix4x4[T] {

  val dataArray: Array[Float]

  protected def buildFrom(x: Array[Float]): T

  def transformX(src: Float): Float = this.dataArray(0) * src + this.dataArray(12)
  def transformY(src: Float): Float = this.dataArray(5) * src + this.dataArray(13)
  def invertTransformX(src: Float): Float = (src - this.dataArray(12)) / this.dataArray(0)
  def invertTransformY(src: Float): Float = (src - this.dataArray(13)) / this.dataArray(5)
  def scaleX: Float = this.dataArray(0)
  def scaleY: Float = this.dataArray(5)
  def translateX: Float = this.dataArray(12)
  def translateY: Float = this.dataArray(13)

  def scale(x: Float, y: Float): T = {
    val newDataArray = copyDataArray(this.dataArray)
    newDataArray(0) = x
    newDataArray(5) = y
    buildFrom(newDataArray)
  }

  def setMatrix(tr: Array[Float]): T = {
    val newDataArray = copyDataArray(tr)
    buildFrom(newDataArray)
  }

  def translateX(x: Float): T = {
    val newDataArray = copyDataArray(this.dataArray)
    newDataArray(12) = x
    buildFrom(newDataArray)
  }

  def translateY(y: Float): T = {
    val newDataArray = copyDataArray(this.dataArray)
    newDataArray(13) = y
    buildFrom(newDataArray)
  }

  def translateRelative(x: Float, y: Float): T = {
    val tr1 = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      x,    y,    0.0f, 1.0f
    )

    multiply(tr1, this.dataArray)
  }

  def translate(x: Float, y: Float): T = {
    val newArray = copyDataArray(this.dataArray)
    newArray(12) = x
    newArray(13) = y
    buildFrom(newArray)
  }

  def scaleRelative(x: Float, y: Float): T = {
    val tr1 = Array(
      x,      0.0f,   0.0f, 0.0f,
      0.0f,   y,      0.0f, 0.0f,
      0.0f,   0.0f,   1.0f, 0.0f,
      0.0f,   0.0f,   0.0f, 1.0f
    )

    multiply(tr1, this.dataArray)
  }

  def multiply(matrix: Matrix4x4[_]): T = {
    multiply(matrix.dataArray, this.dataArray)
  }


  private def multiply(a: Array[Float], b: Array[Float]): T = {
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

    buildFrom(c)
  }

  private def copyDataArray(originalDataArray: Array[Float]): Array[Float] = {
    val newDataArray = new Array[Float](originalDataArray.length)
    originalDataArray.copyToArray(newDataArray)
    newDataArray
  }

}
