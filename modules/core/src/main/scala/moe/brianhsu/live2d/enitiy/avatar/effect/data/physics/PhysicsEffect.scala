package moe.brianhsu.live2d.enitiy.avatar.effect.data.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.ParameterType.{Angle, X, Y}
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.PhysicsEffect.{AirResistance, MaximumWeight, MovementThreshold}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object PhysicsEffect {
  val MaximumWeight = 100.0f
  val MovementThreshold = 0.001f
  val AirResistance = 5.0f
}

case class PhysicsEffect(normalizationPosition: PhysicsNormalization,
                         normalizationAngle: PhysicsNormalization,
                         inputs: List[PhysicsInput],
                         outputs: List[PhysicsOutput],
                         initialParticles: List[PhysicsParticle]
) {

  def calculateParticleUpdateParameter(model: Live2DModel): ParticleUpdateParameter = {
    var particleUpdateParameter = physics.ParticleUpdateParameter(EuclideanVector(0.0f, 0.0f), 0.0f)

    for (input <- inputs) {
      val weight = input.weight / MaximumWeight
      val parameter = model.parameters(input.source.id)

      particleUpdateParameter = input.sourceType match {
        case X =>
          particleUpdateParameter.calculateNewX(parameter, normalizationPosition, input.isReflect, weight)
        case Y =>
          particleUpdateParameter.calculateNewY(parameter, normalizationPosition, input.isReflect, weight)
        case Angle =>
          particleUpdateParameter.calculateNewAngle(parameter, normalizationAngle, input.isReflect, weight)
      }
    }

    val radAngle = Radian.degreesToRadian(-particleUpdateParameter.angle)
    val totalTranslation = EuclideanVector(
      x = particleUpdateParameter.translation.x * Math.cos(radAngle).toFloat - particleUpdateParameter.translation.y * Math.sin(radAngle).toFloat,
      y = particleUpdateParameter.translation.x * Math.sin(radAngle).toFloat + particleUpdateParameter.translation.y * Math.cos(radAngle).toFloat
    )

    particleUpdateParameter.copy(translation = totalTranslation)
  }

  def calculateNewParticleStatus(currentParticles: List[PhysicsParticle],
                                 particleUpdateParameter: ParticleUpdateParameter,
                                 windDirection: EuclideanVector,
                                 deltaTimeSeconds: Float,
                                 airResistance: Float = AirResistance): List[PhysicsParticle] = {

    var resultList: List[PhysicsParticle] = Nil
    val initParticle = currentParticles.head.copy(
      position = particleUpdateParameter.translation
    )

    val totalRadian = Radian.degreesToRadian(particleUpdateParameter.angle)
    val currentGravity = Radian.radianToDirection(totalRadian).normalize()
    var previousParticle = initParticle

    resultList ::= initParticle

    for (currentParticle <- currentParticles.drop(1)) {
      val initForce = (currentGravity * currentParticle.acceleration) + windDirection
      val lastPosition = currentParticle.position
      val delay = currentParticle.delay * deltaTimeSeconds * 30.0f
      val newDirection = calculateNewDirection(currentParticle, previousParticle, currentGravity, initForce, delay, airResistance)
      val finalPosition = calculateFinalPosition(previousParticle, currentParticle, newDirection)

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

  private def calculateFinalPosition(previousParticle: PhysicsParticle, currentParticle: PhysicsParticle, newDirection: EuclideanVector): EuclideanVector = {
    val thresholdValue = MovementThreshold * normalizationPosition.max
    val finalPosition = previousParticle.position + (newDirection * currentParticle.radius)

    if (Math.abs(finalPosition.x) < thresholdValue) {
      finalPosition.copy(x = 0.0f)
    } else {
      finalPosition
    }
  }

  private def calculateNewDirection(currentParticle: PhysicsParticle, previousParticle: PhysicsParticle,
                                    currentGravity: EuclideanVector, initForce: EuclideanVector, delay: Float, airResistance: Float): EuclideanVector = {
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

}
