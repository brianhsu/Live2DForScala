package moe.brianhsu.porting.live2d.enitiy.avatar.settings.detail

import ExpressionSetting.Parameters

object ExpressionSetting {
  case class Parameters(id: String, value: Float, blend: Option[String])
}

case class ExpressionSetting(
  `type`: String,
  fadeInTime: Option[Float],
  fadeOutTime: Option[Float],
  parameters: List[Parameters]
)
