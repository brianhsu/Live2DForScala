package moe.brianhsu.porting.live2d.framework.math.matrix

import moe.brianhsu.live2d.enitiy.math.matrix.Matrix4x4

class ModelMatrix(width: Float, height: Float, override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {

  type T = ModelMatrix

  def setCenterPosition(x: Float, y: Float): Unit = {
    setCenterX(x)
    setCenterY(y)
  }

  def setX(x: Float): ModelMatrix = translateX(x)

  def setY(y: Float): ModelMatrix = translateY(y)

  def setLeft(x: Float): ModelMatrix = setX(x)

  def setRight(x: Float): ModelMatrix = {
    val w = width * xScalar
    translateX(x - w)
  }

  def setTop(y: Float): ModelMatrix = setY(y)

  def setBottom(y: Float): ModelMatrix = translateY(y - height * yScalar)

  def setCenterY(y: Float): ModelMatrix = {
    val h = height * yScalar
    translateY(y - (h / 2.0f))
  }

  def setCenterX(x: Float): ModelMatrix = {
    val w = width * xScalar
    translateX(x - (w / 2.0f))
  }

  def setWidth(w: Float): ModelMatrix = {
    val scaleX = w / width
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def setHeight(h: Float): ModelMatrix = {
    val scaleX = h / height
    val scaleY = scaleX
    scale(scaleX, scaleY)
  }

  def setPosition(x: Float, y: Float): ModelMatrix = translate(x, y)

  override protected def buildFrom(x: Array[Float]): ModelMatrix = {
    new ModelMatrix(width, height, x)
  }
}
