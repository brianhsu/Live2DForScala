package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

trait PhysicsScaleGetter {
  def apply(
    translationScale: EuclideanVector,
    angleScale: Float
  ): Float
}
