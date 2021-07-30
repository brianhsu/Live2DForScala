package moe.brianhsu.live2d.enitiy.model.drawable

trait Flags {
  /**
   * See if a certain bit inside a bitmap have been enabled.
   *
   * @param bitmap The bitmap that represent flags.
   * @param mask   The bitmap that only the desired flags have been enabled.
   * @return True if the bit is enabled, false otherwise.
   */
  protected def isBitSet(bitmap: Byte, mask: Byte): Boolean = (bitmap & mask) == mask
}
