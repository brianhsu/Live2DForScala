package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.math.EuclideanVector

object GetOutputScaleTranslationX extends PhysicsScaleGetter {
  override def apply(translationScale: EuclideanVector, angleScale: Float): Float = {
    translationScale.x
  }
}
