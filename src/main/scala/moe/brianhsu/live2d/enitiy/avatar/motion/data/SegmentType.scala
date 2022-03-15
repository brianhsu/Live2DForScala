package moe.brianhsu.live2d.enitiy.avatar.motion.data

sealed trait SegmentType {
  def typeId: Int
  def pointCount: Int
}

object SegmentType {
  def apply(typeId: Int): SegmentType = {
    typeId match {
      case Linear.typeId => Linear
      case Bezier.typeId => Bezier
      case Stepped.typeId => Stepped
      case InverseStepped.typeId => InverseStepped
    }
  }
}

case object Linear extends SegmentType {
  override val typeId = 0
  override val pointCount = 1
}

case object Bezier extends SegmentType {
  override val typeId = 1
  override val pointCount = 3
}

case object Stepped extends SegmentType {
  override val typeId = 2
  override val pointCount = 1
}

case object InverseStepped extends SegmentType {
  override val typeId = 3
  override val pointCount = 1
}