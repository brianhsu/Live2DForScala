package moe.brianhsu.live2d.enitiy.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.motion.Expression.{Add, Multiply, Overwrite}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.ExpressionSetting
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object Expression {
  sealed trait BlendType
  case object Add extends BlendType
  case object Multiply extends BlendType
  case object Overwrite extends BlendType

  case class Parameter(parameterId: String, blendType: BlendType, value: Float)

  def createExpression(expressionSettings: ExpressionSetting): Expression = {
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

  def createExpressions(avatarSettings: Settings): Map[String, Expression] = {
    avatarSettings.expressions
      .view
      .mapValues(createExpression)
      .toMap
  }
}

class Expression(val fadeInTimeInSeconds: Float,
                 val fadeOutTimeInSeconds: Float,
                 parameters: List[Expression.Parameter]) extends Motion {

  override def calculateOperations(model: Live2DModel,
                                   currentTimeInSeconds: Float,
                                   deltaTimeInSeconds: Float,
                                   weight: Float): List[EffectOperation] = {

    parameters.map { parameter =>
      parameter.blendType match {
        case Add       => ParameterValueAdd(parameter.parameterId, parameter.value, weight)
        case Multiply  => ParameterValueMultiply(parameter.parameterId, parameter.value, weight)
        case Overwrite => ParameterValueUpdate(parameter.parameterId, parameter.value, weight)
      }
    }
  }
}
