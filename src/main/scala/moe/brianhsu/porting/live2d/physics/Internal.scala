package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.porting.live2d.framework.math.{CubismVector2, MutableData}
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
  var InitialPosition = CubismVector2()          ///< 初期位置
  var Mobility: Float = 0.0f                   ///< 動きやすさ
  var Delay: Float = 0.0f                       ///< 遅れ
  var Acceleration: Float = 0.0f                ///< 加速度
  var Radius: Float = 0.0f                      ///< 距離
  var Position = CubismVector2()                 ///< 現在の位置
  var LastPosition = CubismVector2()             ///< 最後の位置
  var LastGravity = CubismVector2()              ///< 最後の重力
  var Force = CubismVector2()                    ///< 現在かかっている力
  var Velocity = CubismVector2()                 ///< 現在の速度
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
  private def GetRangeValue(min: Float, max: Float): Float = {
    val maxValue = Math.max(min, max)
    val minValue = Math.min(min, max)
    Math.abs(maxValue - minValue)
  }

  private def GetDefaultValue(min: Float, max: Float): Float = {
    val minValue = Math.min(min, max)
    minValue + (GetRangeValue(min, max) / 2.0f)
  }

  private def Sign(value: Float): Int = {
    var ret = 0

    if (value > 0.0f) {
      ret = 1
    } else if (value < 0.0f) {
      ret = -1
    }

    ret
  }

  def NormalizeParameterValue(inputValue: Float, parameterMinimum: Float,
    parameterMaximum: Float,
    normalizedMinimum: Float,
    normalizedMaximum: Float,
    normalizedDefault: Float,
    isInverted: Boolean
  ): Float = {
    var result: Float = 0.0f

    var value = inputValue
    val maxValue = Math.max(parameterMaximum, parameterMinimum)

    if (maxValue < value) {
      value = maxValue
    }

    val minValue = Math.min(parameterMaximum, parameterMinimum)

    if (minValue > value) {
      value = minValue
    }

    val minNormValue = Math.min(normalizedMinimum, normalizedMaximum)
    val maxNormValue = Math.max(normalizedMinimum, normalizedMaximum)
    val middleNormValue = normalizedDefault

    val middleValue = GetDefaultValue(minValue, maxValue)
    println("value: " + value)
    println("middleValue: " + middleValue)

    val paramValue = value - middleValue

    Sign(paramValue) match {
      case 1 =>
        val nLength: Float = maxNormValue - middleNormValue
        val pLength = maxValue - middleValue
        if (pLength != 0.0f) {
          result = paramValue * (nLength / pLength)
          result += middleNormValue
        }

      case -1 =>
        val nLength: Float = minNormValue - middleNormValue
        val pLength: Float = minValue - middleValue

        if (pLength != 0.0f) {
          result = paramValue * (nLength / pLength)
          result += middleNormValue
        }
      case 0 =>
        result = middleNormValue;
      case _ =>
    }

    if (isInverted) result else (result * -1.0f)
  }
}
trait NormalizedPhysicsParameterValueGetter {
  def apply(
    targetTranslation: CubismVector2,
    targetAngle: MutableData[Float],
    value: Float,
    parameterMinimumValue: Float,
    parameterMaximumValue: Float,
    parameterDefaultValue: Float,
    normalizationPosition: CubismPhysicsNormalization = new CubismPhysicsNormalization,
    normalizationAngle: CubismPhysicsNormalization = new CubismPhysicsNormalization,
    isInverted: Boolean,
    weight: Float
  ): Unit

}

trait PhysicsValueGetter {
  def apply(translation: CubismVector2, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, parentGravity: CubismVector2): Float
}

trait PhysicsScaleGetter {
  def apply(
    translationScale: CubismVector2,
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
  var TranslationScale: CubismVector2 = CubismVector2()             ///< 移動値のスケール
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
  var Gravity: CubismVector2 = CubismVector2()          ///< 重力
  var Wind: CubismVector2 = CubismVector2()                    ///< 風
}
