package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsInput, CubismPhysicsOutput, CubismPhysicsRig, CubismPhysicsSubRig, ParticleUpdateParameter}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.porting.live2d.physics.CubismPhysics.{Options, updateParticles}

import scala.util.control.Breaks

object CubismPhysics {

  def updateParticles(strand: Array[CubismPhysicsParticle], totalTranslation: EuclideanVector,
                      totalAngle: Float, windDirection: EuclideanVector,
                      thresholdValue: Float, deltaTimeSeconds: Float,
                      airResistance: Float): Unit = {

    var totalRadian: Float = 0.0f
    var delay: Float = 0.0f
    var radian: Float = 0.0f
    var currentGravity: EuclideanVector = EuclideanVector()
    var direction: EuclideanVector = EuclideanVector()
    var velocity: EuclideanVector = EuclideanVector()
    var force: EuclideanVector = EuclideanVector()
    var newDirection: EuclideanVector = EuclideanVector()

    strand(0).position = totalTranslation

    totalRadian = Radian.degreesToRadian(totalAngle)
    currentGravity = Radian.radianToDirection(totalRadian).normalize()

    for (i <- 1 until strand.length) {
      strand(i).force = (currentGravity * strand(i).acceleration) + windDirection

      strand(i).lastPosition = strand(i).position

      delay = strand(i).delay * deltaTimeSeconds * 30.0f

      direction = EuclideanVector(
        x = strand(i).position.x - strand(i - 1).position.x,
        y = strand(i).position.y - strand(i - 1).position.y
      )

      radian = Radian.directionToRadian(strand(i).lastGravity, currentGravity) / airResistance

      direction = EuclideanVector(
        x = (Math.cos(radian).toFloat * direction.x) - (direction.y * Math.sin(radian).toFloat),
        y = (Math.sin(radian).toFloat * direction.x) + (direction.y * Math.cos(radian).toFloat)
      )

      strand(i).position = strand(i - 1).position + direction

      velocity = EuclideanVector(
        x = strand(i).velocity.x * delay,
        y = strand(i).velocity.y * delay
      )
      force = strand(i).force * delay * delay

      strand(i).position = strand(i).position + velocity + force

      newDirection = (strand(i).position - strand(i - 1).position).normalize()

      strand(i).position = strand(i - 1).position + (newDirection * strand(i).radius)

      if (Math.abs(strand(i).position.x) < thresholdValue) {
        strand(i).position = strand(i).position.copy(x = 0.0f)
      }

      if (delay != 0.0f) {
        strand(i).velocity = EuclideanVector(
          x = strand(i).position.x - strand(i).lastPosition.x,
          y = strand(i).position.y - strand(i).lastPosition.y
        )
        strand(i).velocity /= delay
        strand(i).velocity *= strand(i).mobility
      }

      strand(i).force = EuclideanVector(0.0f, 0.0f)
      strand(i).lastGravity = currentGravity
    }

  }
  case class Options(
    var Gravity: EuclideanVector = EuclideanVector(),          ///< 重力方向
    var Wind: EuclideanVector = EuclideanVector()             ///< 風の方向
  )

}
class CubismPhysics(physicsRig: CubismPhysicsRig, options: Options) {

  val MaximumWeight = 100.0f
  val MovementThreshold = 0.001f
  val AirResistance = 5.0f

  def updateOutputParameterValue(id: String, parameter: Parameter, parameterCurrent: Float, parameterValueMinimum: Float, parameterValueMaximum: Float,
                                 translation: Float, output: CubismPhysicsOutput): EffectOperation = {

    var outputScale: Float = 0.0f
    var value: Float = 0.0f
    var weight: Float = 0.0f

    outputScale = output.scaleGetter(output.translationScale, output.angleScale)

    value = translation * outputScale

    if (value < parameterValueMinimum) {
      value = parameterValueMinimum
    } else if (value > parameterValueMaximum) {
      value = parameterValueMaximum
    }

    weight = output.weight / MaximumWeight

    if (weight >= 1.0f) {
      parameter.update(value)
    } else {
      value = (parameterCurrent * (1.0f - weight)) + (value * weight)
      parameter.update(value)
    }

    ParameterValueUpdate(id, value)

  }

  def evaluate(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeSeconds: Float): List[EffectOperation] = {
    var operations: List[EffectOperation] = Nil

    for (currentSetting <- physicsRig.settings) {
      val currentParticles = currentSetting.particles.map(_.copy())
      val particleUpdateParameter = calculateParticleUpdateParameter(currentSetting, model)

      // Calculate particles position.
      updateParticles(
        currentParticles.toArray,
        particleUpdateParameter.translation,
        particleUpdateParameter.angle,
        options.Wind,
        MovementThreshold * currentSetting.normalizationPosition.maximum,
        deltaTimeSeconds,
        AirResistance
      )
      // Update output parameters.

      val currentParticles2 = currentParticles.map(_.copy()).toArray

      for (output <- currentSetting.outputs.takeWhile(_.hasValidVertexIndex(currentParticles2.length))) {
        val particleIndex = output.vertexIndex
        val translation = EuclideanVector(
          x = currentParticles2(particleIndex).position.x - currentParticles2(particleIndex - 1).position.x,
          y = currentParticles2(particleIndex).position.y - currentParticles2(particleIndex - 1).position.y
        )

        val outputValue = output.valueGetter(
          translation,
          currentParticles2,
          particleIndex,
          output.isReflect,
          options.Gravity
        )

        operations ::= updateOutputParameterValue(
          output.destination.id,
          model.parameters(output.destination.id),
          model.parameters(output.destination.id).current,
          model.parameters(output.destination.id).min,
          model.parameters(output.destination.id).max,
          outputValue,
          output
        )
      }
      currentSetting.particles = currentParticles2.toList

    }
    operations.reverse
  }

  private def calculateParticleUpdateParameter(currentSetting: CubismPhysicsSubRig, model: Live2DModel): ParticleUpdateParameter = {
    var particleUpdateParameter = ParticleUpdateParameter(EuclideanVector(0.0f, 0.0f), 0.0f)

    // Load input parameters.
    for (input <- currentSetting.inputs) {
      val weight = input.weight / MaximumWeight

      particleUpdateParameter = input.getNormalizedParameterValue(
        particleUpdateParameter,
        model.parameters(input.source.id).current,
        model.parameters(input.source.id).min,
        model.parameters(input.source.id).max,
        model.parameters(input.source.id).default,
        currentSetting.normalizationPosition,
        currentSetting.normalizationAngle,
        input.isReflect,
        weight
      )
    }

    val radAngle = Radian.degreesToRadian(-particleUpdateParameter.angle)
    val totalTranslation = EuclideanVector(
      x = particleUpdateParameter.translation.x * Math.cos(radAngle).toFloat - particleUpdateParameter.translation.y * Math.sin(radAngle).toFloat,
      y = particleUpdateParameter.translation.x * Math.sin(radAngle).toFloat + particleUpdateParameter.translation.y * Math.cos(radAngle).toFloat
    )

    particleUpdateParameter.copy(translation = totalTranslation)
  }
}
