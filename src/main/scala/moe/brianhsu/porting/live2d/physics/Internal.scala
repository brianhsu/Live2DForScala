package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.porting.live2d.math.CubismVector

trait PointerToFloat

/**
 *
 * 物理演算の適用先の種類。
 */
trait CubismPhysicsTargetType
case object CubismPhysicsTargetType_Parameter extends CubismPhysicsTargetType ///< パラメータに対して適用

/**
 * 物理演算の入力の種類。
 */
trait CubismPhysicsSource
case object CubismPhysicsSource_X extends CubismPhysicsSource          ///< X軸の位置から
case object CubismPhysicsSource_Y extends CubismPhysicsSource          ///< Y軸の位置から
case object CubismPhysicsSource_Angle extends CubismPhysicsSource      ///< 角度から

/**
 * 物理演算のパラメータ情報。
 */
class CubismPhysicsParameter {
  var Id: String = null
  var TargetType: CubismPhysicsTargetType = null
}

/**
 *
 * 物理演算の正規化情報。
 */
class CubismPhysicsNormalization {
  var Minimum: Float = 0.0f
  var Maximum: Float = 0.0f
  var Default: Float = 0.0f
}



/**
 * @brief 物理演算の演算に使用する物理点の情報
 *
 * 物理演算の演算に使用する物理点の情報。
 */
class CubismPhysicsParticle {
  var InitialPosition: CubismVector = null ///< 初期位置
  var Mobility: Float = 0.0f///< 動きやすさ
  var Delay: Float = 0.0f ///< 遅れ
  var Acceleration: Float = 0.0f ///< 加速度
  var Radius: Float = 0.0f ///< 距離
  var Position: CubismVector = null///< 現在の位置
  var LastPosition: CubismVector = null///< 最後の位置
  var LastGravity: CubismVector = null ///< 最後の重力
  var Force: CubismVector = null ///< 現在かかっている力
  var Velocity: CubismVector = null ///< 現在の速度
}

/**
 * @brief 物理演算の物理点の管理
 *
 * 物理演算の物理点の管理。
 */
class CubismPhysicsSubRig {
  var InputCount: Int = 0 ///< 入力の個数
  var OutputCount: Int = 0 ///< 出力の個数
  var ParticleCount: Int = 0 ///< 物理点の個数
  var BaseInputIndex: Int = 0 ///< 入力の最初のインデックス
  var BaseOutputIndex: Int = 0///< 出力の最初のインデックス
  var BaseParticleIndex: Int = 0///< 物理点の最初のインデックス
  var NormalizationPosition: CubismPhysicsNormalization = new CubismPhysicsNormalization///< 正規化された位置
  var NormalizationAngle: CubismPhysicsNormalization = new CubismPhysicsNormalization///< 正規化された角度
}

trait NormalizedPhysicsParameterValueGetter {
  /**
   * 正規化されたパラメータの取得関数の宣言。
   *
   * @param targetTranslation       演算結果の移動値
   * @param targetAngle             演算結果の角度
   * @param value                   パラメータの値
   * @param parameterMinimumValue   パラメータの最小値
   * @param parameterMaximumValue   パラメータの最大値
   * @param parameterDefaultValue   パラメータのデフォルト値
   * @param normalizationPosition   正規化された位置
   * @param normalizationAngle      正規化された角度
   * @param isInverted              値が反転されているか？
   * @param weight                  重み
   */
  def apply(targetTranslation: CubismVector,
            targetAngle: PointerToFloat,
            value: Float,
            parameterMinimumValue: Float,
            parameterMaximumValue: Float,
            parameterDefaultValue: Float,
            normalizationPosition: CubismPhysicsNormalization,
            normalizationAngle: CubismPhysicsNormalization,
            isInverted: Int,
            weight: Float): Unit
}

trait PhysicsValueGetter {
  /**
   * 物理演算の値の取得関数の宣言。
   *
   * @param       translation     移動値
   * @param       particles       物理点のリスト
   * @param       isInverted      値が反転されているか？
   * @param       parentGravity   重力
   * @return  値
   */
  def apply(translation: CubismVector,
            particles: CubismPhysicsParticle,
            particleIndex: Int,
            isInverted: Int,
            parentGravity: CubismVector): Float

}

trait PhysicsScaleGetter {
  /**
   *
   * 物理演算のスケールの取得関数の宣言。
   *
   * @param   translationScale    移動値のスケール
   * @param   angleScale          角度のスケール
   * @return  スケール値
   */
  def apply(translationScale: CubismVector, angleScale: Float): Float
}

/**
 * @brief 物理演算の入力情報
 *
 * 物理演算の入力情報。
 */
class CubismPhysicsInput {
  var Source: CubismPhysicsParameter = null ///< 入力元のパラメータ
  var SourceParameterIndex: Int = 0 ///< 入力元のパラメータのインデックス
  var Weight: Float = 0 ///< 重み
  var Type: CubismPhysicsSource = null ///< 入力の種類
  var Reflect: Boolean = false///< 値が反転されているかどうか
  var GetNormalizedParameterValue: NormalizedPhysicsParameterValueGetter = null ///< 正規化されたパラメータ値の取得関数
}

/**
 *
 * 物理演算の出力情報。
 */
class CubismPhysicsOutput {
 var Destination: CubismPhysicsParameter = null         ///< 出力先のパラメータ
 var DestinationParameterIndex: Int = 0         ///< 出力先のパラメータのインデックス
 var VertexIndex: Int = 0                       ///< 振り子のインデックス
 var TranslationScale: CubismVector = null             ///< 移動値のスケール
 var AngleScale: Float = 0                      ///< 角度のスケール
 var Weight: Float = 0                          /// 重み
 var Type: CubismPhysicsSource = null                   ///< 出力の種類
 var Reflect: Boolean = false                           ///< 値が反転されているかどうか
 var ValueBelowMinimum: Float = null               ///< 最小値を下回った時の値
 var ValueExceededMaximum: Float = null            ///< 最大値をこえた時の値
 var GetValue: PhysicsValueGetter = null                 ///< 物理演算の値の取得関数
 var GetScale: PhysicsScaleGetter = null               ///< 物理演算のスケール値の取得関数
}

/**
 * 物理演算のデータ。
 */
class CubismPhysicsRig {
  var SubRigCount: Int = 0 ///< 物理演算の物理点の個数
  var Settings: Array[CubismPhysicsSubRig] = null ///< 物理演算の物理点の管理のリスト
  var Inputs: Array[CubismPhysicsInput] = null ///< 物理演算の入力のリスト
  var Outputs: Array[CubismPhysicsOutput] = null ///< 物理演算の出力のリスト
  var Particles: Array[CubismPhysicsParticle] = null ///< 物理演算の物理点のリスト
  var Gravity: CubismVector = null///< 重力
  var Wind: CubismVector = null ///< 風
}

case class Options(Gravity: CubismVector, Wind: CubismVector)
