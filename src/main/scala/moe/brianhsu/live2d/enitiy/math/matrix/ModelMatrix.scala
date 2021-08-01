package moe.brianhsu.live2d.enitiy.math.matrix

class ModelMatrix(val canvasWidth: Float, val canvasHeight: Float,
                  override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {

  type T = ModelMatrix

  private def offsetX(x: Float): ModelMatrix = translateX(x)

  private def offsetY(y: Float): ModelMatrix = translateY(y)

  def left(x: Float): ModelMatrix = offsetX(x)

  def right(x: Float): ModelMatrix = translateX(x - canvasWidth * xScalar)

  def top(y: Float): ModelMatrix = offsetY(y)

  def bottom(y: Float): ModelMatrix = translateY(y - canvasHeight * yScalar)

  def position(x: Float, y: Float): ModelMatrix = translate(x, y)

  def centerX(x: Float): ModelMatrix = {
    val width = canvasWidth * xScalar
    translateX(x - (width / 2.0f))
  }

  def centerY(y: Float): ModelMatrix = {
    val h = canvasHeight * yScalar
    translateY(y - (h / 2.0f))
  }

  def centerPosition(x: Float, y: Float): ModelMatrix = centerX(x).centerY(y)

  def scaleToWidth(width: Float): ModelMatrix = {
    val scaleX = width / this.canvasWidth
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def scaleToHeight(height: Float): ModelMatrix = {
    val scaleX = height / this.canvasHeight
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  override protected def buildFrom(elements: Array[Float]): ModelMatrix = {
    new ModelMatrix(canvasWidth, canvasHeight, elements)
  }
}
