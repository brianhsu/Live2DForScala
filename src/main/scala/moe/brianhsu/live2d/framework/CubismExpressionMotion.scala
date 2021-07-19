package moe.brianhsu.live2d.framework
import moe.brianhsu.live2d.framework.CubismExpressionMotion.{Add, Multiply, Overwrite}
import moe.brianhsu.live2d.framework.model.Live2DModel

object CubismExpressionMotion {
  sealed trait BlendType
  case object Add extends BlendType
  case object Multiply extends BlendType
  case object Overwrite extends BlendType

  case class Parameter(parameterId: String, blendType: BlendType, value: Float)
}

class CubismExpressionMotion(parameters: List[CubismExpressionMotion.Parameter]) extends ACubismMotion {

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
