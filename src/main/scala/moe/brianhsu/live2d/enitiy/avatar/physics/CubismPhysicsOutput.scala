package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.porting.live2d.physics.{CubismPhysicsParameter, PhysicsScaleGetter, PhysicsValueGetter}

case class CubismPhysicsOutput(
  destination: CubismPhysicsParameter, ///< 出力先のパラメータ
  vertexIndex: Int, ///< 振り子のインデックス
  angleScale: Float, ///< 角度のスケール
  weight: Float, /// 重み
  outType: CubismPhysicsType, ///< 出力の種類
  isReflect: Boolean, ///< 値が反転されているかどうか
  valueGetter: PhysicsValueGetter, ///< 物理演算の値の取得関数
  scaleGetter: PhysicsScaleGetter, ///< 物理演算のスケール値の取得関数
  translationScale: EuclideanVector = EuclideanVector() ///< 移動値のスケール
)
