package moe.brianhsu.live2d.framework

import com.sun.jna.Pointer
import moe.brianhsu.live2d.framework.exception.ParameterInvalidException

/**
 * This class represent parameters of a Live 2D Cubism model.
 *
 * @param pointer   The pointer to the actual memory address of current value of this parameter.
 * @param id        The parameter id.
 * @param min       The minimum value of this parameter.
 * @param max       The maximum value of this parameter.
 * @param default   The default value of this parameter.
 */
case class Parameter(private val pointer: Pointer, id: String, min: Float, max:Float, default: Float) {

  /**
   * Get the current value of this parameter.
   *
   * @return  The current value of this parameter.
   */
  def current: Float = pointer.getFloat(0)

  /**
   * Update this parameter to a new value.
   *
   * @param   value The new value to assign.
   * @throws  ParameterInvalidException if the assigned value is invalid.
   */
  def update(value: Float): Unit = {

    if (value < min || value > max) {
      throw new ParameterInvalidException(id, value, min, max)
    }

    pointer.setFloat(0, value)
  }
}
