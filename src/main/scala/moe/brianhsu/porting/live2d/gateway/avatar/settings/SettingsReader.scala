package moe.brianhsu.porting.live2d.gateway.avatar.settings

import moe.brianhsu.porting.live2d.enitiy.avatar.settings.Settings

import scala.util.Try

trait SettingsReader {
  def loadSettings(): Try[Settings]
}
