package moe.brianhsu.live2d.enitiy.math.matrix

/**
 * The matrix for set model coordinates.
 *
 * @param canvasWidth   The width of canvas.
 * @param canvasHeight  The height of canvas.
 * @param elements      The elements of this matrix.
 */
class ModelMatrix(val canvasWidth: Float, val canvasHeight: Float,
                  override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {

  type T = ModelMatrix

  /**
   * Create an new matrix with new left boundary
   *
   * @param x The value of x-axis of new left boundary.
   * @return  The new matrix by adjust X axis.
   */
  def left(x: Float): ModelMatrix = translateX(x)

  /**
   * Create an new matrix with new right boundary
   *
   * @param x The value of x-axis of new right boundary.
   * @return  The new matrix by adjust X axis.
   */
  def right(x: Float): ModelMatrix = translateX(x - canvasWidth * xScalar)

  /**
   * Create an new matrix with new top boundary
   *
   * @param y The value of y-axis of new top boundary.
   * @return  The new matrix by adjust Y axis.
   */
  def top(y: Float): ModelMatrix = translateY(y)

  /**
   * Create an new matrix with new bottom boundary
   *
   * @param y The value of y-axis of new bottom boundary.
   * @return  The new matrix by adjust Y axis.
   */
  def bottom(y: Float): ModelMatrix = translateY(y - canvasHeight * yScalar)

  /**
   * Create a new matrix by set X / Y axis offset.
   * @param x The offset in X-axis.
   * @param y The offset in Y-axis.
   * @return  The new created matrix.
   */
  def position(x: Float, y: Float): ModelMatrix = translate(x, y)

  /**
   * Create a new matrix by put the model X center to specific position.
   *
   * @param x The center X axis value of model.
   * @return  The new created matrix.
   */
  def centerX(x: Float): ModelMatrix = {
    val width = canvasWidth * xScalar
    translateX(x - (width / 2.0f))
  }

  /**
   * Create a new matrix by put the model Y center to specific position.
   *
   * @param y The center Y axis value of model.
   * @return  The new created matrix.
   */
  def centerY(y: Float): ModelMatrix = {
    val h = canvasHeight * yScalar
    translateY(y - (h / 2.0f))
  }

  /**
   * Create a new matrix by put the model X / Y center to specific position.
   *
   * @param x The center X axis value of model.
   * @param y The center Y axis value of model.
   * @return  The new created matrix.
   */
  def centerPosition(x: Float, y: Float): ModelMatrix = centerX(x).centerY(y)

  /**
   * Create an new matrix by scale the width.
   *
   * @param width The zoom factor of model width.
   * @return      The new created matrix.
   */
  def scaleToWidth(width: Float): ModelMatrix = {
    val scaleX = width / this.canvasWidth
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  /**
   * Create an new matrix by scale the height.
   *
   * @param height  The zoom factor of model width.
   * @return        The new created matrix.
   */
  def scaleToHeight(height: Float): ModelMatrix = {
    val scaleX = height / this.canvasHeight
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  override protected def buildFrom(elements: Array[Float]): ModelMatrix = {
    new ModelMatrix(canvasWidth, canvasHeight, elements)
  }
}
