package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.math.CubismVector

object GetOutputTranslationX extends PhysicsValueGetter {
  /**
   * 物理演算の値の取得関数の宣言。
   *
   * @param       translation     移動値
   * @param       particles       物理点のリスト
   * @param       isInverted      値が反転されているか？
   * @param       parentGravity   重力
   * @return 値
   */
  override def apply(translation: CubismVector, particles: CubismPhysicsParticle, particleIndex: Int, isInverted: Int, parentGravity: CubismVector): Float = ???
}
