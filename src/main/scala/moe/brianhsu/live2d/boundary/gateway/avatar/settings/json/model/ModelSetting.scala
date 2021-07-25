package moe.brianhsu.live2d.boundary.gateway.avatar.settings.json.model

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.HitAreaSetting

/**
 * Represent the root object in main setting JSON file.
 *
 * @param version         Version of this setting format.
 * @param fileReferences  External files.
 * @param groups          The group of parameters for eye blink and lip sync.
 * @param hitAreas        List of hit area.
 */
private[json] case class ModelSetting(
  version: Int, fileReferences: FileReferences,
  groups: List[Group], hitAreas: List[HitAreaSetting]
)
