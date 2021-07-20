package moe.brianhsu.live2d.framework.model.settings

case class Motion(version: String, meta: MotionMeta, userData: List[MotionUerData], curves: List[MotionCurve])
