package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

case class PhysicsOutput(destination: PhysicsParameter,
                         outputType: ParameterType,
                         vertexIndex: Int,
                         translationScale: EuclideanVector,
                         angleScale: Float,
                         isReflect: Boolean,
                         weight: Float) {

  def hasValidVertexIndex(particleCount: Int): Boolean = {
    vertexIndex >= 1 && vertexIndex < particleCount
  }
}
