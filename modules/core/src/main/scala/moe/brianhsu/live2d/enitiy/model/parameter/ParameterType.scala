package moe.brianhsu.live2d.enitiy.model.parameter

import moe.brianhsu.live2d.exception.UnknownParameterTypeValueException

sealed trait ParameterType

object ParameterType {
  case object Normal extends ParameterType
  case object BlendShape extends ParameterType

  def apply(value: Int): ParameterType = value match {
    case 0 => Normal
    case 1 => BlendShape
    case unknown => throw new UnknownParameterTypeValueException(unknown)
  }
}
