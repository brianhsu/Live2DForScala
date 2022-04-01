package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsNormalization, ParticleUpdateParameter}

object GetInputTranslationXFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(particleUpdateParameter: ParticleUpdateParameter, value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(value, parameterMinimumValue, parameterMaximumValue, normalizationPosition.minimum, normalizationPosition.maximum, normalizationPosition.default, isInverted) * weight
    val newTranslation = particleUpdateParameter.translation.copy(x = particleUpdateParameter.translation.x + normalizedValue)
    particleUpdateParameter.copy(
      translation = newTranslation
    )
  }
}
