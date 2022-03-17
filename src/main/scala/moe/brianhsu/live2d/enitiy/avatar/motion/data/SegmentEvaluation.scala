package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.live2d.enitiy.math.CardanoAlgorithm

trait SegmentEvaluation {
  /**
   * モーションカーブのセグメントの評価関数
   *
   * モーションカーブのセグメントの評価関数。
   *
   * @param points      モーションカーブの制御点リスト
   * @param time        評価する時間[秒]
   */
  def apply(points: Array[MotionPoint], time: Float): Float
}

object SegmentEvaluation {

  object LinearEvaluate extends SegmentEvaluation {
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      val scalar = (time - points(0).time) / (points(1).time - points(0).time)
      val normalizedScalar = if (scalar < 0.0) 0 else scalar
      points(0).value + ((points(1).value - points(0).value) * normalizedScalar)
    }
  }

  object BezierEvaluate extends SegmentEvaluation {
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      val scalar: Float = (time - points(0).time) / (points(3).time - points(0).time)
      val normalizedScalar = if (scalar < 0.0) 0 else scalar

      val p01 = lerpPoints(points(0), points(1), normalizedScalar)
      val p12 = lerpPoints(points(1), points(2), normalizedScalar)
      val p23 = lerpPoints(points(2), points(3), normalizedScalar)

      val p012 = lerpPoints(p01, p12, normalizedScalar)
      val p123 = lerpPoints(p12, p23, normalizedScalar)

      lerpPoints(p012, p123, normalizedScalar).value
    }
  }

  object BezierEvaluateCardanoInterpretation extends SegmentEvaluation {
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      val x: Float = time
      val x1: Float = points(0).time
      val x2: Float = points(3).time
      val cx1: Float = points(1).time
      val cx2: Float = points(2).time

      val a: Float = x2 - 3.0f * cx2 + 3.0f * cx1 - x1
      val b: Float = 3.0f * cx2 - 6.0f * cx1 + 3.0f * x1
      val c: Float = 3.0f * cx1 - 3.0f * x1
      val d: Float = x1 - x

      val t = CardanoAlgorithm.forBezier(a, b, c, d)

      val p01 = lerpPoints(points(0), points(1), t)
      val p12 = lerpPoints(points(1), points(2), t)
      val p23 = lerpPoints(points(2), points(3), t)

      val p012 = lerpPoints(p01, p12, t)
      val p123 = lerpPoints(p12, p23, t)

      lerpPoints(p012, p123, t).value
    }
  }

  object SteppedEvaluate extends SegmentEvaluation {
    override def apply(points: Array[MotionPoint], time: Float): Float = points(0).value
  }

  object InverseSteppedEvaluate extends SegmentEvaluation {
    override def apply(points: Array[MotionPoint], time: Float): Float = points(1).value
  }

  private def lerpPoints(a: MotionPoint, b: MotionPoint, t: Float): MotionPoint = {
    val time = a.time + ((b.time - a.time) * t)
    val value = a.value + ((b.value - a.value) * t)
    MotionPoint(time, value)
  }

}
