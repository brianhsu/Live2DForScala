package moe.brianhsu.live2d.enitiy.math

/**
 * Represent a rectangle
 *
 * Please note that the origin of Y axis is at the bottom.
 *
 * For example, an 1024x768 rectangle will be represent as following:
 *
 * {{{
 *   0 ----------------------------> 1024
 *   +-----------------------------+ 768
 *   | (0,768)          (1024,768) |  ^
 *   |                             |  |
 *   |                             |  |
 *   | (0,0)              (1024,0) |  |
 *   +-----------------------------+  0
 * }}}
 *
 * @param leftX    The left side of X-axis of this rectangle.
 * @param bottomY  The bottom side of Y-axis of this rectangle.
 * @param width    The width of this rectangle.
 * @param height   The height of this rectangle.
 */
case class Rectangle(leftX: Float = 0.0f, bottomY: Float = 0.0f, width: Float = 0.0f, height: Float = 0.0f) {

  /**
   * The right side of X-axis of this rectangle
   */
  val rightX: Float = leftX + width

  /**
   * The top side of Y-axis of this rectangle
   */
  val topY: Float = bottomY + height

  /**
   * Expend the rectangle.
   *
   * Expand the rectangle using the center of original rectangle as
   * pivot.
   *
   * @param width  The expended width.
   * @param height The expended height.
   * @return The new rectangle after expended.
   */
  def expand(width: Float, height: Float): Rectangle = {
    Rectangle(
      leftX - width,
      bottomY - height,
      this.width + width * 2.0f,
      this.height + height * 2.0f
    )
  }
}
