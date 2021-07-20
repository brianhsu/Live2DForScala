package moe.brianhsu.live2d.framework.model.settings

case class MotionCurve(target: String, id: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], segments: List[Float])
