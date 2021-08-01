package moe.brianhsu.live2d.enitiy.math.matrix

class ModelMatrix(width: Float, height: Float, override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {

  type T = ModelMatrix

  def offsetX(x: Float): ModelMatrix = translateX(x)

  def offsetY(y: Float): ModelMatrix = translateY(y)


  def centerPosition(x: Float, y: Float): Unit = {
    centerX(x)
    centerY(y)
  }

  def left(x: Float): ModelMatrix = offsetX(x)

  def right(x: Float): ModelMatrix = {
    val w = width * xScalar
    translateX(x - w)
  }

  def top(y: Float): ModelMatrix = offsetY(y)

  def bottom(y: Float): ModelMatrix = translateY(y - height * yScalar)

  def centerY(y: Float): ModelMatrix = {
    val h = height * yScalar
    translateY(y - (h / 2.0f))
  }

  def centerX(x: Float): ModelMatrix = {
    val w = width * xScalar
    translateX(x - (w / 2.0f))
  }

  def width(width: Float): ModelMatrix = {
    val scaleX = width / this.width
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def height(height: Float): ModelMatrix = {
    val scaleX = height / this.height
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def position(x: Float, y: Float): ModelMatrix = translate(x, y)

  override protected def buildFrom(elements: Array[Float]): ModelMatrix = {
    new ModelMatrix(width, height, elements)
  }
}
