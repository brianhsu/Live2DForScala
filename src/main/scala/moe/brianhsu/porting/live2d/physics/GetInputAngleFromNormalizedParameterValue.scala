package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.math.CubismVector

object GetInputAngleFromNormalizedParameterValue extends NormalizedPhysicsParameterValueGetter {
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
  override def apply(targetTranslation: CubismVector, targetAngle: PointerToFloat, value: Float, parameterMinimumValue: Float, parameterMaximumValue: Float, parameterDefaultValue: Float, normalizationPosition: CubismPhysicsNormalization, normalizationAngle: CubismPhysicsNormalization, isInverted: Int, weight: Float): Unit = ???
}
