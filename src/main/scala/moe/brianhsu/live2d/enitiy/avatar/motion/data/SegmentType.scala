package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.porting.live2d.framework.CubismMotionSegment
import moe.brianhsu.porting.live2d.framework.CubismMotionSegment.CsmMotionSegmentEvaluationFunction

sealed trait SegmentType {
  def typeId: Int
  def pointCount: Int
  def evaluate: CsmMotionSegmentEvaluationFunction
}

object SegmentType {
  def apply(typeId: Int, areBeziersRestricted: Boolean): SegmentType = {
    typeId match {
      case Linear.typeId => Linear
      case Bezier.typeId if areBeziersRestricted => Bezier
      case BezierCardanoInterpretation.typeId if !areBeziersRestricted => BezierCardanoInterpretation
      case Stepped.typeId => Stepped
      case InverseStepped.typeId => InverseStepped
    }
  }

  case object Linear extends SegmentType {
    override val typeId = 0
    override val pointCount = 1
    override val evaluate = CubismMotionSegment.LinearEvaluate
  }

  case object Bezier extends SegmentType {
    override val typeId = 1
    override val pointCount = 3
    override val evaluate = CubismMotionSegment.BezierEvaluate
  }

  case object BezierCardanoInterpretation extends SegmentType {
    override val typeId = 1
    override val pointCount = 3
    override val evaluate = CubismMotionSegment.BezierEvaluateCardanoInterpretation
  }

  case object Stepped extends SegmentType {
    override val typeId = 2
    override val pointCount = 1
    override val evaluate = CubismMotionSegment.SteppedEvaluate
  }

  case object InverseStepped extends SegmentType {
    override val typeId = 3
    override val pointCount = 1
    override val evaluate = CubismMotionSegment.InverseSteppedEvaluate
  }
}


