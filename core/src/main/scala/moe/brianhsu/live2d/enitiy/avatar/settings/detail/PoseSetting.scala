package moe.brianhsu.live2d.enitiy.avatar.settings.detail

import PoseSetting.Part

object PoseSetting {
  case class Part(id: String, link: List[String])
}

/**
 * Poses for a Live 2D Cubism Avatar.
 *
 * Each pose is represented by an element in the `groups` member field,
 * contains a `List[Part]` defines that pose.
 *
 * @param fadeInTime Optional fade in time for poses, in seconds.
 * @param groups Poses of a Live 2D Cubism Avatar.
 */
case class PoseSetting(fadeInTime: Option[Float], groups: List[List[Part]])
