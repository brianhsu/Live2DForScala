package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.{EffectOperation, FallbackParameterValueAdd, FallbackParameterValueUpdate, ParameterValueAdd, ParameterValueMultiply, ParameterValueUpdate, PartOpacityUpdate}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting
import moe.brianhsu.live2d.enitiy.model.{Live2DModel, Parameter}
import moe.brianhsu.porting.live2d.framework.math.{CubismMath, CubismVector2, MutableData}
import moe.brianhsu.porting.live2d.physics.CubismPhysics.UpdateParticles
import moe.brianhsu.porting.live2d.physics.CubismPhysicsSource.{CubismPhysicsSource_Angle, CubismPhysicsSource_X, CubismPhysicsSource_Y}
import moe.brianhsu.porting.live2d.physics.CubismPhysicsTargetType.CubismPhysicsTargetType_Parameter

import scala.util.control.Breaks

object CubismPhysics {
  def Create(settings: PhysicsSetting): CubismPhysics = {
    val ret = new CubismPhysics
    ret.Parse(settings)
    ret._physicsRig.Gravity.Y = 0
    ret
  }
  def UpdateParticles(strand: Array[CubismPhysicsParticle], strandCount: Int, totalTranslation: CubismVector2,
                      totalAngle: MutableData[Float], windDirection: CubismVector2,
                      thresholdValue: Float, deltaTimeSeconds: Float,
                      airResistance: Float): Unit = {

    var totalRadian: Float = 0.0f
    var delay: Float = 0.0f
    var radian: Float = 0.0f
    var currentGravity: CubismVector2 = CubismVector2()
    val direction: CubismVector2 = CubismVector2()
    val velocity: CubismVector2 = CubismVector2()
    var force: CubismVector2 = CubismVector2()
    var newDirection: CubismVector2 = CubismVector2()

    strand(0).Position = totalTranslation

    totalRadian = CubismMath.DegreesToRadian(totalAngle.data)
    currentGravity = CubismMath.RadianToDirection(totalRadian)
    currentGravity.normalize()

    for (i <- 1 until strandCount) {
      strand(i).Force = (currentGravity * strand(i).Acceleration) + windDirection

      strand(i).LastPosition = strand(i).Position

      delay = strand(i).Delay * deltaTimeSeconds * 30.0f

      direction.X = strand(i).Position.X - strand(i - 1).Position.X
      direction.Y = strand(i).Position.Y - strand(i - 1).Position.Y

      radian = CubismMath.DirectionToRadian(strand(i).LastGravity, currentGravity) / airResistance;

      direction.X = ((Math.cos(radian).toFloat * direction.X) - (direction.Y * Math.sin(radian).toFloat))
      direction.Y = ((Math.sin(radian).toFloat * direction.X) + (direction.Y * Math.cos(radian).toFloat))

      strand(i).Position = strand(i - 1).Position + direction;

      velocity.X = strand(i).Velocity.X * delay
      velocity.Y = strand(i).Velocity.Y * delay
      force = strand(i).Force * delay * delay

      strand(i).Position = strand(i).Position + velocity + force

      newDirection = strand(i).Position - strand(i - 1).Position

      newDirection.normalize()

      strand(i).Position = strand(i - 1).Position + (newDirection * strand(i).Radius)

      if (Math.abs(strand(i).Position.X) < thresholdValue) {
        strand(i).Position.X = 0.0f;
      }

      if (delay != 0.0f) {
        strand(i).Velocity.X = strand(i).Position.X - strand(i).LastPosition.X
        strand(i).Velocity.Y = strand(i).Position.Y - strand(i).LastPosition.Y
        strand(i).Velocity /= delay
        strand(i).Velocity *= strand(i).Mobility
      }

      strand(i).Force = CubismVector2(0.0f, 0.0f)
      strand(i).LastGravity = currentGravity
    }

  }

}
class CubismPhysics {
  val MaximumWeight = 100.0f
  val MovementThreshold = 0.001f
  val AirResistance = 5.0f

  case class Options(
    var Gravity: CubismVector2 = CubismVector2(),          ///< 重力方向
    var Wind: CubismVector2 = CubismVector2()             ///< 風の方向
  )

  var _physicsRig: CubismPhysicsRig = null          ///< 物理演算のデータ
  var _options: Options = Options(
    CubismVector2(0.0f, -1.0f),
    CubismVector2(10.0f, 10.0f)
  )  ///< オプション

