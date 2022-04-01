package moe.brianhsu.live2d.enitiy.avatar.physics

import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsSubRig.{AirResistance, MaximumWeight, MovementThreshold}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object CubismPhysicsSubRig {
  val MaximumWeight = 100.0f
  val MovementThreshold = 0.001f
  val AirResistance = 5.0f
}

case class CubismPhysicsSubRig(
  normalizationPosition: CubismPhysicsNormalization, ///< 正規化された位置
  normalizationAngle: CubismPhysicsNormalization,
  inputs: List[CubismPhysicsInput],
  outputs: List[CubismPhysicsOutput],
  var particles: List[CubismPhysicsParticle]
) {

  def calculateParticleUpdateParameter(model: Live2DModel): ParticleUpdateParameter = {
    var particleUpdateParameter = ParticleUpdateParameter(EuclideanVector(0.0f, 0.0f), 0.0f)

    for (input <- inputs) {
      val weight = input.weight / MaximumWeight

      particleUpdateParameter = input.getNormalizedParameterValue(
        particleUpdateParameter,
        model.parameters(input.source.id).current,
        model.parameters(input.source.id).min,
        model.parameters(input.source.id).max,
        model.parameters(input.source.id).default,
        normalizationPosition,
        normalizationAngle,
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

  def updateParticles(particleUpdateParameter: ParticleUpdateParameter,
                      windDirection: EuclideanVector,
                      deltaTimeSeconds: Float,
                      airResistance: Float = AirResistance): List[CubismPhysicsParticle] = {

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

  private def calculateFinalPosition(previousParticle: CubismPhysicsParticle, currentParticle: CubismPhysicsParticle, newDirection: EuclideanVector): EuclideanVector = {
    val thresholdValue = MovementThreshold * normalizationPosition.maximum
    val finalPosition = previousParticle.position + (newDirection * currentParticle.radius)

    if (Math.abs(finalPosition.x) < thresholdValue) {
      finalPosition.copy(x = 0.0f)
    } else {
      finalPosition
    }
  }

  private def calculateNewDirection(currentParticle: CubismPhysicsParticle, previousParticle: CubismPhysicsParticle,
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
