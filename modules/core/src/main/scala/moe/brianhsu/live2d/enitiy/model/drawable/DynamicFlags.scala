package moe.brianhsu.live2d.enitiy.model.drawable

import com.sun.jna.Pointer
import moe.brianhsu.live2d.enitiy.core.NativeCubismAPI.DynamicDrawableFlagMask._

/**
 * The class represent the dynamic flags of a [[Drawable]]
 *
 * @param pointer The pointer to actual of memory address that contains the value of this flag
 */
class DynamicFlags(private val pointer: Pointer) extends Flags {
  private def byteValue = pointer.getByte(0)

  /**
   * The actual bitmap that represent which flag has been enabled.
   *
   * @return The actual bitmap represent which flag has been enabled.
   */
  def bitmask: Byte = byteValue

  def isVisible: Boolean = isBitSet(byteValue, csmIsVisible)

  def visibilityChanged: Boolean = isBitSet(byteValue, csmVisibilityDidChange)

  def opacityChanged: Boolean = isBitSet(byteValue, csmOpacityDidChange)

  def drawOrderChanged: Boolean = isBitSet(byteValue, csmDrawOrderDidChange)

  def vertexPositionChanged: Boolean = isBitSet(byteValue, csmVertexPositionsDidChange)
}
