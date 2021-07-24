package moe.brianhsu.live2d.gateway

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

import scala.util.Try

trait SettingsReader {
  def loadSettings(): Try[Settings]
}
