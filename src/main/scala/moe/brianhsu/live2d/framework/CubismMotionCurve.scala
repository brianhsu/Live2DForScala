package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.framework.CubismMotionCurveTarget.CubismMotionCurveTarget_Model

case class CubismMotionCurve(
  Id: String,
  Type: CubismMotionCurveTarget.TargetType = CubismMotionCurveTarget_Model,
  SegmentCount: Int = 0,
  BaseSegmentIndex: Int = 0,
  var FadeInTime: Float = 0.0f,
  var FadeOutTime: Float = 0.0f
)
