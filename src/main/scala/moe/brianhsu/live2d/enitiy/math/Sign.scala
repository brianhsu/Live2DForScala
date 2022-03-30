package moe.brianhsu.live2d.enitiy.math

sealed trait Sign
case object Positive extends Sign
case object Negative extends Sign
case object Zero extends Sign

object Sign {
  def apply[T](value: Numeric[T]): Sign = {
    value.sign(value) match {
      case
    }
  }
}
