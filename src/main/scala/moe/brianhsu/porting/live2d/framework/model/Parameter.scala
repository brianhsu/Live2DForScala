package moe.brianhsu.porting.live2d.framework.model

import com.sun.jna.Pointer
import moe.brianhsu.porting.live2d.framework.exception.ParameterInvalidException

trait IParameter {
  def id: String
  def min: Float
  def max: Float
  def default: Float
  def current: Float
  def update(value: Float): Unit
}

/**
 * This class represent parameters of a Live 2D Cubism model.
 *
 * @param pointer     The pointer to the actual memory address of current value of this parameter.
 * @param id          The parameter id.
 * @param min         The minimum value of this parameter.
 * @param max         The maximum value of this parameter.
 * @param default     The default value of this parameter.
 */
case class Parameter(
  private val pointer: Pointer,
  override val id: String,
  override val min: Float,
  override val max: Float,
  override val default: Float) extends IParameter {

  /**
   * Get the current value of this parameter.
   *
   * @return The current value of this parameter.
   */
  override def current: Float = pointer.getFloat(0)

  /**
   * Update this parameter to a new value.
   *
   * @param value The new value to assign.
   * @throws ParameterInvalidException if the assigned value is invalid.
   */
  override def update(value: Float): Unit = {

    if (value < min || value > max) {
      throw new ParameterInvalidException(id, value, min, max)
    }

    pointer.setFloat(0, value)
  }
}
