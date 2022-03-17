package moe.brianhsu.live2d.enitiy.avatar.effect

sealed trait EffectOperation

case class ParameterValueAdd(parameterId: String, value: Float, weight: Float = 1.0f) extends EffectOperation
case class ParameterValueMultiply(parameterId: String, value: Float, weight: Float = 1.0f) extends EffectOperation
case class ParameterValueUpdate(parameterId: String, value: Float, weight: Float = 1.0f) extends EffectOperation

case class FallbackParameterValueAdd(parameterId: String, value: Float, weight: Float = 1.0f) extends EffectOperation
case class FallbackParameterValueUpdate(parameterId: String, value: Float, weight: Float = 1.0f) extends EffectOperation

case class PartOpacityUpdate(partId: String, value: Float) extends EffectOperation