  def Parse(json: PhysicsSetting): Unit = {
    _physicsRig = new CubismPhysicsRig
    _physicsRig.Gravity = CubismVector2(
      json.meta.effectiveForces.gravity.x,
      json.meta.effectiveForces.gravity.y
    )
    _physicsRig.Wind = CubismVector2(
      json.meta.effectiveForces.wind.x,
      json.meta.effectiveForces.wind.y
    )
    _physicsRig.SubRigCount = json.physicsSettings.size
    _physicsRig.Settings = Array.fill(_physicsRig.SubRigCount)(new CubismPhysicsSubRig)
    _physicsRig.Inputs = Array.fill(json.meta.totalInputCount)(new CubismPhysicsInput)
    _physicsRig.Outputs = Array.fill(json.meta.totalOutputCount)(new CubismPhysicsOutput)
    _physicsRig.Particles = Array.fill(json.meta.vertexCount)(new CubismPhysicsParticle)

    var inputIndex: Int = 0
    var outputIndex: Int = 0
    var particleIndex: Int = 0

    for (i <- 0 until _physicsRig.Settings.length) {
      _physicsRig.Settings(i).NormalizationPosition.Minimum = json.physicsSettings(i).normalization.position.minimum
      _physicsRig.Settings(i).NormalizationPosition.Maximum = json.physicsSettings(i).normalization.position.maximum
      _physicsRig.Settings(i).NormalizationPosition.Default = json.physicsSettings(i).normalization.position.default

      _physicsRig.Settings(i).NormalizationAngle.Minimum = json.physicsSettings(i).normalization.angle.minimum
      _physicsRig.Settings(i).NormalizationAngle.Maximum = json.physicsSettings(i).normalization.angle.maximum
      _physicsRig.Settings(i).NormalizationAngle.Default = json.physicsSettings(i).normalization.angle.default

      // Input
      _physicsRig.Settings(i).InputCount = json.physicsSettings(i).input.size
      _physicsRig.Settings(i).BaseInputIndex = inputIndex
      for (j <- 0 until _physicsRig.Settings(i).InputCount) {
        _physicsRig.Inputs(inputIndex + j).SourceParameterId = json.physicsSettings(i).input(j).source.id
        _physicsRig.Inputs(inputIndex + j).SourceParameterIndex = -1
        _physicsRig.Inputs(inputIndex + j).Weight = json.physicsSettings(i).input(j).weight
        _physicsRig.Inputs(inputIndex + j).Reflect = json.physicsSettings(i).input(j).reflect
        if (json.physicsSettings(i).input(j).`type` == "X") {
          _physicsRig.Inputs(inputIndex + j).Type = CubismPhysicsSource_X
          _physicsRig.Inputs(inputIndex + j).GetNormalizedParameterValue = GetInputTranslationXFromNormalizedParameterValue
        } else if (json.physicsSettings(i).input(j).`type` == "Y") {
          _physicsRig.Inputs(inputIndex + j).Type = CubismPhysicsSource_Y
          _physicsRig.Inputs(inputIndex + j).GetNormalizedParameterValue = GetInputTranslationYFromNormalizedParameterValue
        } else if (json.physicsSettings(i).input(j).`type` == "Angle") {
          _physicsRig.Inputs(inputIndex + j).Type = CubismPhysicsSource_Angle
          _physicsRig.Inputs(inputIndex + j).GetNormalizedParameterValue = GetInputAngleFromNormalizedParameterValue
        }

        _physicsRig.Inputs(inputIndex + j).Source.TargetType = CubismPhysicsTargetType_Parameter
        _physicsRig.Inputs(inputIndex + j).Source.Id = json.physicsSettings(i).input(j).source.id

      }
      inputIndex += _physicsRig.Settings(i).InputCount;

      // Output
      _physicsRig.Settings(i).OutputCount = json.physicsSettings(i).output.size
      _physicsRig.Settings(i).BaseOutputIndex = outputIndex
      for (j <- 0 until _physicsRig.Settings(i).OutputCount) {
        _physicsRig.Outputs(outputIndex + j).DestinationParameterId = json.physicsSettings(i).output(j).destination.id
        _physicsRig.Outputs(outputIndex + j).DestinationParameterIndex = -1
        _physicsRig.Outputs(outputIndex + j).VertexIndex = json.physicsSettings(i).output(j).vertexIndex
        _physicsRig.Outputs(outputIndex + j).AngleScale = json.physicsSettings(i).output(j).scale
        _physicsRig.Outputs(outputIndex + j).Weight = json.physicsSettings(i).output(j).weight
        _physicsRig.Outputs(outputIndex + j).Destination.TargetType = CubismPhysicsTargetType_Parameter
        _physicsRig.Outputs(outputIndex + j).Destination.Id = json.physicsSettings(i).output(j).destination.id
        if (json.physicsSettings(i).output(j).`type` == "X") {
          _physicsRig.Outputs(outputIndex + j).Type = CubismPhysicsSource_X
          _physicsRig.Outputs(outputIndex + j).GetValue = GetOutputTranslationX
          _physicsRig.Outputs(outputIndex + j).GetScale = GetOutputScaleTranslationX
        } else if (json.physicsSettings(i).output(j).`type` == "Y") {
          _physicsRig.Outputs(outputIndex + j).Type = CubismPhysicsSource_Y
          _physicsRig.Outputs(outputIndex + j).GetValue = GetOutputTranslationY
          _physicsRig.Outputs(outputIndex + j).GetScale = GetOutputScaleTranslationY
        } else if (json.physicsSettings(i).output(j).`type` == "Angle") {
          _physicsRig.Outputs(outputIndex + j).Type = CubismPhysicsSource_Angle
          _physicsRig.Outputs(outputIndex + j).GetValue = GetOutputAngle
          _physicsRig.Outputs(outputIndex + j).GetScale = GetOutputScaleAngle
        }

        _physicsRig.Outputs(outputIndex + j).Reflect = json.physicsSettings(i).output(j).reflect
      }
      outputIndex += _physicsRig.Settings(i).OutputCount

      // Particle
      _physicsRig.Settings(i).ParticleCount = json.physicsSettings(i).vertices.size
      _physicsRig.Settings(i).BaseParticleIndex = particleIndex
      for (j <- 0 until _physicsRig.Settings(i).ParticleCount) {
        _physicsRig.Particles(particleIndex + j).Mobility = json.physicsSettings(i).vertices(j).mobility
        _physicsRig.Particles(particleIndex + j).Delay = json.physicsSettings(i).vertices(j).delay
        _physicsRig.Particles(particleIndex + j).Acceleration = json.physicsSettings(i).vertices(j).acceleration
        _physicsRig.Particles(particleIndex + j).Radius = json.physicsSettings(i).vertices(j).radius
        _physicsRig.Particles(particleIndex + j).Position = CubismVector2(
          json.physicsSettings(i).vertices(j).position.x,
          json.physicsSettings(i).vertices(j).position.y
        )
      }

      particleIndex += _physicsRig.Settings(i).ParticleCount
    }
    Initialize()
  }

