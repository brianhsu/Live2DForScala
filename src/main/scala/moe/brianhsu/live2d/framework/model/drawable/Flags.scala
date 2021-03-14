package moe.brianhsu.live2d.framework.model.drawable

trait Flags {
  def isBitSet(value: Byte, mask: Byte): Boolean = (value & mask) == mask
}
