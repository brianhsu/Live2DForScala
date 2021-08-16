package moe.brianhsu.live2d.enitiy.math

/**
 * This object provides easing function that could be used in animation.
 *
 * Concept about easing function could be found at https://easings.net/.
 *
 */
object Easing {

  /**
   * Calculate the easing sine value.
   *
   * For fade-in / fade-out animation usage.
   *
   *@param value  The value represent current steps.
   *@return       The easing sine of passed in value.
   */
  def sine(value: Float): Float = {

    value match {
      case x if x < 0.0f => 0.0f
      case x if x > 1.0f => 1.0f
      case x => 0.5f - 0.5f * Math.cos(x * Math.PI).toFloat
    }
  }
}
