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
case class CubismPhysicsParameter(Id: String, TargetType: CubismPhysicsTargetType)

/**
 *
 * 物理演算の正規化情報。
 */
case class CubismPhysicsNormalization(Minimum: Float, Maximum: Float, Default: Float)



/**
 * @brief 物理演算の演算に使用する物理点の情報
 *
 * 物理演算の演算に使用する物理点の情報。
 */
case class CubismPhysicsParticle(
  InitialPosition: CubismVector,          ///< 初期位置
  Mobility: Float,              ///< 動きやすさ
  Delay: Float,                    ///< 遅れ
  Acceleration: Float,                ///< 加速度
  Radius: Float,                      ///< 距離
  Position: CubismVector,                 ///< 現在の位置
  LastPosition: CubismVector,             ///< 最後の位置
  LastGravity: CubismVector,              ///< 最後の重力
  Force: CubismVector,                    ///< 現在かかっている力
  Velocity: CubismVector                 ///< 現在の速度
)

/**
 * @brief 物理演算の物理点の管理
 *
 * 物理演算の物理点の管理。
 */
case class CubismPhysicsSubRig(
  InputCount: Int,                                        ///< 入力の個数
  OutputCount: Int,                                       ///< 出力の個数
  ParticleCount: Int,                                     ///< 物理点の個数
  BaseInputIndex: Int,                                    ///< 入力の最初のインデックス
  BaseOutputIndex: Int,                                   ///< 出力の最初のインデックス
  BaseParticleIndex: Int,                                 ///< 物理点の最初のインデックス
  NormalizationPosition: CubismPhysicsNormalization,           ///< 正規化された位置
  NormalizationAngle: CubismPhysicsNormalization              ///< 正規化された角度
)

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
case class CubismPhysicsInput(
  Source: CubismPhysicsParameter,                  ///< 入力元のパラメータ
  SourceParameterIndex: Int,                  ///< 入力元のパラメータのインデックス
  Weight: Float,                              ///< 重み
  Type: Int,                                  ///< 入力の種類
  Reflect: Int,                               ///< 値が反転されているかどうか
  GetNormalizedParameterValue: NormalizedPhysicsParameterValueGetter          ///< 正規化されたパラメータ値の取得関数
)

/**
 *
 * 物理演算の出力情報。
 */
case class CubismPhysicsOutput(
 Destination: CubismPhysicsParameter,         ///< 出力先のパラメータ
 DestinationParameterIndex: Int,         ///< 出力先のパラメータのインデックス
 VertexIndex: Int,                       ///< 振り子のインデックス
 TranslationScale: CubismVector,             ///< 移動値のスケール
 AngleScale: Float,                      ///< 角度のスケール
 Weight: Float,                          /// 重み
 Type: CubismPhysicsSource,                   ///< 出力の種類
 Reflect: Int,                           ///< 値が反転されているかどうか
 ValueBelowMinimum: Float,               ///< 最小値を下回った時の値
 ValueExceededMaximum: Float,            ///< 最大値をこえた時の値
 GetValue: PhysicsValueGetter,                 ///< 物理演算の値の取得関数
 GetScale: PhysicsScaleGetter                ///< 物理演算のスケール値の取得関数
)

/**
 * 物理演算のデータ。
 */
case class CubismPhysicsRig(
  SubRigCount: Int,                           ///< 物理演算の物理点の個数
  Settings: List[CubismPhysicsSubRig],        ///< 物理演算の物理点の管理のリスト
  Inputs: List[CubismPhysicsInput],           ///< 物理演算の入力のリスト
  Outputs: List[CubismPhysicsOutput],         ///< 物理演算の出力のリスト
  Particles: List[CubismPhysicsParticle],     ///< 物理演算の物理点のリスト
  Gravity: CubismVector,                          ///< 重力
  Wind: CubismVector                             ///< 風
)

