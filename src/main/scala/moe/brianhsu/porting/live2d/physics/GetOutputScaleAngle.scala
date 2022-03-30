package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.CubismVector

object GetOutputScaleAngle extends PhysicsScaleGetter {
  override def apply(translationScale: CubismVector, angleScale: Float): Float = {
    angleScale
  }
}
