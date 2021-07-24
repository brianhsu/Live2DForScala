package moe.brianhsu.live2d.boundary.gateway.avatar.settings.json.model

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting.{Curve, Meta, UserData}

/**
 * Represent the Motion object in JSON file.
 *
 * @param version Version of the setting format.
 * @param meta Metadata object in the JSON file.
 * @param userData List of user data in the JSON file.
 * @param curves List of curves in the JSON file.
 */
private[json] case class Motion(version: String, meta: Meta, userData: List[UserData], curves: List[Curve])
