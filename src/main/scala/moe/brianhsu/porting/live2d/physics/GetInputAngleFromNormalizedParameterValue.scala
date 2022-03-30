package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.{CubismVector, MutableData}
import moe.brianhsu.porting.live2d.physics.NormalizedPhysicsParameterValueGetter.normalizeParameterValue

object GetInputAngleFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(targetTranslation: CubismVector, targetAngle: MutableData[Float], value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): Unit = {
    targetAngle.data += normalizeParameterValue(value, parameterMinimumValue, parameterMaximumValue, normalizationAngle.Minimum, normalizationAngle.Maximum, normalizationAngle.Default, isInverted) * weight
  }
}
