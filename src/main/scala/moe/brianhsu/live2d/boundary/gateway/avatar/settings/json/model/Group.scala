package moe.brianhsu.live2d.boundary.gateway.avatar.settings.json.model

/**
 * Represent the Group object in JSON file.
 *
 * The group that defines parameter used for eye blink or lip sync.
 *
 * @param target  What kind of data ids represents.
 * @param name    Name of this group, for example, `EyeBlink` or `LipSync`.
 * @param ids     The target id of this group.
 */
private[json] case class Group(target: String, name: String, ids: List[String])
