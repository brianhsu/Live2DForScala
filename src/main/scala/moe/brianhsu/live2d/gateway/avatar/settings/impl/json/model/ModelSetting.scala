package moe.brianhsu.live2d.gateway.avatar.settings.impl.json.model

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.HitAreaSetting

case class ModelSetting(version: Int, fileReferences: FileReferences,
  groups: List[Group], hitAreas: List[HitAreaSetting])
