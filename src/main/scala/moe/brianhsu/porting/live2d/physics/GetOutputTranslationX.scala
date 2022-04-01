package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsParticle
import moe.brianhsu.live2d.enitiy.math.EuclideanVector

object GetOutputTranslationX extends PhysicsValueGetter {
  override def apply(translation: EuclideanVector, particles: List[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, parentGravity: EuclideanVector): Float = {
    var outputValue: Float = translation.x

    if (isInverted) {
      outputValue *= -1.0f
    }

    outputValue
  }
}
