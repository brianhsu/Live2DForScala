package moe.brianhsu.live2d.enitiy.avatar.effect

sealed trait UpdateOperation

case class ParameterValueAdd(parameterId: String, value: Float, weight: Float = 1.0f) extends UpdateOperation
case class ParameterValueMultiply(parameterId: String, value: Float, weight: Float = 1.0f) extends UpdateOperation
case class ParameterValueUpdate(parameterId: String, value: Float, weight: Float = 1.0f) extends UpdateOperation

case class FallbackParameterValueAdd(parameterId: String, value: Float, weight: Float = 1.0f) extends UpdateOperation
case class FallbackParameterValueUpdate(parameterId: String, value: Float, weight: Float = 1.0f) extends UpdateOperation

case class PartOpacityUpdate(partId: String, value: Float) extends UpdateOperation