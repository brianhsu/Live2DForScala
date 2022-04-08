package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.avatar.physics.data.{ParameterType, PhysicsParameter}
import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class CubismPhysicsOutput(destination: PhysicsParameter, ///< 出力先のパラメータ
                                vertexIndex: Int, ///< 振り子のインデックス
                                angleScale: Float, ///< 角度のスケール
                                weight: Float, /// 重み
                                outType: ParameterType, ///< 出力の種類
                                isReflect: Boolean, ///< 値が反転されているかどうか
                                translationScale: EuclideanVector ///< 移動値のスケール
) {
  def hasValidVertexIndex(particleCount: Int): Boolean = {
    vertexIndex >= 1 && vertexIndex < particleCount
  }
}
