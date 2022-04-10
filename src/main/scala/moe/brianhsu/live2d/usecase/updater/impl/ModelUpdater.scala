package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation.{FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.usecase.updater.{UpdateOperation, Updater}

class ModelUpdater(model: Live2DModel) extends Updater {
  override def executeOperations(operations: List[UpdateOperation]): Unit = {
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
