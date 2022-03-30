package moe.brianhsu.live2d.enitiy.math

sealed trait Sign
case object Positive extends Sign
case object Negative extends Sign
case object Neutral extends Sign

object Sign {
  def apply(value: Float): Sign = {
    if (value == 0) {
      Neutral
    } else if (value < 0) {
      Negative
    } else {
      Positive
    }
  }
}
