package moe.brianhsu.live2d.enitiy.math.matrix

import moe.brianhsu.live2d.enitiy.math.matrix.Matrix4x4.{NumberOfColumns, NumberOfRows}

object Matrix4x4 {

  private val NumberOfRows = 4
  private val NumberOfColumns = 4

  /**
   * @return The identity matrix.
   * @todo Refine the package limitation once ViewMatrix is ported.
   */
  private[brianhsu] def createIdentity(): Array[Float] = Array[Float](
    1.0f, 0.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f, 0.0f,
    0.0f, 0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
  )
}

/**
 * The base matrix trait.
 *
 * This trait will handle the most basic matrix computation operation.
 */
trait Matrix4x4 {

  /**
   * The type of matrix, it should have the same type as subclass itselft.
   */
  type T <: Matrix4x4

  /**
   * The actual data of this matrix.
   *
   * It will represent by a array that contains 16 elements,
   * 4 consecutive elements represent each row in matrix.
   */
  val elements: Array[Float]

  /**
   * The build function to build a Matrix object.
   *
   * @param elements  The elements of the target matrix object.
   * @return The matrix object.
   */
  protected def buildFrom(elements: Array[Float]): T

  /**
   * The scalar of X-axis.
   *
   * @return  The scalar of X-axis.
   */
  def xScalar: Float = this.elements(0)

  /**
   * The scalar of Y-axis.
   *
   * @return  The scalar of Y-axis.
   */
  def yScalar: Float = this.elements(5)

  /**
   * The offset of X-axis.
   * @return The offset of X-axis.
   */
  def xOffset: Float = this.elements(12)

  /**
   * The offset of Y-axis.
   * @return The offset of Y-axis.
   */
  def yOffset: Float = this.elements(13)

  /**
   * Calculate the transformed X using this matrix.
   * @param source The source value of X.
   * @return The transformed value of X.
   */
  def transformedX(source: Float): Float = xScalar * source + xOffset

  /**
   * Calculate the transformed Y using this matrix
   * @param source The source value of X.
   * @return The transformed value of X.
   */
  def transformedY(source: Float): Float = yScalar * source + yOffset

  /**
   * Invert transformed X to original X using this matrix.
   * @param transformedX The transformed value of X.
   * @return The original value of X.
   */
  def invertedTransformedX(transformedX: Float): Float = (transformedX - xOffset) / xScalar

  /**
   * Invert transformed Y to original Y using this matrix.
   * @param transformedY The transformed value of Y.
   * @return The original value of Y.
   */
  def invertedTransformedY(transformedY: Float): Float = (transformedY - yOffset) / yScalar


  /**
   * Create new matrix by update the xScalar / yScalar
   * @param xScalar The new X scalar.
   * @param yScalar The new Y scalar.
   * @return The new instance of updated matrix.
   */
  def scale(xScalar: Float, yScalar: Float): T = {
    val newDataArray = copyDataArray(this.elements)
    newDataArray(0) = xScalar
    newDataArray(5) = yScalar
    buildFrom(newDataArray)
  }

  /**
   * Create new matrix by update the X offset.
   *
   * @param xOffset The new X offset.
   * @return The new instance of updated matrix.
   */
  def translateX(xOffset: Float): T = {
    val newDataArray = copyDataArray(this.elements)
    newDataArray(12) = xOffset
    buildFrom(newDataArray)
  }

  /**
   * Create new matrix by update the Y offset.
   *
   * @param yOffset The new Y offset.
   * @return The new instance of updated matrix.
   */
  def translateY(yOffset: Float): T = {
    val newDataArray = copyDataArray(this.elements)
    newDataArray(13) = yOffset
    buildFrom(newDataArray)
  }

  /**
   * Create new matrix by update the X / Y offset.
   *
   * @param xOffset The new X offset.
   * @param yOffset The new Y offset.
   * @return The new instance of updated matrix.
   */
  def translate(xOffset: Float, yOffset: Float): T = {
    val newArray = copyDataArray(this.elements)
    newArray(12) = xOffset
    newArray(13) = yOffset
    buildFrom(newArray)
  }

  /**
   * Create new matrix by update the X / Y offset relatively to current matrix.
   *
   * @param xOffset The relative X offset.
   * @param yOffset The relative Y offset.
   * @return The new instance of updated matrix.
   */
  def translateRelative(xOffset: Float, yOffset: Float): T = {
    val thatMatrix = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      xOffset, yOffset, 0.0f, 1.0f
    )

    buildFrom(multiply(thatMatrix, this.elements))
  }

  /**
   * Create new matrix by scale X / Y relatively to current matrix.
   *
   * @param xScalar The relative X scale rate.
   * @param yScalar The relative Y scale rate.
   * @return The new instance of updated matrix.
   */
  def scaleRelative(xScalar: Float, yScalar: Float): T = {
    val thatMatrix = Array(
      xScalar,      0.0f,   0.0f, 0.0f,
      0.0f,      yScalar,   0.0f, 0.0f,
      0.0f,         0.0f,   1.0f, 0.0f,
      0.0f,         0.0f,   0.0f, 1.0f
    )

    buildFrom(multiply(thatMatrix, this.elements))
  }

  /**
   * Create a new matrix by multiple this matrix with another matrix.
   *
   * @param that  The multiplier matrix.
   * @return A new matrix instance contains the multiplication result.
   */
  def *(that: Matrix4x4): that.T = {
    that.buildFrom(multiply(this.elements, that.elements))
  }

  /**
   * Calculate matrix multiplication
   * @param a The multiplicand matrix in array.
   * @param b The multiplier matrix in array.
   * @return The result of multiplication in array.
   */
  protected def multiply(a: Array[Float], b: Array[Float]): Array[Float] = {
    val result = new Array[Float](NumberOfRows * NumberOfColumns)
    val numberOfRows = 4

    for (i <- 0 until numberOfRows) {
      for (j <- 0 until numberOfRows) {
        for (k <- 0 until numberOfRows) {
          result(j + i * 4) += a(k + i * 4) * b(j + k * 4)
        }
      }
    }

    result
  }

  /**
   * Copy data from an array to a new array instance.
   * @param originalDataArray The original array.
   * @return A new array instance contains same elements.
   */
  private def copyDataArray(originalDataArray: Array[Float]): Array[Float] = {
    val newDataArray = new Array[Float](originalDataArray.length)
    originalDataArray.copyToArray(newDataArray)
    newDataArray
  }

}
