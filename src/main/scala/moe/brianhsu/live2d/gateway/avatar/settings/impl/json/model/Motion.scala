package moe.brianhsu.live2d.gateway.avatar.settings.impl.json.model

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting.{Curve, Meta, UserData}

case class Motion(version: String, meta: Meta, userData: List[UserData], curves: List[Curve])
