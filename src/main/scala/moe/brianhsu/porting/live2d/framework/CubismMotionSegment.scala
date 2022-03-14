package moe.brianhsu.porting.live2d.framework

import CubismMotionSegment.CsmMotionSegmentEvaluationFunction
import moe.brianhsu.porting.live2d.framework.math.CubismMath

object CubismMotionSegment {
  trait CsmMotionSegmentEvaluationFunction {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points      モーションカーブの制御点リスト
     * @param time        評価する時間[秒]
     */
    def apply(points: Array[CubismMotionPoint], time: Float): Float
  }

  private def LerpPoints(a: CubismMotionPoint, b: CubismMotionPoint, t: Float): CubismMotionPoint = {
    val time = a.time + ((b.time - a.time) * t)
    val value = a.value + ((b.value - a.value) * t)
    CubismMotionPoint(time, value)
  }

  object LinearEvaluate extends CsmMotionSegmentEvaluationFunction {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[CubismMotionPoint], time: Float): Float = {
      var t: Float = (time - points.head.time) / (points(1).time - points.head.time)

      if (t < 0.0f) {
        t = 0.0f
      }

      points.head.value + ((points(1).value - points.head.value) * t)
    }

    override def toString: String = "LinearEvaluate"
  }

  object BezierEvaluate extends CsmMotionSegmentEvaluationFunction {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[CubismMotionPoint], time: Float): Float = {
      var t: Float = (time - points.head.time) / (points(3).time - points.head.time)

      if (t < 0.0f) {
        t = 0.0f
      }

      val p01 = LerpPoints(points.head, points(1), t)
      val p12 = LerpPoints(points(1), points(2), t)
      val p23 = LerpPoints(points(2), points(3), t)

      val p012 = LerpPoints(p01, p12, t)
      val p123 = LerpPoints(p12, p23, t)

      LerpPoints(p012, p123, t).value
    }
    override def toString: String = "BezierEvaluate"

  }

  object BezierEvaluateCardanoInterpretation extends CsmMotionSegmentEvaluationFunction {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[CubismMotionPoint], time: Float): Float = {
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

      val p01 = LerpPoints(points.head, points(1), t)
      val p12 = LerpPoints(points(1), points(2), t)
      val p23 = LerpPoints(points(2), points(3), t)

      val p012 = LerpPoints(p01, p12, t)
      val p123 = LerpPoints(p12, p23, t)

      LerpPoints(p012, p123, t).value
    }
    override def toString: String = "BezierEvaluateCardanoInterpretation"

  }

  object SteppedEvaluate extends CsmMotionSegmentEvaluationFunction {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[CubismMotionPoint], time: Float): Float = {
      points.head.value
    }

    override def toString: String = "SteppedEvaluate"

  }

  object InverseSteppedEvaluate extends CsmMotionSegmentEvaluationFunction {
    /**
     * モーションカーブのセグメントの評価関数
     *
     * モーションカーブのセグメントの評価関数。
     *
     * @param points モーションカーブの制御点リスト
     * @param time   評価する時間[秒]
     */
    override def apply(points: Array[CubismMotionPoint], time: Float): Float = {
      points(1).value
    }

    override def toString: String = "InverseSteppedEvaluate"

  }

}

case class CubismMotionSegment(
  var Evaluate: CsmMotionSegmentEvaluationFunction,
  var BasePointIndex: Int = 0,
  var SegmentType: Int = 0
)