  def Initialize(): Unit = {
    var radius: CubismVector2 = CubismVector2()

    for (settingIndex <- 0 until _physicsRig.SubRigCount) {
      val currentSetting = _physicsRig.Settings(settingIndex)
      val strand = _physicsRig.Particles.drop(currentSetting.BaseParticleIndex)

      // Initialize the top of particle.
      strand(0).InitialPosition = CubismVector2(0.0f, 0.0f)
      strand(0).LastPosition = strand(0).InitialPosition
      strand(0).LastGravity = CubismVector2(0.0f, -1.0f)
      strand(0).LastGravity.Y *= -1.0f
      strand(0).Velocity = CubismVector2(0.0f, 0.0f)
      strand(0).Force = CubismVector2(0.0f, 0.0f)

      // Initialize paritcles.
      for (i <- 1 until currentSetting.ParticleCount) {
        radius = CubismVector2(0.0f, 0.0f)
        radius.Y = strand(i).Radius
        strand(i).InitialPosition = strand(i - 1).InitialPosition + radius
        strand(i).Position = strand(i).InitialPosition
        strand(i).LastPosition = strand(i).InitialPosition
        strand(i).LastGravity = CubismVector2(0.0f, -1.0f)
        strand(i).LastGravity.Y *= -1.0f
        strand(i).Velocity = CubismVector2(0.0f, 0.0f)
        strand(i).Force = CubismVector2(0.0f, 0.0f)
      }
    }

  }


