package moe.brianhsu.porting.live2d.enitiy.avatar.settings

import moe.brianhsu.porting.live2d.enitiy.avatar.settings.detail.{ExpressionSetting, HitAreaSetting, MotionSetting, PoseSetting}

case class Settings(
  mocFile: String,
  textureFiles: List[String],
  pose: Option[PoseSetting],
  eyeBlinkParameterIds: List[String],
  expressions: Map[String, ExpressionSetting],
  motionGroups: Map[String, List[MotionSetting]],
  hitArea: List[HitAreaSetting]
)
