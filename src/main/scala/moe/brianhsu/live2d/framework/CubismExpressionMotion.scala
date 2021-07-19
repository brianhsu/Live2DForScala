package moe.brianhsu.live2d.framework
import moe.brianhsu.live2d.framework.CubismExpressionMotion.{Add, Multiply, Overwrite}
import moe.brianhsu.live2d.framework.model.settings.Expression
import moe.brianhsu.live2d.framework.model.{AvatarSettings, Live2DModel}

object CubismExpressionMotion {
  sealed trait BlendType
  case object Add extends BlendType
  case object Multiply extends BlendType
  case object Overwrite extends BlendType

  case class Parameter(parameterId: String, blendType: BlendType, value: Float)

  def createExpression(expressionSettings: Expression): CubismExpressionMotion = {
    val parameters = expressionSettings.parameters.map { p =>
      val blendType = p.blend match {
        case Some("Add") => Add
        case Some("Multiply") => Multiply
        case Some("Overwrite") => Overwrite
        case _ => Add
      }
      Parameter(p.id, blendType, p.value)
    }

    val expression = new CubismExpressionMotion(parameters)
    expression.SetFadeInTime(expressionSettings.fadeInTime.getOrElse(1.0f))
    expression.SetFadeOutTime(expressionSettings.fadeOutTime.getOrElse(1.0f))
    expression
  }

  def createExpressions(avatarSettings: AvatarSettings): Map[String, CubismExpressionMotion] = {
    avatarSettings.expressions
      .view
      .mapValues(createExpression)
      .toMap
  }
}

class CubismExpressionMotion private (parameters: List[CubismExpressionMotion.Parameter]) extends ACubismMotion {

  override protected def DoUpdateParameters(model: Live2DModel, userTimeSeconds: Float, weight: Float,
                                            motionQueueEntry: CubismMotionQueueEntry): Unit = {

    parameters.foreach { parameter =>
      parameter.blendType match {
        case Add       => model.addParameterValue(parameter.parameterId, parameter.value, weight)
        case Multiply  => model.multiplyParameterValue(parameter.parameterId, parameter.value, weight)
        case Overwrite => model.setParameterValue(parameter.parameterId, parameter.value, weight)
      }
    }
  }
}
