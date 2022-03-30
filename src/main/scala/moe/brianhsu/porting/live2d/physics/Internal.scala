package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Negative, Neutral, Positive, Sign}
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsInput, CubismPhysicsNormalization, CubismPhysicsOutput, CubismPhysicsSubRig, CubismPhysicsType, TargetType}






class CubismPhysicsParticle {
  var InitialPosition = EuclideanVector()          ///< 初期位置
  var Mobility: Float = 0.0f                   ///< 動きやすさ
  var Delay: Float = 0.0f                       ///< 遅れ
  var Acceleration: Float = 0.0f                ///< 加速度
  var Radius: Float = 0.0f                      ///< 距離
  var Position = EuclideanVector()                 ///< 現在の位置
  var LastPosition = EuclideanVector()             ///< 最後の位置
  var LastGravity = EuclideanVector()              ///< 最後の重力
  var Force = EuclideanVector()                    ///< 現在かかっている力
  var Velocity = EuclideanVector()                 ///< 現在の速度
}



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
    targetTranslation: EuclideanVector,
    targetAngle: MutableData[Float],
    value: Float,
    parameterMinimumValue: Float,
    parameterMaximumValue: Float,
    parameterDefaultValue: Float,
    normalizationPosition: CubismPhysicsNormalization,
    normalizationAngle: CubismPhysicsNormalization,
    isInverted: Boolean,
    weight: Float
  ): EuclideanVector

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





class CubismPhysicsRig {
  var SubRigCount: Int = 0                           ///< 物理演算の物理点の個数
  var Settings: Array[CubismPhysicsSubRig] = Array()       ///< 物理演算の物理点の管理のリスト
  var Inputs: Array[CubismPhysicsInput] = Array()          ///< 物理演算の入力のリスト
  var Outputs: Array[CubismPhysicsOutput] = Array()         ///< 物理演算の出力のリスト
  var Particles: Array[CubismPhysicsParticle] = Array()     ///< 物理演算の物理点のリスト
  var Gravity: EuclideanVector = EuclideanVector()          ///< 重力
  var Wind: EuclideanVector = EuclideanVector()                    ///< 風
}
