package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, ParameterValueUpdate}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsInput, CubismPhysicsOutput, CubismPhysicsRig, CubismPhysicsSubRig}
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.porting.live2d.physics.CubismPhysics.{Options, updateParticles}

import scala.util.control.Breaks

object CubismPhysics {

  def updateParticles(strand: Array[CubismPhysicsParticle], strandCount: Int, totalTranslation: EuclideanVector,
                      totalAngle: MutableData[Float], windDirection: EuclideanVector,
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

    totalRadian = Radian.degreesToRadian(totalAngle.data)
    currentGravity = Radian.radianToDirection(totalRadian).normalize()

    for (i <- 1 until strandCount) {
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
    var totalAngle: MutableData[Float] = MutableData(0.0f)
    var weight: Float = 0.0f
    var radAngle: Float = 0.0f
    var outputValue: Float = 0.0f
    var totalTranslation: EuclideanVector = EuclideanVector()
    var particleIndex: Int = 0
    var currentSetting: CubismPhysicsSubRig = null
    var currentInput: Array[CubismPhysicsInput] = null
    var currentOutput: Array[CubismPhysicsOutput] = null
    var currentParticles: Array[CubismPhysicsParticle] = null
    var operations: List[EffectOperation] = Nil

    for (settingIndex <- 0 until physicsRig.subRigCount) {
      totalAngle = MutableData(0.0f)
      totalTranslation = EuclideanVector(0.0f, 0.0f)
      currentSetting = physicsRig.settings(settingIndex)
      currentInput = physicsRig.inputs.drop(currentSetting.baseInputIndex)
      currentOutput = physicsRig.outputs.drop(currentSetting.baseOutputIndex)
      currentParticles = physicsRig.particles.drop(currentSetting.baseParticleIndex)
      // Load input parameters.
      for (i <- 0 until currentSetting.inputCount) {
        weight = currentInput(i).weight / MaximumWeight

        val newTotalTranslation = currentInput(i).getNormalizedParameterValue(
          totalTranslation,
          totalAngle,
          model.parameters(currentInput(i).source.id).current,
          model.parameters(currentInput(i).source.id).min,
          model.parameters(currentInput(i).source.id).max,
          model.parameters(currentInput(i).source.id).default,
          currentSetting.normalizationPosition,
          currentSetting.normalizationAngle,
          currentInput(i).isReflect,
          weight
        )
        totalTranslation = newTotalTranslation
      }
      radAngle = Radian.degreesToRadian(-totalAngle.data)
      totalTranslation = EuclideanVector(
        x = totalTranslation.x * Math.cos(radAngle).toFloat - totalTranslation.y * Math.sin(radAngle).toFloat,
        y = totalTranslation.x * Math.sin(radAngle).toFloat + totalTranslation.y * Math.cos(radAngle).toFloat
      )

      // Calculate particles position.
      updateParticles(
        currentParticles,
        currentSetting.particleCount,
        totalTranslation,
        totalAngle,
        options.Wind,
        MovementThreshold * currentSetting.normalizationPosition.maximum,
        deltaTimeSeconds,
        AirResistance
      )
      // Update output parameters.
      val loop = new Breaks
      loop.breakable {
        for (i <- 0 until currentSetting.outputCount) {
          particleIndex = currentOutput(i).vertexIndex

          if (particleIndex < 1 || particleIndex >= currentSetting.particleCount) {
            loop.break()
          }

          val translation = EuclideanVector(
            x = currentParticles(particleIndex).position.x - currentParticles(particleIndex - 1).position.x,
            y = currentParticles(particleIndex).position.y - currentParticles(particleIndex - 1).position.y
          )

          outputValue = currentOutput(i).valueGetter(
            translation,
            currentParticles,
            particleIndex,
            currentOutput(i).isReflect,
            options.Gravity
          )

          operations ::= updateOutputParameterValue(
            currentOutput(i).destination.id,
            model.parameters(currentOutput(i).destination.id),
            model.parameters(currentOutput(i).destination.id).current,
            model.parameters(currentOutput(i).destination.id).min,
            model.parameters(currentOutput(i).destination.id).max,
            outputValue,
            currentOutput(i)
          )
        }
      }

    }
    operations.reverse
  }
}
