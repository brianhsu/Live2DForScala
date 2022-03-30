package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.{CubismVector, MutableData}
import moe.brianhsu.porting.live2d.physics.NormalizedPhysicsParameterValueGetter.normalizeParameterValue

object GetInputTranslationYFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(targetTranslation: CubismVector, targetAngle: MutableData[Float], value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): CubismVector = {
    targetTranslation.copy(
      y = targetTranslation.y + normalizeParameterValue(value, parameterMinimumValue, parameterMaximumValue, normalizationPosition.Minimum, normalizationPosition.Maximum, normalizationPosition.Default, isInverted) * weight
    )
  }
}
