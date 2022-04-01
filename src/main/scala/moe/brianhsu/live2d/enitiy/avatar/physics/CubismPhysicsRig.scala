package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class CubismPhysicsRig(
  settings: List[CubismPhysicsSubRig], ///< 物理演算の物理点の管理のリスト
  gravity: EuclideanVector, ///< 重力
  wind: EuclideanVector ///< 風
)
