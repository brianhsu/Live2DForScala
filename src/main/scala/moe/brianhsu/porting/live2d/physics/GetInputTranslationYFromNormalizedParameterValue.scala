package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.porting.live2d.physics.NormalizedPhysicsParameterValueGetter.normalizeParameterValue

object GetInputTranslationYFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(targetTranslation: EuclideanVector, targetAngle: MutableData[Float], value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): EuclideanVector = {
    targetTranslation.copy(
      y = targetTranslation.y + normalizeParameterValue(value, parameterMinimumValue, parameterMaximumValue, normalizationPosition.Minimum, normalizationPosition.Maximum, normalizationPosition.Default, isInverted) * weight
    )
  }
}
