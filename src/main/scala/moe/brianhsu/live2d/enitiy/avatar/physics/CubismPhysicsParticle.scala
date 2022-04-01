package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class CubismPhysicsParticle(
  mobility: Float,                   ///< 動きやすさ
  delay: Float,                       ///< 遅れ
  acceleration: Float,                ///< 加速度
  radius: Float,                      ///< 距離
  initialPosition: EuclideanVector,          ///< 初期位置
  position: EuclideanVector,                 ///< 現在の位置
  lastPosition: EuclideanVector,             ///< 最後の位置
  lastGravity: EuclideanVector,              ///< 最後の重力
  velocity: EuclideanVector                 ///< 現在の速度
)
