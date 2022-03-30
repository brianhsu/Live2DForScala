package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.CubismVector

object GetOutputScaleTranslationX extends PhysicsScaleGetter {
  override def apply(translationScale: CubismVector, angleScale: Float): Float = {
    translationScale.X
  }
}
