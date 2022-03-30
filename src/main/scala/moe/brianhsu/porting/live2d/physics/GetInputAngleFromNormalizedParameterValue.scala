package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.porting.live2d.physics.NormalizedPhysicsParameterValueGetter.normalizeParameterValue

object GetInputAngleFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
  override def apply(targetTranslation: EuclideanVector, targetAngle: MutableData[Float], value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): EuclideanVector = {
    targetAngle.data += normalizeParameterValue(value, parameterMinimumValue, parameterMaximumValue, normalizationAngle.Minimum, normalizationAngle.Maximum, normalizationAngle.Default, isInverted) * weight
    targetTranslation
  }
}
