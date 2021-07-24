package moe.brianhsu.live2d.enitiy.avatar.settings.detail

import ExpressionSetting.Parameters

object ExpressionSetting {
  /**
   * The parameter of a expression.
   * @param id  The parameter id.
   * @param value The target value of this parameter.
   * @param blend The blend type, possible value are `Add`, `Multiply`, `Overwrite`.
   */
  case class Parameters(id: String, value: Float, blend: Option[String])
}

/**
 * Expression of a Live 2D Cubism Avatar.
 *
 * @param fadeInTime  Optional fade in time, in seconds.
 * @param fadeOutTime Optional fade out time, in seconds.
 * @param parameters  List of parameter target values.
 */
case class ExpressionSetting(
  fadeInTime: Option[Float],
  fadeOutTime: Option[Float],
  parameters: List[Parameters]
)
