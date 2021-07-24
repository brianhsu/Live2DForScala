package moe.brianhsu.live2d.gateway.avatar.settings

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import scala.util.Try

trait SettingsReader {
  def loadSettings(): Try[Settings]
}
