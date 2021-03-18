package moe.brianhsu.live2d.framework.model.drawable

import moe.brianhsu.live2d.core.CubismCoreCLibrary.ConstantDrawableFlagMask
import moe.brianhsu.live2d.framework.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}


object ConstantFlags {
  sealed trait BlendMode
  case object Normal extends BlendMode
  case object AdditiveBlend extends BlendMode
  case object MultiplicativeBlend extends BlendMode
}

/**
 * This class represent the constant flag of a [[Drawable]].
 *
 * @param bitmask The actual bitmap that represent which flag has been enabled.
 */
case class ConstantFlags(bitmask: Byte) extends Flags {
  val isDoubleSided: Boolean = isBitSet(bitmask, ConstantDrawableFlagMask.csmIsDoubleSided)
  val isInvertedMask: Boolean = isBitSet(bitmask, ConstantDrawableFlagMask.csmIsInvertedMask)
  val blendMode: BlendMode = {
    if (isBitSet(bitmask, ConstantDrawableFlagMask.csmBlendAdditiveBit)) {
      AdditiveBlend
    } else if (isBitSet(bitmask, ConstantDrawableFlagMask.csmBlendMultiplicative)) {
      MultiplicativeBlend
    } else {
      Normal
    }
  }

}
