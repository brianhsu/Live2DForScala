package moe.brianhsu.live2d.enitiy.avatar.settings.detail

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PoseSetting.Group

object PoseSetting {
  case class Group(id: String, link: List[String])
}

case class PoseSetting(`type`: String, fadeInTime: Option[Float], groups: List[List[Group]])
