package moe.brianhsu.live2d.enitiy.avatar.physics

case class CubismPhysicsSubRig(
  normalizationPosition: CubismPhysicsNormalization, ///< 正規化された位置
  normalizationAngle: CubismPhysicsNormalization,
  inputs: List[CubismPhysicsInput],
  outputs: List[CubismPhysicsOutput],
  var particles: List[CubismPhysicsParticle]
)
