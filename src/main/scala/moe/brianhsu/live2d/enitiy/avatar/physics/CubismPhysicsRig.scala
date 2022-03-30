package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.porting.live2d.physics.CubismPhysicsParticle

case class CubismPhysicsRig(
  subRigCount: Int, ///< 物理演算の物理点の個数
  settings: Array[CubismPhysicsSubRig], ///< 物理演算の物理点の管理のリスト
  inputs: Array[CubismPhysicsInput], ///< 物理演算の入力のリスト
  outputs: Array[CubismPhysicsOutput], ///< 物理演算の出力のリスト
  particles: Array[CubismPhysicsParticle], ///< 物理演算の物理点のリスト
  gravity: EuclideanVector, ///< 重力
  wind: EuclideanVector ///< 風
)
