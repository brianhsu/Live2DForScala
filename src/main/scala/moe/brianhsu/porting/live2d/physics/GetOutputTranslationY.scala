package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsParticle
import moe.brianhsu.live2d.enitiy.math.EuclideanVector

object GetOutputTranslationY extends PhysicsValueGetter {
  override def apply(translation: EuclideanVector, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, parentGravity: EuclideanVector): Float = {
    var outputValue: Float = translation.y

    if (isInverted) {
      outputValue *= -1.0f
    }

    outputValue
  }
}
