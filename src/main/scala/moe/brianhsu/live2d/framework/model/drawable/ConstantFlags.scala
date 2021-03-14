package moe.brianhsu.live2d.framework.model.drawable

import moe.brianhsu.live2d.core.CubismCoreCLibrary.ConstantDrawableFlagMask
import moe.brianhsu.live2d.framework.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}


object ConstantFlags {
  sealed trait BlendMode
  case object Normal extends BlendMode
  case object AdditiveBlend extends BlendMode
  case object MultiplicativeBlend extends BlendMode
}

case class ConstantFlags(flagsValue: Byte) extends Flags {
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
