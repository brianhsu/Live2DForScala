package moe.brianhsu.porting.live2d.gateway.avatar.settings.impl.json.model

case class FileReferences(moc: String, textures: List[String],
  physics: Option[String],
  pose: Option[String],
  expressions: List[ExpressionFile],
  motions: Map[String, List[MotionFile]],
  userData: Option[String])
