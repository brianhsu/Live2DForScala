package moe.brianhsu.live2d.enitiy.avatar.physics

sealed trait CubismPhysicsType

object CubismPhysicsType {
  case object X extends CubismPhysicsType
  case object Y extends CubismPhysicsType
  case object Angle extends CubismPhysicsType
}