  def UpdateOutputParameterValue(id: String, parameter: Parameter, parameterCurrent: Float, parameterValueMinimum: Float, parameterValueMaximum: Float,
                                 translation: Float, output: CubismPhysicsOutput): EffectOperation = {

    var outputScale: Float = 0.0f
    var value: Float = 0.0f
    var weight: Float = 0.0f

    outputScale = output.GetScale(output.TranslationScale, output.AngleScale)

    value = translation * outputScale

    if (value < parameterValueMinimum) {
      if (value < output.ValueBelowMinimum) {
        output.ValueBelowMinimum = value
      }

      value = parameterValueMinimum
    } else if (value > parameterValueMaximum) {
      if (value > output.ValueExceededMaximum) {
        output.ValueExceededMaximum = value
      }

      value = parameterValueMaximum
    }

    weight = (output.Weight / MaximumWeight)

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

  def Evaluate(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeSeconds: Float): List[EffectOperation] = {
    var totalAngle: MutableData[Float] = MutableData(0.0f)
    var weight: Float = 0.0f
    var radAngle: Float = 0.0f
    var outputValue: Float = 0.0f
    var totalTranslation: CubismVector2 = CubismVector2()
    var i: Int = 0
    var settingIndex: Int = 0
    var particleIndex: Int = 0
    var currentSetting: CubismPhysicsSubRig = null
    var currentInput: Array[CubismPhysicsInput] = null
    var currentOutput: Array[CubismPhysicsOutput] = null
    var currentParticles: Array[CubismPhysicsParticle] = null
    var operations: List[EffectOperation] = Nil

    for (settingIndex <- 0 until _physicsRig.SubRigCount) {
      totalAngle = MutableData(0.0f)
      totalTranslation.X = 0.0f
      totalTranslation.Y = 0.0f
      currentSetting = _physicsRig.Settings(settingIndex)
      currentInput = _physicsRig.Inputs.drop(currentSetting.BaseInputIndex)
      currentOutput = _physicsRig.Outputs.drop(currentSetting.BaseOutputIndex)
      currentParticles = _physicsRig.Particles.drop(currentSetting.BaseParticleIndex)
      // Load input parameters.
      for (i <- 0 until currentSetting.InputCount) {
        weight = currentInput(i).Weight / MaximumWeight

        currentInput(i).GetNormalizedParameterValue(
          totalTranslation,
          totalAngle,
          model.parameters(currentInput(i).SourceParameterId).current,
          model.parameters(currentInput(i).SourceParameterId).min,
          model.parameters(currentInput(i).SourceParameterId).max,
          model.parameters(currentInput(i).SourceParameterId).default,
          currentSetting.NormalizationPosition,
          currentSetting.NormalizationAngle,
          currentInput(i).Reflect,
          weight
        )
      }
      radAngle = CubismMath.DegreesToRadian(-totalAngle.data);
      totalTranslation.X = (totalTranslation.X * Math.cos(radAngle).toFloat - totalTranslation.Y * Math.sin(radAngle).toFloat)
      totalTranslation.Y = (totalTranslation.X * Math.sin(radAngle).toFloat + totalTranslation.Y * Math.cos(radAngle).toFloat)

      // Calculate particles position.
      UpdateParticles(
        currentParticles,
        currentSetting.ParticleCount,
        totalTranslation,
        totalAngle,
        _options.Wind,
        MovementThreshold * currentSetting.NormalizationPosition.Maximum,
        deltaTimeSeconds,
        AirResistance
      )
      // Update output parameters.
      val loop = new Breaks
      loop.breakable {
        for (i <- 0 until currentSetting.OutputCount) {
          particleIndex = currentOutput(i).VertexIndex;

          if (particleIndex < 1 || particleIndex >= currentSetting.ParticleCount) {
            loop.break()
          }

          val translation = CubismVector2()
          translation.X = currentParticles(particleIndex).Position.X - currentParticles(particleIndex - 1).Position.X
          translation.Y = currentParticles(particleIndex).Position.Y - currentParticles(particleIndex - 1).Position.Y

          outputValue = currentOutput(i).GetValue(
            translation,
            currentParticles,
            particleIndex,
            currentOutput(i).Reflect,
            _options.Gravity
          )

          operations ::= UpdateOutputParameterValue(
            currentOutput(i).DestinationParameterId,
            model.parameters(currentOutput(i).DestinationParameterId),
            model.parameters(currentOutput(i).DestinationParameterId).current,
            model.parameters(currentOutput(i).DestinationParameterId).min,
            model.parameters(currentOutput(i).DestinationParameterId).max,
            outputValue,
            currentOutput(i)
          )
        }
      }

    }
    operations.reverse
  }
}
