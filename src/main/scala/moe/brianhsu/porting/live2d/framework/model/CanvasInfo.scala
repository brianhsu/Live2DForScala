package moe.brianhsu.porting.live2d.framework.model

/**
 * This class represent the canvas information about the Live 2D model
 *
 * @param widthInPixel  The width of this canvas in pixels.
 * @param heightInPixel The height of this canvas in pixels.
 * @param originInPixel The (X, Y) coordinate of the origin in pixels.
 * @param pixelPerUnit  How many pixels per unit.
 */
case class CanvasInfo(widthInPixel: Float, heightInPixel: Float, originInPixel: (Float, Float), pixelPerUnit: Float) {
  assert(pixelPerUnit > 0, "pixelPerUnit should > 0")

  /**
   * Get canvas width in unit
   * @return Width in unit
   */
  def width: Float = widthInPixel / pixelPerUnit

  /**
   * Get canvas height in unit
   * @return Height in unit
   */
  def height: Float = heightInPixel / pixelPerUnit
}
