package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.math.CubismVector

object GetOutputScaleAngle extends PhysicsScaleGetter {
  /**
   *
   * 物理演算のスケールの取得関数の宣言。
   *
   * @param   translationScale    移動値のスケール
   * @param   angleScale          角度のスケール
   * @return スケール値
   */
  override def apply(translationScale: CubismVector, angleScale: Float): Float = ???
}
