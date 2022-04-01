package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsOutput, CubismPhysicsParticle, CubismPhysicsRig, CubismPhysicsSubRig, ParticleUpdateParameter}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.porting.live2d.physics.CubismPhysics.{Options, updateParticles}

object CubismPhysics {

  def updateParticles(particles: List[CubismPhysicsParticle],
                      particleUpdateParameter: ParticleUpdateParameter,
                      windDirection: EuclideanVector,
                      thresholdValue: Float, deltaTimeSeconds: Float,
                      airResistance: Float): List[CubismPhysicsParticle] = {

    var resultList: List[CubismPhysicsParticle] = Nil
    val initParticle = particles.head.copy(
      position = particleUpdateParameter.translation
    )

    val totalRadian = Radian.degreesToRadian(particleUpdateParameter.angle)
    val currentGravity = Radian.radianToDirection(totalRadian).normalize()
    var previousParticle = initParticle

    resultList ::= initParticle

    for (currentParticle <- particles.drop(1)) {
      val initForce = (currentGravity * currentParticle.acceleration) + windDirection
      val lastPosition = currentParticle.position
      val delay = currentParticle.delay * deltaTimeSeconds * 30.0f
      val newDirection = calculateNewDirection(currentParticle, previousParticle, currentGravity, initForce, delay, airResistance)
      val finalPosition = calculateFinalPosition(thresholdValue, previousParticle, currentParticle, newDirection)

      val velocity = if (delay != 0.0f) {
        (finalPosition - lastPosition) / delay * currentParticle.mobility
      } else {
        EuclideanVector(0.0f, 0.0f)
      }

      val updatedParticle = currentParticle.copy(
        velocity = velocity,
        position = finalPosition,
        lastPosition = lastPosition,
        lastGravity = currentGravity
      )

      resultList ::= updatedParticle
      previousParticle = updatedParticle
    }

    resultList.reverse

  }

  private def calculateFinalPosition(thresholdValue: Float, previousParticle: CubismPhysicsParticle, currentParticle: CubismPhysicsParticle, newDirection: EuclideanVector): EuclideanVector = {
    val finalPosition = previousParticle.position + (newDirection * currentParticle.radius)

    if (Math.abs(finalPosition.x) < thresholdValue) {
      finalPosition.copy(x = 0.0f)
    } else {
      finalPosition
    }
  }

  private def calculateNewDirection(currentParticle: CubismPhysicsParticle, previousParticle: CubismPhysicsParticle,
                                    currentGravity: EuclideanVector, initForce: EuclideanVector, delay: Float,
                                    airResistance: Float): EuclideanVector = {
    val direction = EuclideanVector(
      x = currentParticle.position.x - previousParticle.position.x,
      y = currentParticle.position.y - previousParticle.position.y
    )

    val radian = Radian.directionToRadian(currentParticle.lastGravity, currentGravity) / airResistance

    val directionWithRadian = EuclideanVector(
      x = (Math.cos(radian).toFloat * direction.x) - (direction.y * Math.sin(radian).toFloat),
      y = (Math.sin(radian).toFloat * direction.x) + (direction.y * Math.cos(radian).toFloat)
    )

    val positionWithDirection = previousParticle.position + directionWithRadian

    val velocity = currentParticle.velocity * delay
    val force = initForce * delay * delay

    val positionWithVelocityForce = positionWithDirection + velocity + force

    (positionWithVelocityForce - previousParticle.position).normalize()
  }

  case class Options(
    var Gravity: EuclideanVector = EuclideanVector(0.0f, 0.0f),          ///< 重力方向
    var Wind: EuclideanVector = EuclideanVector(0.0f, 0.0f)             ///< 風の方向
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
      val particleUpdateParameter = currentSetting.calculateParticleUpdateParameter(model)
      val updatedParticles = updateParticles(
        currentSetting.particles,
        particleUpdateParameter,
        options.Wind,
        MovementThreshold * currentSetting.normalizationPosition.maximum,
        deltaTimeSeconds,
        AirResistance
      )

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
      particles.toArray,
      particleIndex,
      output.isReflect,
      options.Gravity
    )

    updateOutputParameterValue(
      output.destination.id,
      model.parameters(output.destination.id),
      model.parameters(output.destination.id).current,
      model.parameters(output.destination.id).min,
      model.parameters(output.destination.id).max,
      outputValue,
      output
    )

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
