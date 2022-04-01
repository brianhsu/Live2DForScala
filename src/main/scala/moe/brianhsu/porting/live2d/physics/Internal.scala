package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Negative, Neutral, Positive, Sign}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsInput, CubismPhysicsNormalization, CubismPhysicsOutput, CubismPhysicsParticle, CubismPhysicsSubRig, CubismPhysicsType, ParticleUpdateParameter, TargetType}









object NormalizedPhysicsParameterValueGetter {
  private def getRangeValue(min: Float, max: Float): Float = {
    val maxValue = Math.max(min, max)
    val minValue = Math.min(min, max)
    Math.abs(maxValue - minValue)
  }

  private def getDefaultValue(min: Float, max: Float): Float = {
    val minValue = Math.min(min, max)
    minValue + (getRangeValue(min, max) / 2.0f)
  }

  def normalizeParameterValue(inputValue: Float,
                              parameterMinimum: Float, parameterMaximum: Float, normalizedMinimum: Float,
                              normalizedMaximum: Float, normalizedDefault: Float, isInverted: Boolean): Float = {
    val maxValue = Math.max(parameterMaximum, parameterMinimum)
    val minValue = Math.min(parameterMaximum, parameterMinimum)
    val value = inputValue.min(maxValue).max(minValue)
    val minNormValue = Math.min(normalizedMinimum, normalizedMaximum)
    val maxNormValue = Math.max(normalizedMinimum, normalizedMaximum)
    val middleNormValue = normalizedDefault

    val middleValue = getDefaultValue(minValue, maxValue)
    val paramValue = value - middleValue

    val result = Sign(paramValue) match {
      case Positive =>
        calculateForPositiveParameterValue(maxValue, maxNormValue, middleNormValue, middleValue, paramValue)
      case Negative =>
        calculateNegativeParameterValue(minValue, minNormValue, middleNormValue, middleValue, paramValue)
      case Neutral => middleNormValue
    }

    if (isInverted) {
      result
    } else {
      result * -1.0f
    }
  }

  private def calculateNegativeParameterValue(minValue: Float, minNormValue: Float, middleNormValue: Float, middleValue: Float, paramValue: Float) = {
    val nLength: Float = minNormValue - middleNormValue
    val pLength: Float = minValue - middleValue

    if (pLength != 0.0f) {
      paramValue * (nLength / pLength) + middleNormValue
    } else {
      0.0f
    }
  }

  private def calculateForPositiveParameterValue(maxValue: Float, maxNormValue: Float, middleNormValue: Float, middleValue: Float, paramValue: Float) = {
    val nLength: Float = maxNormValue - middleNormValue
    val pLength = maxValue - middleValue
    if (pLength != 0.0f) {
      paramValue * (nLength / pLength) + middleNormValue
    } else {
      0.0f
    }
  }
}
trait NormalizedPhysicsParameterValueGetter {
  def apply(
    particleUpdateParameter: ParticleUpdateParameter,
    value: Float,
    parameterMinimumValue: Float,
    parameterMaximumValue: Float,
    parameterDefaultValue: Float,
    normalizationPosition: CubismPhysicsNormalization,
    normalizationAngle: CubismPhysicsNormalization,
    isInverted: Boolean,
    weight: Float
  ): ParticleUpdateParameter

}

trait PhysicsValueGetter {
  def apply(translation: EuclideanVector, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, parentGravity: EuclideanVector): Float
}

trait PhysicsScaleGetter {
  def apply(
    translationScale: EuclideanVector,
    angleScale: Float
  ): Float
}






