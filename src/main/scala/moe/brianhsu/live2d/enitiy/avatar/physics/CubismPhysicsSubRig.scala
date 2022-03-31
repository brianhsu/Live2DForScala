package moe.brianhsu.live2d.enitiy.avatar.physics

case class CubismPhysicsSubRig(
  particleCount: Int, ///< 物理点の個数
  baseParticleIndex: Int, ///< 物理点の最初のインデックス
  normalizationPosition: CubismPhysicsNormalization, ///< 正規化された位置
  normalizationAngle: CubismPhysicsNormalization,
  inputs: List[CubismPhysicsInput],
  outputs: List[CubismPhysicsOutput]
)
