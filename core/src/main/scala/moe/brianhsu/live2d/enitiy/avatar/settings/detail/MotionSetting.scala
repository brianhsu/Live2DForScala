package moe.brianhsu.live2d.enitiy.avatar.settings.detail

import MotionSetting.{Curve, Meta, UserData}

object MotionSetting {

  /**
   * The user defined event for a motion.
   *
   * @param time  When should this event fired, in seconds relative to the time when a motion started.
   * @param value The name of this event.
   */
  case class UserData(time: Float, value: String)

  /**
   * The curve defines a motion.
   * @param target Type of target.
   * @param id Id of target.
   * @param fadeInTime Optional fade in time, in seconds.
   * @param fadeOutTime Optional fade out time, in seconds.
   * @param segments The list of floats defines this curve.
   */
  case class Curve(target: String, id: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], segments: List[Float])

  /**
   * Motion metadata
   *
   * @param duration Duration of this motion, in seconds.
   * @param fps Frame rate of this motion.
   * @param loop Should loop this motion?
   * @param areBeziersRestricted  TODO: What is this?
   * @param curveCount How many curve this motion has.
   * @param totalSegmentCount How many segments this motion has.
   * @param totalPointCount How many points (floats in the segments) this motion has.
   * @param userDataCount How many user data this motion has.
   */
  case class Meta(
    duration: Float, fps: Float, loop: Boolean, areBeziersRestricted: Boolean,
    curveCount: Int, totalSegmentCount: Int, totalPointCount: Int,
    userDataCount: Int
  )
}

/**
 * Motion of a Live 2D Cubism Avatar.
 *
 * @param version The version of this motion setting.
 * @param fadeInTime  Optional fade in time, in seconds.
 * @param fadeOutTime Optional fade out time, in seconds.
 * @param meta The meta data of this motion.
 * @param userData List of user data.
 * @param curves List of curve that defines the motion.
 */
case class MotionSetting(version: String, fadeInTime: Option[Float], fadeOutTime: Option[Float], meta: Meta, userData: List[UserData], curves: List[Curve])
