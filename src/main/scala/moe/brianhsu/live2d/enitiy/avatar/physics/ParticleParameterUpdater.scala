package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.math.{Negative, Neutral, Positive, Sign}
import moe.brianhsu.live2d.enitiy.model.Parameter

object ParticleParameterUpdater {
  def calculateNewAngle(particleUpdateParameter: ParticleUpdateParameter, parameter: Parameter, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    particleUpdateParameter.copy(
      angle = particleUpdateParameter.angle +
        normalizeParameterValue(
          parameter.current, parameter.min, parameter.max,
          normalizationAngle.minimum, normalizationAngle.maximum,
          normalizationAngle.default, isInverted
        ) * weight
    )
  }

  def calculateNewX(particleUpdateParameter: ParticleUpdateParameter, parameter: Parameter, normalizationPosition: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(parameter.current, parameter.min, parameter.max, normalizationPosition.minimum, normalizationPosition.maximum, normalizationPosition.default, isInverted) * weight
    val newTranslation = particleUpdateParameter.translation.copy(x = particleUpdateParameter.translation.x + normalizedValue)
    particleUpdateParameter.copy(
      translation = newTranslation
    )
  }

  def calculateNewY(particleUpdateParameter: ParticleUpdateParameter, parameter: Parameter, normalizationPosition: CubismPhysicsNormalization, isInverted: Boolean, weight: Float): ParticleUpdateParameter = {
    val normalizedValue = normalizeParameterValue(parameter.current, parameter.min, parameter.max, normalizationPosition.minimum, normalizationPosition.maximum, normalizationPosition.default, isInverted) * weight
    val newTranslation = particleUpdateParameter.translation.copy(y = particleUpdateParameter.translation.y + normalizedValue)
    particleUpdateParameter.copy(
      translation = newTranslation
    )
  }

  def normalizeParameterValue(inputValue: Float,
    parameterMinimum: Float, parameterMaximum: Float, normalizedMinimum: Float,
    normalizedMaximum: Float, normalizedDefault: Float, isInverted: Boolean): Float = {
    val maxValue = Math.max(parameterMaximum, parameterMinimum)
    val minValue = Math.min(parameterMaximum, parameterMinimum)
    val value = inputValue.min(maxValue).max(minValue)
    val minNormalizedValue = Math.min(normalizedMinimum, normalizedMaximum)
    val maxNormalizedValue = Math.max(normalizedMinimum, normalizedMaximum)

    val middleValue = getDefaultValue(minValue, maxValue)
    val paramValue = value - middleValue

    val result = Sign(paramValue) match {
      case Positive =>
        calculateForPositiveParameterValue(maxValue, maxNormalizedValue, normalizedDefault, middleValue, paramValue)
      case Negative =>
        calculateNegativeParameterValue(minValue, minNormalizedValue, normalizedDefault, middleValue, paramValue)
      case Neutral => normalizedDefault
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

  private def getDefaultValue(min: Float, max: Float): Float = {
    val minValue = Math.min(min, max)
    minValue + (getRangeValue(min, max) / 2.0f)
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
