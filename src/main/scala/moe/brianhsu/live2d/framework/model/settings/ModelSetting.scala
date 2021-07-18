package moe.brianhsu.live2d.framework.model.settings

case class ModelSetting(version: Int, fileReferences: FileReferences,
                        groups: List[Group], hitAreas: List[HitArea])
