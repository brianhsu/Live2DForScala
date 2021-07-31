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

trait Matrix4x4 {

  type T <: Matrix4x4

  val elements: Array[Float]

  protected def buildFrom(x: Array[Float]): T

  def transformedX(src: Float): Float = this.elements(0) * src + this.elements(12)
  def transformedY(src: Float): Float = this.elements(5) * src + this.elements(13)
  def invertedTransformedX(src: Float): Float = (src - this.elements(12)) / this.elements(0)
  def invertedTransformedY(src: Float): Float = (src - this.elements(13)) / this.elements(5)

  def xScalar: Float = this.elements(0)
  def yScalar: Float = this.elements(5)
  def xTranslator: Float = this.elements(12)
  def yTranslator: Float = this.elements(13)

  def scale(x: Float, y: Float): T = {
    val newDataArray = copyDataArray(this.elements)
    newDataArray(0) = x
    newDataArray(5) = y
    buildFrom(newDataArray)
  }

  def translateX(x: Float): T = {
    val newDataArray = copyDataArray(this.elements)
    newDataArray(12) = x
    buildFrom(newDataArray)
  }

  def translateY(y: Float): T = {
    val newDataArray = copyDataArray(this.elements)
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

    multiply(tr1, this.elements)
  }

  def translate(x: Float, y: Float): T = {
    val newArray = copyDataArray(this.elements)
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

    multiply(tr1, this.elements)
  }

  def multiply(matrix: Matrix4x4): T = {
    multiply(matrix.elements, this.elements)
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
