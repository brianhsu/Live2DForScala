package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.framework.CubismMotionSegment.CsmMotionSegmentEvaluationFunction

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
    def apply(points: List[CubismMotionPoint], time: Float): Float
  }
}

case class CubismMotionSegment(
  Evaluate: CsmMotionSegmentEvaluationFunction,
  BasePointIndex: Int = 0,
  SegmentType: Int = 0
)

