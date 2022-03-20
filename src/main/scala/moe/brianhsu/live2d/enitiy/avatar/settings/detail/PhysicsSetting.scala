package moe.brianhsu.live2d.enitiy.avatar.settings.detail

object PhysicsSetting {
  case class NormalizationValue(minimum: Float, default: Float, maximum: Float)
  case class Normalization(position: NormalizationValue, angle: NormalizationValue)
  case class Vertex(position: Point, mobility: Float)
  case class Input()
  case class Output()
  case class Setting(
    id: String, input: List[Input], output: List[Output], vertices: List[Vertex],
    normalization: Normalization
  )

  case class PhysicsInfo(id: String, name: String)
  case class Point(x: Float, y: Float)
  case class EffectiveForce(gravity: Point, wind: Point)
  case class Meta(
    physicsSettingCount: Int, totalInputCount: Int, totalOutputCount: Int, vertexCount: Int,
    effectiveForces: EffectiveForce,
    physicsDictionary: PhysicsInfo
  )
}
case class PhysicsSetting(version: Int)
