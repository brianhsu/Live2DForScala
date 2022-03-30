package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Negative, Neutral, Positive, Sign}
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.porting.live2d.physics.CubismPhysicsSource.CubismPhysicsSource
import moe.brianhsu.porting.live2d.physics.CubismPhysicsTargetType.CubismPhysicsTargetType

object CubismPhysicsTargetType extends Enumeration {
  type CubismPhysicsTargetType = Value
  val CubismPhysicsTargetType_Parameter = Value("Parameter")
}

object CubismPhysicsSource extends Enumeration {
  type CubismPhysicsSource = Value
  val CubismPhysicsSource_X = Value("X")
  val CubismPhysicsSource_Y = Value("Y")
  val CubismPhysicsSource_Angle = Value("Angle")
}

class CubismPhysicsParameter {
  var Id: String = null                          ///< パラメータID
  var TargetType: CubismPhysicsTargetType = null     ///< 適用先の種類
}

class CubismPhysicsNormalization {
  var Minimum: Float = 0.0f             ///< 最大値
  var Maximum: Float = 0.0f             ///< 最小値
  var Default: Float = 0.0f             ///< デフォルト値
}

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

class CubismPhysicsSubRig {
  var InputCount: Int = 0                                        ///< 入力の個数
  var OutputCount: Int = 0                                       ///< 出力の個数
  var ParticleCount: Int = 0                                     ///< 物理点の個数
  var BaseInputIndex: Int = 0                                    ///< 入力の最初のインデックス
  var BaseOutputIndex: Int = 0                                   ///< 出力の最初のインデックス
  var BaseParticleIndex: Int = 0                                 ///< 物理点の最初のインデックス
  var NormalizationPosition = new CubismPhysicsNormalization           ///< 正規化された位置
  var NormalizationAngle  = new CubismPhysicsNormalization              ///< 正規化された角度
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
    normalizationPosition: CubismPhysicsNormalization = new CubismPhysicsNormalization,
    normalizationAngle: CubismPhysicsNormalization = new CubismPhysicsNormalization,
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

class CubismPhysicsInput {
  var Source: CubismPhysicsParameter = new CubismPhysicsParameter ///< 入力元のパラメータ
  var SourceParameterId: String = null                  ///< 入力元のパラメータのインデックス
  var SourceParameterIndex: Int = 0                  ///< 入力元のパラメータのインデックス
  var Weight: Float = 0.0f                              ///< 重み
  var Type: CubismPhysicsSource = null                                  ///< 入力の種類
  var Reflect: Boolean = false                               ///< 値が反転されているかどうか
  var GetNormalizedParameterValue: NormalizedPhysicsParameterValueGetter = null ///< 正規化されたパラメータ値の取得関数
}

class CubismPhysicsOutput {
  var Destination: CubismPhysicsParameter = new CubismPhysicsParameter        ///< 出力先のパラメータ
  var DestinationParameterId: String = null         ///< 出力先のパラメータのインデックス
  var DestinationParameterIndex: Int = 0         ///< 出力先のパラメータのインデックス
  var VertexIndex: Int = 0                       ///< 振り子のインデックス
  var TranslationScale: EuclideanVector = EuclideanVector()             ///< 移動値のスケール
  var AngleScale: Float = 0.0f                      ///< 角度のスケール
  var Weight: Float = 0.0f                          /// 重み
  var Type: CubismPhysicsSource = null                   ///< 出力の種類
  var Reflect: Boolean = false ///< 値が反転されているかどうか
  var ValueBelowMinimum: Float = 0.0f               ///< 最小値を下回った時の値
  var ValueExceededMaximum: Float = 0.0f            ///< 最大値をこえた時の値
  var GetValue: PhysicsValueGetter = null                ///< 物理演算の値の取得関数
  var GetScale: PhysicsScaleGetter = null                ///< 物理演算のスケール値の取得関数
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
