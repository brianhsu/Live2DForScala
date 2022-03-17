package moe.brianhsu.live2d.enitiy.avatar.motion.data

import moe.brianhsu.live2d.enitiy.avatar.motion.data.CurveTarget.Model

case class MotionCurve(id: String, targetType: CurveTarget = Model, fadeInTime: Option[Float], fadeOutTime: Option[Float], segments: List[MotionSegment])
