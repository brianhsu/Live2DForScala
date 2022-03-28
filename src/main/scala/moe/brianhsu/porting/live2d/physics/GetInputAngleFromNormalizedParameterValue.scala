package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.{CubismVector2, MutableData}

object GetInputAngleFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(targetTranslation: CubismVector2, targetAngle: MutableData[Float], value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): Unit = {
    targetAngle.data += NormalizeParameterValue(
      value,
      parameterMinimumValue,
      parameterMaximumValue,
      parameterDefaultValue,
      normalizationAngle.Minimum,
      normalizationAngle.Maximum,
      normalizationAngle.Default,
      isInverted
    ) * weight
  }
}
