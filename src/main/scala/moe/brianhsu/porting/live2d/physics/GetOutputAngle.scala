package moe.brianhsu.porting.live2d.physics
import moe.brianhsu.porting.live2d.framework.math.{CubismMath, CubismVector2}

object GetOutputAngle extends PhysicsValueGetter {
  override def apply(translation: CubismVector2, particles: Array[CubismPhysicsParticle], particleIndex: Int, isInverted: Boolean, inputParentGravity: CubismVector2): Float = {
    var outputValue: Float = 0
    var parentGravity = inputParentGravity.copy()

    if (particleIndex >= 2) {
      parentGravity = particles(particleIndex - 1).Position - particles(particleIndex - 2).Position
    } else {
      parentGravity *= -1.0f
    }

    outputValue = CubismMath.directionToRadian(parentGravity, translation)

    if (isInverted) {
      outputValue *= -1.0f
    }

    outputValue

  }
}
