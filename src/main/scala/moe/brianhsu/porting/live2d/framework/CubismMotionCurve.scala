package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.porting.live2d.framework.CubismMotionCurveTarget.Model

case class CubismMotionCurve(id: String,
                             targetType: CubismMotionCurveTarget.TargetType = Model,
                             segmentCount: Int = 0,
                             baseSegmentIndex: Int = 0,
                             var fadeInTime: Float = 0.0f,
                             var fadeOutTime: Float = 0.0f
)
