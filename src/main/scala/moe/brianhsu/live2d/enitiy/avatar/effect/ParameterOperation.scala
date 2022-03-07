package moe.brianhsu.live2d.enitiy.avatar.effect

sealed trait ParameterOperation

case class AddOperation(parameterId: String, value: Float, weight: Float = 1.0f) extends ParameterOperation
case class UpdateOperation(parameterId: String, value: Float, weight: Float = 1.0f) extends ParameterOperation
