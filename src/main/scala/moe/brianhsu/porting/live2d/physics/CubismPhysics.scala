package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsType.{Angle, X, Y}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsInput, CubismPhysicsNormalization, CubismPhysicsOutput, CubismPhysicsParameter, CubismPhysicsSubRig, TargetType}
import moe.brianhsu.porting.live2d.framework.math.MutableData
import moe.brianhsu.porting.live2d.physics.CubismPhysics.updateParticles

import scala.util.control.Breaks

object CubismPhysics {

  def create(settings: PhysicsSetting): CubismPhysics = {
    val ret = new CubismPhysics
    ret.parse(settings)
    ret
  }

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

    strand(0).Position = totalTranslation

    totalRadian = Radian.degreesToRadian(totalAngle.data)
    currentGravity = Radian.radianToDirection(totalRadian).normalize()

    for (i <- 1 until strandCount) {
      strand(i).Force = (currentGravity * strand(i).Acceleration) + windDirection

      strand(i).LastPosition = strand(i).Position

      delay = strand(i).Delay * deltaTimeSeconds * 30.0f

      direction = EuclideanVector(
        x = strand(i).Position.x - strand(i - 1).Position.x,
        y = strand(i).Position.y - strand(i - 1).Position.y
      )

      radian = Radian.directionToRadian(strand(i).LastGravity, currentGravity) / airResistance;

      direction = EuclideanVector(
        x = ((Math.cos(radian).toFloat * direction.x) - (direction.y * Math.sin(radian).toFloat)),
        y = ((Math.sin(radian).toFloat * direction.x) + (direction.y * Math.cos(radian).toFloat))
      )

      strand(i).Position = strand(i - 1).Position + direction;

      velocity = EuclideanVector(
        x = strand(i).Velocity.x * delay,
        y = strand(i).Velocity.y * delay
      )
      force = strand(i).Force * delay * delay

      strand(i).Position = strand(i).Position + velocity + force

      newDirection = (strand(i).Position - strand(i - 1).Position).normalize()

      strand(i).Position = strand(i - 1).Position + (newDirection * strand(i).Radius)

      if (Math.abs(strand(i).Position.x) < thresholdValue) {
        strand(i).Position = strand(i).Position.copy(x = 0.0f)
      }

      if (delay != 0.0f) {
        strand(i).Velocity = EuclideanVector(
          x = strand(i).Position.x - strand(i).LastPosition.x,
          y = strand(i).Position.y - strand(i).LastPosition.y
        )
        strand(i).Velocity /= delay
        strand(i).Velocity *= strand(i).Mobility
      }

      strand(i).Force = EuclideanVector(0.0f, 0.0f)
      strand(i).LastGravity = currentGravity
    }

  }

}
class CubismPhysics {
  val MaximumWeight = 100.0f
  val MovementThreshold = 0.001f
  val AirResistance = 5.0f

  case class Options(
    var Gravity: EuclideanVector = EuclideanVector(),          ///< 重力方向
    var Wind: EuclideanVector = EuclideanVector()             ///< 風の方向
  )

  var _physicsRig: CubismPhysicsRig = null          ///< 物理演算のデータ
  var _options: Options = Options(
    EuclideanVector(0.0f, -1.0f),
    EuclideanVector(10.0f, 10.0f)
  )  ///< オプション

  def parse(json: PhysicsSetting): Unit = {
    _physicsRig = new CubismPhysicsRig
    _physicsRig.Gravity = EuclideanVector(
      json.meta.effectiveForces.gravity.x,
      json.meta.effectiveForces.gravity.y
    )
    _physicsRig.Wind = EuclideanVector(
      json.meta.effectiveForces.wind.x,
      json.meta.effectiveForces.wind.y
    )
    _physicsRig.SubRigCount = json.physicsSettings.size
    _physicsRig.Settings = Array.fill(_physicsRig.SubRigCount)(null)
    _physicsRig.Inputs = Array.fill(json.meta.totalInputCount)(null)
    _physicsRig.Outputs = Array.fill(json.meta.totalOutputCount)(null)
    _physicsRig.Particles = Array.fill(json.meta.vertexCount)(new CubismPhysicsParticle)

    var inputIndex: Int = 0
    var outputIndex: Int = 0
    var particleIndex: Int = 0

    for (i <- _physicsRig.Settings.indices) {
      _physicsRig.Settings(i) = new CubismPhysicsSubRig(
        json.physicsSettings(i).input.size,
        json.physicsSettings(i).output.size,
        json.physicsSettings(i).vertices.size,
        inputIndex, outputIndex, particleIndex,
        CubismPhysicsNormalization(
          json.physicsSettings(i).normalization.position.minimum,
          json.physicsSettings(i).normalization.position.maximum,
          json.physicsSettings(i).normalization.position.default
        ),
        CubismPhysicsNormalization(
          json.physicsSettings(i).normalization.angle.minimum,
          json.physicsSettings(i).normalization.angle.maximum,
          json.physicsSettings(i).normalization.angle.default
        )
      )

      // Input
      for (j <- json.physicsSettings(i).input.indices) {

        val (inputType, normalizeFunction) = json.physicsSettings(i).input(j).`type` match {
          case "X" => (X, GetInputTranslationXFromNormalizedParameterValue)
          case "Y" => (Y, GetInputTranslationYFromNormalizedParameterValue)
          case "Angle" => (Angle, GetInputAngleFromNormalizedParameterValue)
          case _ => throw new UnsupportedOperationException("Unsupported input type")
        }

        _physicsRig.Inputs(inputIndex + j) = CubismPhysicsInput(
          CubismPhysicsParameter(json.physicsSettings(i).input(j).source.id, TargetType.Parameter),
          inputType,
          json.physicsSettings(i).input(j).weight,
          json.physicsSettings(i).input(j).reflect,
          normalizeFunction
        )
      }
      inputIndex += json.physicsSettings(i).input.size

      // Output

      for (j <- json.physicsSettings(i).output.indices) {

        val (outputType, translationFunction, scaleFunction) = json.physicsSettings(i).output(j).`type` match {
          case "X" => (X, GetOutputTranslationX, GetOutputScaleTranslationX)
          case "Y" => (Y, GetOutputTranslationY, GetOutputScaleTranslationY)
          case "Angle" => (Angle, GetOutputAngle, GetOutputScaleAngle)
          case _ => throw new UnsupportedOperationException("Unsupported output type")
        }

        _physicsRig.Outputs(outputIndex + j) = new CubismPhysicsOutput(
          CubismPhysicsParameter(
            json.physicsSettings(i).output(j).destination.id,
            TargetType.Parameter
          ),
          json.physicsSettings(i).output(j).vertexIndex,
          json.physicsSettings(i).output(j).scale,
          json.physicsSettings(i).output(j).weight,
          outputType,
          json.physicsSettings(i).output(j).reflect,
          translationFunction,
          scaleFunction
        )
      }
      outputIndex += json.physicsSettings(i).output.size

      // Particle
      for (j <- json.physicsSettings(i).vertices.indices) {
        _physicsRig.Particles(particleIndex + j).Mobility = json.physicsSettings(i).vertices(j).mobility
        _physicsRig.Particles(particleIndex + j).Delay = json.physicsSettings(i).vertices(j).delay
        _physicsRig.Particles(particleIndex + j).Acceleration = json.physicsSettings(i).vertices(j).acceleration
        _physicsRig.Particles(particleIndex + j).Radius = json.physicsSettings(i).vertices(j).radius
        _physicsRig.Particles(particleIndex + j).Position = EuclideanVector(
          json.physicsSettings(i).vertices(j).position.x,
          json.physicsSettings(i).vertices(j).position.y
        )
      }

      particleIndex += json.physicsSettings(i).vertices.size
    }
    initialize()
  }

