package moe.brianhsu.live2d.framework.model.settings

case class MotionMeta(
  duration: Float, fps: Float, loop: Boolean, areBeziersRestricted: Boolean,
  curveCount: Int, totalSegmentCount: Int, totalPointCount: Int,
  userDataCount: Int, totalUserDataSize: Int
)
