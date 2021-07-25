package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.porting.live2d.framework.CubismMotionCurveTarget.CubismMotionCurveTarget_Model

case class CubismMotionCurve(
  var Id: String,
  var Type: CubismMotionCurveTarget.TargetType = CubismMotionCurveTarget_Model,
  var SegmentCount: Int = 0,
  var BaseSegmentIndex: Int = 0,
  var FadeInTime: Float = 0.0f,
  var FadeOutTime: Float = 0.0f
)
