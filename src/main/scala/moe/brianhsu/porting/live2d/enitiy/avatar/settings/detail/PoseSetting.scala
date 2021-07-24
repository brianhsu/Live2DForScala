package moe.brianhsu.porting.live2d.enitiy.avatar.settings.detail

import PoseSetting.Group

object PoseSetting {
  case class Group(id: String, link: List[String])
}

case class PoseSetting(`type`: String, fadeInTime: Option[Float], groups: List[List[Group]])
