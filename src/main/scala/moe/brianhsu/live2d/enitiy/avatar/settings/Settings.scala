package moe.brianhsu.live2d.enitiy.avatar.settings

case class Settings(
  mocFile: String,
  textureFiles: List[String],
  pose: Option[PoseSettings],
  eyeBlinkParameterIds: List[String],
  expressions: Map[String, ExpressionSettings],
  motionGroups: Map[String, List[MotionSetting]]
)
