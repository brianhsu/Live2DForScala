package moe.brianhsu.live2d.enitiy.avatar.settings.detail

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting.{Meta, Setting}

object PhysicsSetting {
  case class NormalizationValue(minimum: Float, default: Float, maximum: Float)
  case class Normalization(position: NormalizationValue, angle: NormalizationValue)
  case class Vertex(position: Point, mobility: Float, delay: Float, acceleration: Float, radius: Float)
  case class Target(target: String, id: String)
  case class Input(source: Target, weight: Float, `type`: String, reflect: Boolean)
  case class Output(destination: Target, vertexIndex: Int, scale: Float, weight: Float, `type`: String, reflect: Boolean)
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
    physicsDictionary: List[PhysicsInfo]
  )
}

case class PhysicsSetting(version: Int, meta: Meta, physicsSettings: List[Setting])
