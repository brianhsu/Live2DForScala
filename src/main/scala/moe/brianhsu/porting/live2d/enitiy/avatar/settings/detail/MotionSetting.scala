package moe.brianhsu.porting.live2d.enitiy.avatar.settings.detail

import MotionSetting.{Curve, Meta, UserData}

object MotionSetting {
  case class UserData(time: Float, value: String)
  case class Curve(target: String, id: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], segments: List[Float])
  case class Meta(
    duration: Float, fps: Float, loop: Boolean, areBeziersRestricted: Boolean,
    curveCount: Int, totalSegmentCount: Int, totalPointCount: Int,
    userDataCount: Int, totalUserDataSize: Int
  )

}
case class MotionSetting(version: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], meta: Meta, userData: List[UserData], curves: List[Curve])
