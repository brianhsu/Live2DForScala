package com.live2d.framework

import com.sun.jna.Pointer

/**
 * This class represent the part in the Live 2D model.
 *
 * @param opacityPointer    The pointer to the actual opacity value of this part
 * @param id                The part id
 * @param parentIdHolder    The id of this part's parent
 */
case class Part(private val opacityPointer: Pointer, id: String, parentIdHolder: Option[String]) {
  /**
   * Get the current opacity of this part.
   * @return  The current opacity of this part.
   */
  def opacity: Float = opacityPointer.getFloat(0)

  /**
   * Set the opacity of this part.
   *
   * @param value New opacity value.
   */
  def setOpacity(value: Float) = opacityPointer.setFloat(0, value)
}
