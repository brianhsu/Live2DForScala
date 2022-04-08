package moe.brianhsu.live2d.enitiy.avatar.physics.data

sealed trait ParameterType

object ParameterType {
  case object X extends ParameterType
  case object Y extends ParameterType
  case object Angle extends ParameterType

  def apply(fromString: String): ParameterType = {
    fromString match {
      case "X" => X
      case "Y" => Y
      case "Angle" => Angle
      case _ => throw new UnsupportedOperationException("Unsupported output type")
    }
  }
}
