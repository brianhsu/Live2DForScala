package moe.brianhsu.live2d.gateway.impl

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.framework.model.AvatarSettings
import moe.brianhsu.live2d.gateway.SettingsReader

class JsonSettingsReader(directory: String) extends SettingsReader {
  val t = new AvatarSettings(directory)

  override def loadSettings(): Settings = {
    Settings(
      t.mocFile.get,
      t.textureFiles,
      t.pose,
      t.eyeBlinkParameterIds,
      t.expressions,
      t.motionGroups
    )
  }
}
