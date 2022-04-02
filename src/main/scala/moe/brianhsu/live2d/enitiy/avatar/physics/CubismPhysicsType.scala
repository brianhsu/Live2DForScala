package moe.brianhsu.live2d.enitiy.avatar.physics

sealed trait CubismPhysicsType

object CubismPhysicsType {
  case object X extends CubismPhysicsType
  case object Y extends CubismPhysicsType
  case object Angle extends CubismPhysicsType

  def apply(fromString: String): CubismPhysicsType = {
    fromString match {
      case "X" => X
      case "Y" => Y
      case "Angle" => Angle
      case _ => throw new UnsupportedOperationException("Unsupported output type")
    }
  }
}
