package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.porting.live2d.framework.math.CubismMath

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
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      var t: Float = (time - points.head.time) / (points(1).time - points.head.time)

      if (t < 0.0f) {
        t = 0.0f
      }

      points.head.value + ((points(1).value - points.head.value) * t)
    }

    override def toString: String = "LinearEvaluate"
  }

  object BezierEvaluate extends SegmentEvaluation {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      var t: Float = (time - points.head.time) / (points(3).time - points.head.time)

      if (t < 0.0f) {
        t = 0.0f
      }

      val p01 = lerpPoints(points.head, points(1), t)
      val p12 = lerpPoints(points(1), points(2), t)
      val p23 = lerpPoints(points(2), points(3), t)

      val p012 = lerpPoints(p01, p12, t)
      val p123 = lerpPoints(p12, p23, t)

      lerpPoints(p012, p123, t).value
    }
  }

  object BezierEvaluateCardanoInterpretation extends SegmentEvaluation {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      val x: Float = time
      val x1: Float = points.head.time
      val x2: Float = points(3).time
      val cx1: Float = points(1).time
      val cx2: Float = points(2).time

      val a: Float = x2 - 3.0f * cx2 + 3.0f * cx1 - x1
      val b: Float = 3.0f * cx2 - 6.0f * cx1 + 3.0f * x1
      val c: Float = 3.0f * cx1 - 3.0f * x1
      val d: Float = x1 - x

      val t = CubismMath.CardanoAlgorithmForBezier(a, b, c, d)

      val p01 = lerpPoints(points.head, points(1), t)
      val p12 = lerpPoints(points(1), points(2), t)
      val p23 = lerpPoints(points(2), points(3), t)

      val p012 = lerpPoints(p01, p12, t)
      val p123 = lerpPoints(p12, p23, t)

      lerpPoints(p012, p123, t).value
    }
  }

  object SteppedEvaluate extends SegmentEvaluation {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      points.head.value
    }
  }

  object InverseSteppedEvaluate extends SegmentEvaluation {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[MotionPoint], time: Float): Float = {
      points(1).value
    }
  }

  private def lerpPoints(a: MotionPoint, b: MotionPoint, t: Float): MotionPoint = {
    val time = a.time + ((b.time - a.time) * t)
    val value = a.value + ((b.value - a.value) * t)
    MotionPoint(time, value)
  }

}
