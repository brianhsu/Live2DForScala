package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}

object GetOutputAngle extends PhysicsValueGetter {
  override def apply(translation: EuclideanVector, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, inputParentGravity: EuclideanVector): Float = {
    var outputValue: Float = 0
    var parentGravity = inputParentGravity.copy()

    if (particleIndex >= 2) {
      parentGravity = particles(particleIndex - 1).Position - particles(particleIndex - 2).Position
    } else {
      parentGravity *= -1.0f
    }

    outputValue = Radian.directionToRadian(parentGravity, translation)

    if (isInverted) {
      outputValue *= -1.0f
    }

    outputValue

  }
}
