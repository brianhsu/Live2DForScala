package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsNormalization, ParticleUpdateParameter}
import moe.brianhsu.porting.live2d.physics.NormalizedPhysicsParameterValueGetter.normalizeParameterValue

object GetInputAngleFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(particleUpdateParameter: ParticleUpdateParameter, value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    particleUpdateParameter.copy(
      angle = particleUpdateParameter.angle +
        normalizeParameterValue(
          value, parameterMinimumValue, parameterMaximumValue,
          normalizationAngle.minimum, normalizationAngle.maximum,
          normalizationAngle.default, isInverted
        ) * weight
    )
  }
}
