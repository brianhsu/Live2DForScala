package moe.brianhsu.live2d.enitiy.updater

import UpdateOperation._
import moe.brianhsu.live2d.enitiy.model.Live2DModel

import java.util.regex.Pattern

class ModelUpdater(model: Live2DModel) extends Updater {
  private val camelCasePattern = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")

  override def executeOperations(operations: List[UpdateOperation]): Unit = {
    operations.foreach {
      case ParameterValueAdd(parameterId, value, weight) =>
        model.parameters
          .get(normalizeParameterID(parameterId))
          .foreach(_.add(value, weight))

      case ParameterValueUpdate(parameterId, value, weight) =>
        model.parameters
          .get(normalizeParameterID(parameterId))
          .foreach(_.update(value, weight))

      case ParameterValueMultiply(parameterId, value, weight) =>
        model.parameters
          .get(normalizeParameterID(parameterId))
          .foreach(_.multiply(value, weight))

      case FallbackParameterValueAdd(parameterId, value, weight) =>
        model.parameterWithFallback(normalizeParameterID(parameterId))
          .add(value, weight)

      case FallbackParameterValueUpdate(parameterId, value, weight) =>
        model.parameterWithFallback(normalizeParameterID(parameterId))
          .update(value, weight)

      case PartOpacityUpdate(partId, value) =>
        model.parts.get(partId)
          .foreach(_.opacity = value)
    }
  }

  private def normalizeParameterID(id: String): String = {
    id match {
      case "ParamTere" if model.isOldParameterId => "PARAM_CHEEK"
      case _ if model.isOldParameterId => camelCasePattern.split(id).map(_.toUpperCase).mkString("_")
      case _ => id
    }
  }

}
