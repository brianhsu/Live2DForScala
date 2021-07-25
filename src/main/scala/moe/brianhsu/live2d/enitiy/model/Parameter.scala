package moe.brianhsu.live2d.enitiy.model

import moe.brianhsu.porting.live2d.framework.exception.ParameterInvalidException

trait Parameter {
  /**
   * The parameter id.
   */
  val id: String

  /**
   * The minimum value of this parameter
   */
  val min: Float

  /**
   * The maximum value of this parameter.
   */
  val max: Float

  /**
   * The default value of this parameter.
   */
  val default: Float

  /**
   * Get the current value of this parameter.
   *
   * @return The current value of this parameter.
   */
  def current: Float

  /**
   * Update this parameter to a new value.
   *
   * @param value The new value to assign.
   * @throws ParameterInvalidException if the assigned value is invalid.
   */
  def update(value: Float): Unit
}
