package moe.brianhsu.live2d.enitiy.avatar.physics

case class CubismPhysicsSubRig(
  inputCount: Int, ///< 入力の個数
  outputCount: Int, ///< 出力の個数
  particleCount: Int, ///< 物理点の個数
  baseInputIndex: Int, ///< 入力の最初のインデックス
  baseOutputIndex: Int, ///< 出力の最初のインデックス
  baseParticleIndex: Int, ///< 物理点の最初のインデックス
  normalizationPosition: CubismPhysicsNormalization, ///< 正規化された位置
  normalizationAngle: CubismPhysicsNormalization ///< 正規化された角度
)
