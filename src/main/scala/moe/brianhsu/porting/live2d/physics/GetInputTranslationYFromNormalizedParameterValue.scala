package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.{CubismVector2, MutableData}

object GetInputTranslationYFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(targetTranslation: CubismVector2, targetAngle: MutableData[Float], value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): Unit = {
    targetTranslation.Y += NormalizeParameterValue(
      value,
      parameterMinimumValue,
      parameterMaximumValue,
      parameterDefaultValue,
      normalizationPosition.Minimum,
      normalizationPosition.Maximum,
      normalizationPosition.Default,
      isInverted
    ) * weight
  }
}
