package moe.brianhsu.live2d.math

class ModelMatrix(width: Float, height: Float) extends Matrix4x4 {
  setHeight(2.0f)

  def setCenterPosition(x: Float, y: Float): Unit = {
    setCenterX(x)
    setCenterY(y)
  }

  def setX(x: Float): Unit = {
    translateX(x)
  }

  def setY(y: Float): Unit = {
    translateY(y)
  }

  def setLeft(x: Float): Unit = {
    setX(x)
  }

  def setRight(x: Float): Unit = {
    val w = width * getScaleX()
    translateX(x - w)
  }

  def setTop(y: Float): Unit = {
    setY(y)
  }

  def setBottom(y: Float): Unit = {
    translateY(y - height * getScaleY())
  }

  def setCenterY(y: Float): Unit = {
    val h = height * getScaleY()
    translateY(y - (h / 2.0f))
  }

  def setCenterX(x: Float): Unit = {
    val w = width * getScaleX()
    translateX(x - (w / 2.0f))
  }

  def setWidth(w: Float): Unit = {
    val scaleX = w / width
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def setHeight(h: Float): Unit = {
    val scaleX = h / height
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def setPosition(x: Float, y: Float): Unit = {
    translate(x, y)
  }
}
