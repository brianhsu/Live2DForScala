package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.porting.live2d.framework.CubismMotionCurveTarget.Model

case class CubismMotionCurve(
                              var id: String,
                              var targetType: CubismMotionCurveTarget.TargetType = Model,
                              var segmentCount: Int = 0,
                              var baseSegmentIndex: Int = 0,
                              var fadeInTime: Float = 0.0f,
                              var fadeOutTime: Float = 0.0f
)
