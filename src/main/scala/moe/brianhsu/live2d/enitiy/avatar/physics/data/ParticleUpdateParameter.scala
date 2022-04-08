package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Negative, Neutral, Positive, Sign}
import moe.brianhsu.live2d.enitiy.model.Parameter

case class ParticleUpdateParameter(translation: EuclideanVector, angle: Float) {
  def calculateNewAngle(parameter: Parameter, normalizationAngle: PhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(parameter, normalizationAngle, isInverted) * weight
    this.copy(angle = this.angle + normalizedValue)
  }

  def calculateNewX(parameter: Parameter, normalizationPosition: PhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(parameter, normalizationPosition, isInverted) * weight
    val newTranslation = this.translation.copy(x = this.translation.x + normalizedValue)
    this.copy(translation = newTranslation)
  }

  def calculateNewY(parameter: Parameter, normalizationPosition: PhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(parameter, normalizationPosition, isInverted) * weight
    val newTranslation = this.translation.copy(y = this.translation.y + normalizedValue)
    this.copy(translation = newTranslation)
  }

  private[physics] def normalizeParameterValue(parameter: Parameter,
                                               normalization: PhysicsNormalization,
                                               isInverted: Boolean): Float = {
    val maxValue = Math.max(parameter.max, parameter.min)
    val minValue = Math.min(parameter.max, parameter.min)
    val value = parameter.current.min(maxValue).max(minValue)
    val minNormalizedValue = Math.min(normalization.min, normalization.max)
    val maxNormalizedValue = Math.max(normalization.min, normalization.max)

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