  def initialize(): Unit = {
    var radius: EuclideanVector = EuclideanVector()

    for (settingIndex <- 0 until _physicsRig.SubRigCount) {
      val currentSetting = _physicsRig.Settings(settingIndex)
      val strand = _physicsRig.Particles.drop(currentSetting.baseParticleIndex)

      // Initialize the top of particle.
      strand(0).InitialPosition = EuclideanVector(0.0f, 0.0f)
      strand(0).LastPosition = strand(0).InitialPosition
      strand(0).LastGravity = EuclideanVector(0.0f, 1.0f)
      strand(0).Velocity = EuclideanVector(0.0f, 0.0f)
      strand(0).Force = EuclideanVector(0.0f, 0.0f)

      // Initialize paritcles.
      for (i <- 1 until currentSetting.particleCount) {
        radius = EuclideanVector(0.0f, strand(i).Radius)
        strand(i).InitialPosition = strand(i - 1).InitialPosition + radius
        strand(i).Position = strand(i).InitialPosition
        strand(i).LastPosition = strand(i).InitialPosition
        strand(i).LastGravity = EuclideanVector(0.0f, 1.0f)
        strand(i).Velocity = EuclideanVector(0.0f, 0.0f)
        strand(i).Force = EuclideanVector(0.0f, 0.0f)
      }
    }

  }


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

    weight = (output.weight / MaximumWeight)

    if (weight >= 1.0f) {
      //*parameterValue = value;
      parameter.update(value)
    } else {
      value = (parameterCurrent * (1.0f - weight)) + (value * weight)
      //*parameterValue = value;
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

    for (settingIndex <- 0 until _physicsRig.SubRigCount) {
      totalAngle = MutableData(0.0f)
      totalTranslation = EuclideanVector(0.0f, 0.0f)
      currentSetting = _physicsRig.Settings(settingIndex)
      currentInput = _physicsRig.Inputs.drop(currentSetting.baseInputIndex)
      currentOutput = _physicsRig.Outputs.drop(currentSetting.baseOutputIndex)
      currentParticles = _physicsRig.Particles.drop(currentSetting.baseParticleIndex)
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
      radAngle = Radian.degreesToRadian(-totalAngle.data);
      totalTranslation = EuclideanVector(
        x = (totalTranslation.x * Math.cos(radAngle).toFloat - totalTranslation.y * Math.sin(radAngle).toFloat),
        y = (totalTranslation.x * Math.sin(radAngle).toFloat + totalTranslation.y * Math.cos(radAngle).toFloat)
      )

      // Calculate particles position.
      updateParticles(
        currentParticles,
        currentSetting.particleCount,
        totalTranslation,
        totalAngle,
        _options.Wind,
        MovementThreshold * currentSetting.normalizationPosition.maximum,
        deltaTimeSeconds,
        AirResistance
      )
      // Update output parameters.
      val loop = new Breaks
      loop.breakable {
        for (i <- 0 until currentSetting.outputCount) {
          particleIndex = currentOutput(i).vertexIndex;

          if (particleIndex < 1 || particleIndex >= currentSetting.particleCount) {
            loop.break()
          }

          val translation = EuclideanVector(
            x = currentParticles(particleIndex).Position.x - currentParticles(particleIndex - 1).Position.x,
            y = currentParticles(particleIndex).Position.y - currentParticles(particleIndex - 1).Position.y
          )

          outputValue = currentOutput(i).valueGetter(
            translation,
            currentParticles,
            particleIndex,
            currentOutput(i).isReflect,
            _options.Gravity
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
