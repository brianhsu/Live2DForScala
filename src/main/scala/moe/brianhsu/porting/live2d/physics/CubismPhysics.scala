package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsOutput, CubismPhysicsParticle, CubismPhysicsRig, CubismPhysicsSubRig, ParticleUpdateParameter}
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class CubismPhysics(physicsRig: CubismPhysicsRig, gravityDirection: EuclideanVector, windDirection: EuclideanVector) {

  private val MaximumWeight = 100.0f

  def evaluate(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeSeconds: Float): List[EffectOperation] = {
    var operations: List[EffectOperation] = Nil

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

  private def createUpdateOperation(output: CubismPhysicsOutput, particles: List[CubismPhysicsParticle], model: Live2DModel): EffectOperation = {
    val particleIndex = output.vertexIndex
    val translation = particles(particleIndex).position - particles(particleIndex - 1).position
    val outputValue = output.valueGetter(
      translation,
      particles,
      particleIndex,
      output.isReflect,
      gravityDirection
    )

    calculateUpdateOperation(
      output.destination.id,
      model.parameters(output.destination.id).current,
      model.parameters(output.destination.id).min,
      model.parameters(output.destination.id).max,
      outputValue,
      output
    )

  }

  private def calculateUpdateOperation(id: String, parameterCurrentValue: Float, parameterValueMinimum: Float, parameterValueMaximum: Float,
    translation: Float, output: CubismPhysicsOutput): EffectOperation = {

    val outputScale = output.scaleGetter(output.translationScale, output.angleScale)
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
