package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.avatar.physics.CubismPhysicsType.{Angle, X, Y}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting
import moe.brianhsu.live2d.enitiy.math.{EuclideanVector, Radian}
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysicsInput, CubismPhysicsNormalization, CubismPhysicsOutput, CubismPhysicsParameter, CubismPhysicsRig, CubismPhysicsSubRig, TargetType}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting.{Input, Output, Setting, Vertex}
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

      radian = Radian.directionToRadian(strand(i).lastGravity, currentGravity) / airResistance;

      direction = EuclideanVector(
        x = ((Math.cos(radian).toFloat * direction.x) - (direction.y * Math.sin(radian).toFloat)),
        y = ((Math.sin(radian).toFloat * direction.x) + (direction.y * Math.cos(radian).toFloat))
      )

      strand(i).position = strand(i - 1).position + direction;

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

    var inputIndex: Int = 0
    var outputIndex: Int = 0
    var particleIndex: Int = 0

    var settings: List[CubismPhysicsSubRig] = Nil
    var inputs: List[CubismPhysicsInput] = Nil
    var outputs: List[CubismPhysicsOutput] = Nil
    var particles: List[CubismPhysicsParticle] = Nil

    for (i <- json.physicsSettings.indices) {
      val setting = json.physicsSettings(i)
      settings ::= createSubRig(setting, inputIndex, outputIndex, particleIndex)

      inputs ++= setting.input.map(createInput)
      outputs ++= setting.output.map(createOutput)
      particles ++= createParticleList(setting.vertices)

      inputIndex += setting.input.size
      outputIndex += setting.output.size
      particleIndex += setting.vertices.size
    }
    _physicsRig = new CubismPhysicsRig(
      json.physicsSettings.size,
      settings.reverse.toArray,
      inputs.toArray,
      outputs.toArray,
      particles.toArray,
      EuclideanVector(
        json.meta.effectiveForces.gravity.x,
        json.meta.effectiveForces.gravity.y
      ),
      EuclideanVector(
        json.meta.effectiveForces.wind.x,
        json.meta.effectiveForces.wind.y
      )
    )
  }

  private def createParticleList(vertexSettings: List[Vertex]): List[CubismPhysicsParticle] = {
    var previousParticle: Option[CubismPhysicsParticle] = None
    var resultList: List[CubismPhysicsParticle] = Nil

    for (vertexSetting <- vertexSettings) {
      val particle = createParticle(previousParticle, vertexSetting)
      resultList ::= particle
      previousParticle = Some(particle)
    }

    resultList.reverse
  }

  private def createParticle(previousParticle: Option[CubismPhysicsParticle],
                             vertexSetting: Vertex): CubismPhysicsParticle = {
    val initialPosition = previousParticle match {
      case None => EuclideanVector(0.0f, 0.0f)
      case Some(particle) =>
        val radius = EuclideanVector(0.0f, vertexSetting.radius)
        particle.initialPosition + radius
    }

    val position = if (previousParticle.isEmpty) {
      EuclideanVector(vertexSetting.position.x, vertexSetting.position.y)
    } else {
      initialPosition
    }

    val lastPosition = initialPosition

    CubismPhysicsParticle(
      vertexSetting.mobility, vertexSetting.delay,
      vertexSetting.acceleration, vertexSetting.radius,
      initialPosition, position, lastPosition,
      EuclideanVector(0.0f, 1.0f),
      EuclideanVector(0.0f, 0.0f),
      EuclideanVector(0.0f, 0.0f)
    )
  }

  private def createOutput(outputSetting: Output): CubismPhysicsOutput = {
    val (outputType, translationFunction, scaleFunction) = outputSetting.`type` match {
      case "X" => (X, GetOutputTranslationX, GetOutputScaleTranslationX)
      case "Y" => (Y, GetOutputTranslationY, GetOutputScaleTranslationY)
      case "Angle" => (Angle, GetOutputAngle, GetOutputScaleAngle)
      case _ => throw new UnsupportedOperationException("Unsupported output type")
    }

    CubismPhysicsOutput(
      CubismPhysicsParameter(
        outputSetting.destination.id,
        TargetType.Parameter
      ),
      outputSetting.vertexIndex,
      outputSetting.scale,
      outputSetting.weight,
      outputType,
      outputSetting.reflect,
      translationFunction,
      scaleFunction
    )
  }
  private def createInput(input: Input): CubismPhysicsInput = {
    val (inputType, normalizeFunction) = input.`type` match {
      case "X" => (X, GetInputTranslationXFromNormalizedParameterValue)
      case "Y" => (Y, GetInputTranslationYFromNormalizedParameterValue)
      case "Angle" => (Angle, GetInputAngleFromNormalizedParameterValue)
      case _ => throw new UnsupportedOperationException("Unsupported input type")
    }

    CubismPhysicsInput(
      CubismPhysicsParameter(input.source.id, TargetType.Parameter),
      inputType,
      input.weight,
      input.reflect,
      normalizeFunction
    )
  }

  private def createSubRig(setting: Setting,
                           inputIndex: Int, outputIndex: Int, particleIndex: Int): CubismPhysicsSubRig = {
    CubismPhysicsSubRig(
      setting.input.size,
      setting.output.size,
      setting.vertices.size,
      inputIndex, outputIndex, particleIndex,
      CubismPhysicsNormalization(
        setting.normalization.position.minimum,
        setting.normalization.position.maximum,
        setting.normalization.position.default
      ),
      CubismPhysicsNormalization(
        setting.normalization.angle.minimum,
        setting.normalization.angle.maximum,
        setting.normalization.angle.default
      )
    )
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

    for (settingIndex <- 0 until _physicsRig.subRigCount) {
      totalAngle = MutableData(0.0f)
      totalTranslation = EuclideanVector(0.0f, 0.0f)
      currentSetting = _physicsRig.settings(settingIndex)
      currentInput = _physicsRig.inputs.drop(currentSetting.baseInputIndex)
      currentOutput = _physicsRig.outputs.drop(currentSetting.baseOutputIndex)
      currentParticles = _physicsRig.particles.drop(currentSetting.baseParticleIndex)
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
            x = currentParticles(particleIndex).position.x - currentParticles(particleIndex - 1).position.x,
            y = currentParticles(particleIndex).position.y - currentParticles(particleIndex - 1).position.y
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
