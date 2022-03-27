package moe.brianhsu.porting.live2d.physics

import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting
import moe.brianhsu.porting.live2d.math.CubismVector

object CubismPhysics {
  def create(json: PhysicsSetting): CubismPhysics = {
    val ret = new CubismPhysics

    val _physicsRig = new CubismPhysicsRig
    _physicsRig.Gravity = CubismVector(json.meta.effectiveForces.gravity.x, json.meta.effectiveForces.gravity.y)
    _physicsRig.Wind = CubismVector(json.meta.effectiveForces.wind.x, json.meta.effectiveForces.wind.y)
    _physicsRig.SubRigCount = json.meta.physicsSettingCount
    _physicsRig.Settings = Array.fill(_physicsRig.SubRigCount)(new CubismPhysicsSubRig)
    _physicsRig.Inputs = Array.fill(json.meta.totalInputCount)(new CubismPhysicsInput)
    _physicsRig.Outputs = Array.fill(json.meta.totalOutputCount)(new CubismPhysicsOutput)
    _physicsRig.Particles = Array.fill(json.meta.vertexCount)(new CubismPhysicsParticle)

    var inputIndex = 0
    var outputIndex = 0
    var particleIndex = 0

    for (i <- 0 until _physicsRig.SubRigCount) {

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
        _physicsRig.Inputs(inputIndex + j).SourceParameterIndex = -1
        _physicsRig.Inputs(inputIndex + j).Weight = json.physicsSettings(i).input(j).weight
        _physicsRig.Inputs(inputIndex + j).Reflect = json.physicsSettings(i).input(j).reflect
        if (json.physicsSettings(i).input(j).`type` == "PhysicsTypeTagX") {
          _physicsRig.Inputs(inputIndex + j).Type = CubismPhysicsSource_X
          _physicsRig.Inputs(inputIndex + j).GetNormalizedParameterValue = GetInputTranslationXFromNormalizedParameterValue
        } else if (json.physicsSettings(i).input(j).`type` == "PhysicsTypeTagY") {
          _physicsRig.Inputs(inputIndex + j).Type = CubismPhysicsSource_Y
          _physicsRig.Inputs(inputIndex + j).GetNormalizedParameterValue = GetInputTranslationYFromNormalizedParameterValue
        } else if (json.physicsSettings(i).input(j).`type` == "PhysicsTypeTagAngle") {
          _physicsRig.Inputs(inputIndex + j).Type = CubismPhysicsSource_Angle
          _physicsRig.Inputs(inputIndex + j).GetNormalizedParameterValue = GetInputAngleFromNormalizedParameterValue
        }
        _physicsRig.Inputs(inputIndex + j).Source.TargetType = CubismPhysicsTargetType_Parameter
        _physicsRig.Inputs(inputIndex + j).Source.Id = json.physicsSettings(i).input(j).source.id
      }
      inputIndex += _physicsRig.Settings(i).InputCount

      // Output
      _physicsRig.Settings(i).OutputCount = json.physicsSettings(i).output.size
      _physicsRig.Settings(i).BaseOutputIndex = outputIndex
      for (j <- 0 until _physicsRig.Settings(i).OutputCount) {
        _physicsRig.Outputs(outputIndex + j).DestinationParameterIndex = -1;
        _physicsRig.Outputs(outputIndex + j).VertexIndex = json.physicsSettings(i).output(j).vertexIndex
        _physicsRig.Outputs(outputIndex + j).AngleScale = json.physicsSettings(i).output(j).scale
        _physicsRig.Outputs(outputIndex + j).Weight = json.physicsSettings(i).output(j).weight
        _physicsRig.Outputs(outputIndex + j).Destination.TargetType = CubismPhysicsTargetType_Parameter
        _physicsRig.Outputs(outputIndex + j).Destination.Id = json.physicsSettings(i).output(j).destination.id
        if (json.physicsSettings(i).output(j).`type` == "PhysicsTypeTagX") {
          _physicsRig.Outputs(outputIndex + j).Type = CubismPhysicsSource_X
          _physicsRig.Outputs(outputIndex + j).GetValue = GetOutputTranslationX
          _physicsRig.Outputs(outputIndex + j).GetScale = GetOutputScaleTranslationX
        } else if (json.physicsSettings(i).output(j).`type` == "PhysicsTypeTagY") {
          _physicsRig.Outputs(outputIndex + j).Type = CubismPhysicsSource_Y
          _physicsRig.Outputs(outputIndex + j).GetValue = GetOutputTranslationY
          _physicsRig.Outputs(outputIndex + j).GetScale = GetOutputScaleTranslationY
        } else if (json.physicsSettings(i).output(j).`type` == "PhysicsTypeTagAngle") {
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
        val p = json.physicsSettings(i).vertices(j).position
        _physicsRig.Particles(particleIndex + j).Position = CubismVector(p.x, p.y)
      }

      particleIndex += _physicsRig.Settings(i).ParticleCount
    }

    ret._physicsRig = _physicsRig
    ret
  }
}
class CubismPhysics {
  var _physicsRig: CubismPhysicsRig = null
  var _options: Options = null



}
