package moe.brianhsu.live2d.enitiy.avatar.settings

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.{ExpressionSetting, HitAreaSetting, MotionSetting, PoseSetting}

/**
 * Live2D Cubism Model Setting
 *
 * This class represent the settings of a Live2D Cubism Avatar.
 *
 * User should obtain instance of this class using a [[moe.brianhsu.live2d.boundary.gateway.avatar.settings.SettingsReader]],
 * instead of create the class directly.
 *
 * @param mocFile The absolute path of .moc file for this avatar.
 * @param textureFiles  List of the absolute path of the texture file.
 * @param pose  The poses of this avatar.
 * @param eyeBlinkParameterIds  List of parameter ids for eye blink effect.
 * @param expressions Map of expression name to expression setting for this avatar.
 * @param motionGroups Map of motion group name to list of motion setting for this avatar.
 * @param hitArea List of hit area definition for this avatar.
 */
case class Settings(
  mocFile: String,
  textureFiles: List[String],
  pose: Option[PoseSetting],
  eyeBlinkParameterIds: List[String],
  expressions: Map[String, ExpressionSetting],
  motionGroups: Map[String, List[MotionSetting]],
  hitArea: List[HitAreaSetting]
)
