package moe.brianhsu.live2d.framework.model.drawable

import com.sun.jna.Pointer
import moe.brianhsu.live2d.core.CubismCoreCLibrary.DynamicDrawableFlagMask._

/**
 * The class represent the dynamic flags of a drawab.e
 *
 * @param pointer   The pointer to actual of memory address that contains the value of this flag
 */
case class DynamicFlags(private val pointer: Pointer) extends Flags {
  private def byteValue = pointer.getByte(0)
  def isVisible = isBitSet(byteValue, csmIsVisible)
  def visibilityChanged = isBitSet(byteValue, csmVisibilityDidChange)
  def opacityChanged = isBitSet(byteValue, csmOpacityDidChange)
  def drawOrderChanged = isBitSet(byteValue, csmDrawOrderDidChange)
  def vertexPositionChanged = isBitSet(byteValue, csmDrawOrderDidChange)
}
