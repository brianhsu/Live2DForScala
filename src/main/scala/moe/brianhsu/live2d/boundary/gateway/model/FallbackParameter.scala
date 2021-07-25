package moe.brianhsu.live2d.boundary.gateway.model

import moe.brianhsu.live2d.enitiy.model.Parameter

/**
 *
 * @param id The parameter id.
 */
class FallbackParameter(override val id: String) extends Parameter {

  private var value: Float = 0

  /**
   * The minimum value of this parameter
   */
  override val min: Float = Float.MinValue

  /**
   * The maximum value of this parameter.
   */
  override val max: Float = Float.MaxValue

  /**
   * The default value of this parameter.
   */
  override val default: Float = 0

  /**
   * Get the current value of this parameter.
   *
   * @return The current value of this parameter.
   */
  override def current: Float = value

  /**
   * Update this parameter to a new value.
   *
   * @param value The new value to assign.
   * @throws ParameterInvalidException if the assigned value is invalid.
   */
  override def update(value: Float): Unit = {
    this.value = value
  }
}
