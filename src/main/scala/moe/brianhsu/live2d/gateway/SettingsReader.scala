package moe.brianhsu.live2d.gateway

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings

trait SettingsReader {
  def loadSettings(): Settings
}
