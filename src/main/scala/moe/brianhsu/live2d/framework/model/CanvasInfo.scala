package moe.brianhsu.live2d.framework.model

/**
 * This class represent the canvas about the Live 2D model
 *
 * @param widthInPixel  The width of this canvas in pixels.
 * @param heightInPixel The height of this canvas in pixels.
 * @param originInPixel The (X, Y) coordinate of the origin in pixels.
 * @param pixelPerUnit  How many pixels per unit.
 */
case class CanvasInfo(widthInPixel: Float, heightInPixel: Float, originInPixel: (Float, Float), pixelPerUnit: Float) {
  def width: Float = widthInPixel / pixelPerUnit

  def height: Float = heightInPixel / pixelPerUnit
}
