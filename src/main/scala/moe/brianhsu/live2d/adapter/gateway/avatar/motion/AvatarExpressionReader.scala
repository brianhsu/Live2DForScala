package moe.brianhsu.live2d.adapter.gateway.avatar.motion

import moe.brianhsu.live2d.boundary.gateway.avatar.motion.ExpressionReader
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression.{Add, Multiply, Overwrite, Parameter}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.ExpressionSetting

/**
 * Reader to read expression from Live2D avatar settings.
 *
 * @param avatarSettings The settings from Live2D json model.
 */
class AvatarExpressionReader(avatarSettings: Settings) extends ExpressionReader {

  /**
   * Create expression from ExpressionSetting
   *
   * @param expressionSettings ExpressionSetting from settings extract from Live 2D json files.
   * @return                   The Expression motion could be used to update parameters in model.
   */
  private def createExpression(expressionSettings: ExpressionSetting): Expression = {
    val parameters = expressionSettings.parameters.map { p =>
      val blendType = p.blend match {
        case Some("Add") => Add
        case Some("Multiply") => Multiply
        case Some("Overwrite") => Overwrite
        case _ => Add
      }
      Parameter(p.id, blendType, p.value)
    }

    new Expression(
      expressionSettings.fadeInTime.getOrElse(1.0f),
      expressionSettings.fadeOutTime.getOrElse(1.0f),
      parameters
    )
  }

  override def loadExpressions: Map[String, Expression] = {
    avatarSettings.expressions
      .view
      .mapValues(createExpression)
      .toMap
  }
}
