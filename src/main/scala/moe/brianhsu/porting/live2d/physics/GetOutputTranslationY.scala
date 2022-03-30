package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.CubismVector

object GetOutputTranslationY extends PhysicsValueGetter {
  override def apply(translation: CubismVector, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, parentGravity: CubismVector): Float = {
    var outputValue: Float = translation.Y

    if (isInverted) {
      outputValue *= -1.0f
    }

    outputValue
  }
}
