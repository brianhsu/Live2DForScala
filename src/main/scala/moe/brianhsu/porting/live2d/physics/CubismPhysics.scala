package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsType.{Angle, X, Y}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsOutput, CubismPhysicsParticle, CubismPhysicsRig, CubismPhysicsSubRig, ParticleUpdateParameter}
import moe.brianhsu.live2d.enitiy.avatar.updater.{ParameterValueUpdate, UpdateOperation}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class CubismPhysics(physicsRig: CubismPhysicsRig, gravityDirection: EuclideanVector, windDirection: EuclideanVector) {

  private val MaximumWeight = 100.0f

  def evaluate(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeSeconds: Float): List[UpdateOperation] = {
    var operations: List[UpdateOperation] = Nil

    for (currentSetting <- physicsRig.settings) {
      val particleUpdateParameter = currentSetting.calculateParticleUpdateParameter(model)
      val updatedParticles = currentSetting.updateParticles(particleUpdateParameter, windDirection, deltaTimeSeconds)

      val operationsForSetting = currentSetting
        .outputs
        .takeWhile(_.hasValidVertexIndex(updatedParticles.length))
        .map(output => createUpdateOperation(output, updatedParticles, model))

      operations ++= operationsForSetting
      currentSetting.particles = updatedParticles
    }

    operations
  }

  private def createUpdateOperation(output: CubismPhysicsOutput, particles: List[CubismPhysicsParticle], model: Live2DModel): UpdateOperation = {
    val particleIndex = output.vertexIndex
    val translation = particles(particleIndex).position - particles(particleIndex - 1).position
    val outputValue = output.outType match {
      case X => if (output.isReflect) -translation.x else translation.x
      case Y => if (output.isReflect) -translation.y else translation.y
      case Angle =>
        val outputAngle = calculateOutputAngle(translation, particles, particleIndex, gravityDirection)
        if (output.isReflect) -outputAngle else outputAngle
    }


    calculateUpdateOperation(
      output.destination.id,
      model.parameters(output.destination.id).current,
      model.parameters(output.destination.id).min,
      model.parameters(output.destination.id).max,
      outputValue,
      output
    )

  }

  private def calculateOutputAngle(translation: EuclideanVector, particles: List[CubismPhysicsParticle], particleIndex: Int, inputParentGravity: EuclideanVector): Float = {
    val parentGravity = if (particleIndex >= 2) {
      particles(particleIndex - 1).position - particles(particleIndex - 2).position
    } else {
      inputParentGravity * -1.0f
    }

    Radian.directionToRadian(parentGravity, translation)
  }


  private def calculateUpdateOperation(id: String, parameterCurrentValue: Float, parameterValueMinimum: Float, parameterValueMaximum: Float,
    translation: Float, output: CubismPhysicsOutput): UpdateOperation = {

    val outputScale = output.outType match {
      case X => output.translationScale.x
      case Y => output.translationScale.y
      case Angle => output.angleScale
    }

    val value = (translation * outputScale).max(parameterValueMinimum).min(parameterValueMaximum)
    val weight = output.weight / MaximumWeight
    val valueWithWeight = if (weight >= 1.0f) {
      value
    } else {
      (parameterCurrentValue * (1.0f - weight)) + (value * weight)
    }

    ParameterValueUpdate(id, valueWithWeight)

  }

}
