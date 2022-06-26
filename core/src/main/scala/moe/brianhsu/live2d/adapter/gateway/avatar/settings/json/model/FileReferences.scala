package moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.model

/**
 * Represent the FileReference object in JSON file.
 *
 * @param moc         The relative path of .moc model file.
 * @param textures    List of relative path of texture files.
 * @param physics     Optional physics file, in relative path.
 * @param pose        Optional pose file, in relative path.
 * @param expressions List of expression files, in relative path.
 * @param motions     Map of group name to list of motion files.
 * @param userData    Optional user data file, in relative path.
 */
private[json] case class FileReferences(
  moc: String,
  textures: List[String],
  physics: Option[String],
  pose: Option[String],
  expressions: List[ExpressionFile],
  motions: Map[String, List[MotionFile]],
  userData: Option[String]
)
