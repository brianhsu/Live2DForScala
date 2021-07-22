package moe.brianhsu.live2d.enitiy.avatar.settings

import moe.brianhsu.live2d.enitiy.avatar.settings.PoseSettings.Group

object PoseSettings {
  case class Group(id: String, link: List[String])
}

case class PoseSettings(`type`: String, fadeInTime: Option[Float], groups: List[List[Group]])
