package moe.brianhsu.live2d.usecase.updater

import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}

trait UpdateStrategy {
  def update(frameTimeInfo: FrameTimeInfo): Unit

  protected def executeOperations(model: Live2DModel, operations: List[UpdateOperation]): Unit = {
    operations.foreach {
      case ParameterValueAdd(parameterId, value, weight) => model.parameters.get(parameterId).foreach(_.add(value, weight))
      case ParameterValueUpdate(parameterId, value, weight) => model.parameters.get(parameterId).foreach(_.update(value, weight))
      case ParameterValueMultiply(parameterId, value, weight) => model.parameters.get(parameterId).foreach(_.multiply(value, weight))
      case FallbackParameterValueAdd(parameterId, value, weight) => model.parameterWithFallback(parameterId).add(value, weight)
      case FallbackParameterValueUpdate(parameterId, value, weight) => model.parameterWithFallback(parameterId).update(value, weight)
      case PartOpacityUpdate(partId, value) => model.parts.get(partId).foreach(_.opacity = value)
    }
  }
}
