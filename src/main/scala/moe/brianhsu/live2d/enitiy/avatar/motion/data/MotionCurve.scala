package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.Model

case class MotionCurve(id: String,
                       targetType: CurveTarget = Model,
                       segmentCount: Int = 0,
                       baseSegmentIndex: Int = 0,
                       fadeInTime: Option[Float],
                       fadeOutTime: Option[Float])
