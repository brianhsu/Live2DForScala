package moe.brianhsu.live2d.enitiy.avatar.settings

import moe.brianhsu.live2d.enitiy.avatar.settings.ExpressionSettings.ExpressionParameter

object ExpressionSettings {
  case class ExpressionParameter(id: String, value: Float, blend: Option[String])
}

case class ExpressionSettings(
  `type`: String,
  fadeInTime: Option[Float],
  fadeOutTime: Option[Float],
  parameters: List[ExpressionParameter]
)
