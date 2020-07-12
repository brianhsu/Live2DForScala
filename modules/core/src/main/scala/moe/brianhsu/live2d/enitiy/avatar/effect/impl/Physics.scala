package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.ParameterType.{Angle, X, Y}
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.{PhysicsData, PhysicsEffect, PhysicsOutput, PhysicsParticle}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueUpdate

case class Physics(physicsData: PhysicsData, var gravityDirection: EuclideanVector, var windDirection: EuclideanVector) extends Effect {

  private val MaximumWeight = 100.0f
  private var currentParticlesMap: Map[PhysicsEffect, List[PhysicsParticle]] = Map.empty

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float): List[UpdateOperation] = {
    var operations: List[UpdateOperation] = Nil

    for (currentSetting <- physicsData.effects) {
      val particleUpdateParameter = currentSetting.calculateParticleUpdateParameter(model)
      val currentParticles = currentParticlesMap.getOrElse(currentSetting, currentSetting.initialParticles)
      val updatedParticles = currentSetting.calculateNewParticleStatus(
        currentParticles,
        particleUpdateParameter,
        windDirection,
        deltaTimeInSeconds
      )

      val operationsForSetting = currentSetting
        .outputs
        .takeWhile(_.hasValidVertexIndex(updatedParticles.length))
        .map(output => createUpdateOperation(output, updatedParticles, model))

      operations ++= operationsForSetting
      currentParticlesMap = currentParticlesMap.updated(currentSetting, updatedParticles)
    }

    operations
  }
  override def start(): Unit = {}

  override def stop(): Unit = {}

  private def createUpdateOperation(output: PhysicsOutput, particles: List[PhysicsParticle], model: Live2DModel): UpdateOperation = {
    val particleIndex = output.vertexIndex
    val translation = particles(particleIndex).position - particles(particleIndex - 1).position
    val outputValue = output.outputType match {
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

  private def calculateOutputAngle(translation: EuclideanVector, particles: List[PhysicsParticle], particleIndex: Int, inputParentGravity: EuclideanVector): Float = {
    val parentGravity = if (particleIndex >= 2) {
      particles(particleIndex - 1).position - particles(particleIndex - 2).position
    } else {
      inputParentGravity * -1.0f
    }

    Radian.directionToRadian(parentGravity, translation)
  }

  private def calculateUpdateOperation(id: String, parameterCurrentValue: Float, parameterValueMinimum: Float, parameterValueMaximum: Float,
    translation: Float, output: PhysicsOutput): UpdateOperation = {

    val outputScale = output.outputType match {
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
