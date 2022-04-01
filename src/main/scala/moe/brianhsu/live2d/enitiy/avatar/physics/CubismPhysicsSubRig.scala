package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsSubRig.MaximumWeight
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object CubismPhysicsSubRig {
  val MaximumWeight = 100.0f
}

case class CubismPhysicsSubRig(
  normalizationPosition: CubismPhysicsNormalization, ///< 正規化された位置
  normalizationAngle: CubismPhysicsNormalization,
  inputs: List[CubismPhysicsInput],
  outputs: List[CubismPhysicsOutput],
  var particles: List[CubismPhysicsParticle]
) {

  def calculateParticleUpdateParameter(model: Live2DModel): ParticleUpdateParameter = {
    var particleUpdateParameter = ParticleUpdateParameter(EuclideanVector(0.0f, 0.0f), 0.0f)

    for (input <- inputs) {
      val weight = input.weight / MaximumWeight

      particleUpdateParameter = input.getNormalizedParameterValue(
        particleUpdateParameter,
        model.parameters(input.source.id).current,
        model.parameters(input.source.id).min,
        model.parameters(input.source.id).max,
        model.parameters(input.source.id).default,
        normalizationPosition,
        normalizationAngle,
        input.isReflect,
        weight
      )
    }

    val radAngle = Radian.degreesToRadian(-particleUpdateParameter.angle)
    val totalTranslation = EuclideanVector(
      x = particleUpdateParameter.translation.x * Math.cos(radAngle).toFloat - particleUpdateParameter.translation.y * Math.sin(radAngle).toFloat,
      y = particleUpdateParameter.translation.x * Math.sin(radAngle).toFloat + particleUpdateParameter.translation.y * Math.cos(radAngle).toFloat
    )

    particleUpdateParameter.copy(translation = totalTranslation)
  }

}
