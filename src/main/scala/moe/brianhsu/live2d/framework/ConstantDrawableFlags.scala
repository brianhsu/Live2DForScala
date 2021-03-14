package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.core.CubismCoreCLibrary
import CubismCoreCLibrary.ConstantDrawableFlagMask
import moe.brianhsu.live2d.framework.ConstantDrawableFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}

trait Flags {
  def isBitSet(value: Byte, mask: Byte): Boolean = (value & mask) == mask
}

object ConstantDrawableFlags {
  sealed trait BlendMode
  case object Normal extends BlendMode
  case object AdditiveBlend extends BlendMode
  case object MultiplicativeBlend extends BlendMode
}

case class ConstantDrawableFlags(flagsValue: Byte) extends Flags {
  val isDoubleSided: Boolean = isBitSet(flagsValue, ConstantDrawableFlagMask.csmIsDoubleSided)
  val isInvertedMask: Boolean = isBitSet(flagsValue, ConstantDrawableFlagMask.csmIsInvertedMask)
  val blendMode: BlendMode = {
    if (isBitSet(flagsValue, ConstantDrawableFlagMask.csmBlendAdditiveBit)) {
      AdditiveBlend
    } else if (isBitSet(flagsValue, ConstantDrawableFlagMask.csmBlendMultiplicative)) {
      MultiplicativeBlend
    } else {
      Normal
    }
  }

}
