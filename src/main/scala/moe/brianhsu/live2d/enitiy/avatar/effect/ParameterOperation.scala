package moe.brianhsu.live2d.enitiy.avatar.effect

sealed trait ParameterOperation

case class AddOperation(parameterId: String, value: Float, weight: Float) extends ParameterOperation
