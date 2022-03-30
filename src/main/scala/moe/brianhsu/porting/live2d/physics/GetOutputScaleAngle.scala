package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.CubismVector2

object GetOutputScaleAngle extends PhysicsScaleGetter {
  override def apply(translationScale: CubismVector2, angleScale: Float): Float = {
    angleScale
  }
}
