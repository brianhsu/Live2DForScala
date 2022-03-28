package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.CubismVector2

object GetOutputTranslationX extends PhysicsValueGetter {
  override def apply(translation: CubismVector2, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, parentGravity: CubismVector2): Float = {
    var outputValue: Float = translation.X

    if (isInverted) {
      outputValue *= -1.0f
    }

    outputValue
  }
}
