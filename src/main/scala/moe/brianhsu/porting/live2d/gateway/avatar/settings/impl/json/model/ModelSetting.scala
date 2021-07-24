package moe.brianhsu.porting.live2d.gateway.avatar.settings.impl.json.model

import moe.brianhsu.porting.live2d.enitiy.avatar.settings.detail.HitAreaSetting

case class ModelSetting(version: Int, fileReferences: FileReferences,
  groups: List[Group], hitAreas: List[HitAreaSetting])
