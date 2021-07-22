package moe.brianhsu.live2d.enitiy.avatar.settings

import moe.brianhsu.live2d.framework.model.settings.{MotionCurve, MotionMeta, MotionUerData}

case class MotionSetting(version: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], meta: MotionMeta, userData: List[MotionUerData], curves: List[MotionCurve])
