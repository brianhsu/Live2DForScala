package moe.brianhsu.live2d.enitiy.math.matrix

import moe.brianhsu.porting.live2d.framework.math.Rectangle

/**
 * The 4x4 matrix to change the camera position.
 *
 * @param screenRectangle The logical coordinates corresponding to the display device.
 * @param maxRectangle    Movable range on logical coordinates.
 * @param minScale        The min scale rate.
 * @param maxScale        The max scale rate.
 * @param elements        The elements of the matrix.
 */
class ViewMatrix(screenRectangle: Rectangle, maxRectangle: Rectangle, minScale: Float, maxScale: Float, override val elements: Array[Float] = Matrix4x4.createIdentity()) extends Matrix4x4 {

  type T = ViewMatrix

  private def adjustAccordingToScreenLeft(x: Float): Float = {
    if (xScalar * maxRectangle.leftX + (xOffset + x) > screenRectangle.leftX) {
      this.screenRectangle.leftX - xScalar * maxRectangle.leftX - xOffset
    } else {
      x
    }
  }

  private def adjustAccordingToScreenRight(x: Float): Float = {
    if (xScalar * maxRectangle.rightX + (xOffset + x) < this.screenRectangle.rightX)  {
      screenRectangle.rightX - xScalar * maxRectangle.rightX - xOffset
    } else {
      x
    }
  }

  private def adjustAccordingToScreenTop(y: Float): Float = {
    if (yScalar * this.maxRectangle.topY + (yOffset + y) > this.screenRectangle.topY) {
      this.screenRectangle.topY - yScalar * this.maxRectangle.topY - yOffset
    } else {
      y
    }
  }

  private def adjustAccordingToScreenBottom(y: Float): Float = {
    if (yScalar * this.maxRectangle.bottomY + (yOffset + y) < this.screenRectangle.bottomY) {
      this.screenRectangle.bottomY - yScalar * this.maxRectangle.bottomY - yOffset
    } else {
      y
    }
  }

  /**
   * Adjust the offset
   *
   * @param x The offset of X-axis.
   * @param y The offset of Y-axis.
   * @return  The updated matrix contains new offset.
   */
  def adjustTranslate(x: Float, y: Float): ViewMatrix = {
    val xForTranslate = adjustAccordingToScreenRight(adjustAccordingToScreenLeft(x))
    val yForTranslate = adjustAccordingToScreenBottom(adjustAccordingToScreenTop(y))
    val tr1 = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      xForTranslate, yForTranslate, 0.0f, 1.0f
    )

    buildFrom(multiply(tr1, this.elements))
  }

  /**
   * Adjust the scale
   *
   * @param cx    The center of X-axis when scaling.
   * @param cy    The center of Y-axis when scaling.
   * @param scale The scale rate.
   * @return      The updated matrix.
   */
  def adjustScale(cx: Float, cy: Float, scale: Float): ViewMatrix = {

    val targetScale = scale * xScalar
    val adjustedScale: Float = if (targetScale < this.minScale) {
      adjustToNewScalar(this.minScale, scale)
    } else if (targetScale > this.maxScale) {
      adjustToNewScalar(this.maxScale, scale)
    } else {
      scale
    }

    val transform1 = Array[Float](
    1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      cx,   cy,   0.0f, 1.0f
    )
    val transform2 = Array[Float](
      adjustedScale, 0.0f,  0.0f, 0.0f,
      0.0f,  adjustedScale, 0.0f, 0.0f,
      0.0f,  0.0f,  1.0f, 0.0f,
      0.0f,  0.0f,  0.0f, 1.0f
    )
    val transform3 = Array[Float](
      1.0f, 0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f, 0.0f,
      0.0f, 0.0f, 1.0f, 0.0f,
      -cx,  -cy,  0.0f, 1.0f
    )

    val c1 = multiply(transform3, this.elements)
    val c2 = multiply(transform2, c1)
    val c3 = multiply(transform1, c2)

    buildFrom(c3)
  }


  private def adjustToNewScalar(newScalar: Float, defaultScalar: Float): Float = {
    if (xScalar > 0.0f) {
      newScalar / xScalar
    } else {
      defaultScalar
    }
  }

  override protected def buildFrom(elements: Array[Float]): ViewMatrix = {
    new ViewMatrix(screenRectangle, maxRectangle, minScale, maxScale, elements)
  }
}
