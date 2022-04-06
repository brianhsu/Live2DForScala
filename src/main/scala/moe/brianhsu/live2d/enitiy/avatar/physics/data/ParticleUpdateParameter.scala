package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsNormalization
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Negative, Neutral, Positive, Sign}
import moe.brianhsu.live2d.enitiy.model.Parameter

case class ParticleUpdateParameter(translation: EuclideanVector, angle: Float) {
  def calculateNewAngle(parameter: Parameter, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(
      parameter.current, parameter.min, parameter.max,
      normalizationAngle, isInverted
    ) * weight

    this.copy(angle = this.angle + normalizedValue)
  }

  def calculateNewX(parameter: Parameter, normalizationPosition: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(
      parameter.current, parameter.min, parameter.max,
      normalizationPosition, isInverted) * weight

    val newTranslation = this.translation.copy(x = this.translation.x + normalizedValue)
    this.copy(translation = newTranslation)
  }

  def calculateNewY(parameter: Parameter, normalizationPosition: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(
      parameter.current, parameter.min, parameter.max,
      normalizationPosition, isInverted) * weight

    val newTranslation = this.translation.copy(y = this.translation.y + normalizedValue)
    this.copy(translation = newTranslation)
  }

  private[physics] def normalizeParameterValue(inputValue: Float,
                                               parameterMinimum: Float, parameterMaximum: Float,
                                               normalization: CubismPhysicsNormalization,
                                               isInverted: Boolean): Float = {
    val maxValue = Math.max(parameterMaximum, parameterMinimum)
    val minValue = Math.min(parameterMaximum, parameterMinimum)
    val value = inputValue.min(maxValue).max(minValue)
    val minNormalizedValue = Math.min(normalization.minimum, normalization.maximum)
    val maxNormalizedValue = Math.max(normalization.minimum, normalization.maximum)

    val middleValue = calculateMiddleValue(minValue, maxValue)
    val paramValue = value - middleValue

    val result = Sign(paramValue) match {
      case Positive =>
        calculateForPositiveParameterValue(paramValue, maxValue, middleValue, maxNormalizedValue, normalization.default)
      case Negative =>
        calculateNegativeParameterValue(paramValue, minValue, middleValue, minNormalizedValue, normalization.default)
      case Neutral => normalization.default
    }

    if (isInverted) {
      result
    } else {
      result * -1.0f
    }
  }

  private def getRangeValue(min: Float, max: Float): Float = {
    val maxValue = Math.max(min, max)
    val minValue = Math.min(min, max)
    Math.abs(maxValue - minValue)
  }

  private def calculateMiddleValue(min: Float, max: Float): Float = {
    val minValue = Math.min(min, max)
    minValue + (getRangeValue(min, max) / 2.0f)
  }

  private def calculateNegativeParameterValue(paramValue: Float, minValue: Float, middleValue: Float,
                                              minNormalizedValue: Float, middleNormalizedValue: Float) = {
    val nLength: Float = minNormalizedValue - middleNormalizedValue
    val pLength: Float = minValue - middleValue
    paramValue * (nLength / pLength) + middleNormalizedValue
  }

  private def calculateForPositiveParameterValue(paramValue: Float, maxValue: Float, middleValue: Float,
                                                 maxNormalizedValue: Float, middleNormalizedValue: Float) = {
    val nLength: Float = maxNormalizedValue - middleNormalizedValue
    val pLength = maxValue - middleValue
    paramValue * (nLength / pLength) + middleNormalizedValue
  }

}
