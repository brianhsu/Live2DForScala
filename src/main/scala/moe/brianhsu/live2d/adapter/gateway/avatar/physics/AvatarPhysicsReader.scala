package moe.brianhsu.live2d.adapter.gateway.avatar.physics

import moe.brianhsu.live2d.boundary.gateway.avatar.physics.PhysicsReader
import moe.brianhsu.live2d.enitiy.avatar.physics.data.{ParameterType, PhysicsInput, PhysicsNormalization, PhysicsParameter}
import moe.brianhsu.live2d.enitiy.avatar.physics.{CubismPhysics, CubismPhysicsOutput, CubismPhysicsParticle, CubismPhysicsRig, CubismPhysicsSubRig, TargetType, data}
import moe.brianhsu.live2d.enitiy.avatar.physics.data.ParameterType.{Angle, X, Y}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting.{Input, Normalization, Output, Setting, Vertex}
import moe.brianhsu.live2d.enitiy.math.EuclideanVector

class AvatarPhysicsReader(avatarSettings: Settings) extends PhysicsReader {
  /**
   * Load and create Pose effect
   *
   * @return `None` if there is no physics setting, or `Some[CubismPhysics]` contains
   *         the [[moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose]] effect.
   */
  override def loadPhysics: Option[CubismPhysics] = {
    avatarSettings.physics.map(x => createCubismPhysics(x))
  }

  private def createCubismPhysics(physicsSetting: PhysicsSetting): CubismPhysics = {
    val gravityDirection = EuclideanVector(0.0f, -1.0f)
    val windDirection = EuclideanVector(0.0f, 0.0f)
    new CubismPhysics(createRig(physicsSetting), gravityDirection, windDirection)
  }

  private def createRig(json: PhysicsSetting): CubismPhysicsRig = {

    val settings: List[CubismPhysicsSubRig] = json.physicsSettings.map { setting =>
      createSubRig(
        setting.normalization,
        setting.input.map(createInput),
        setting.output.map(createOutput),
        createParticleList(setting.vertices)
      )
    }

    CubismPhysicsRig(
      settings,
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
      EuclideanVector(0.0f, 0.0f)
    )
  }

  private def createOutput(outputSetting: Output): CubismPhysicsOutput = {
    val outputType = ParameterType(outputSetting.`type`)
    CubismPhysicsOutput(
      PhysicsParameter(
        outputSetting.destination.id,
        TargetType.Parameter
      ),
      outputSetting.vertexIndex,
      outputSetting.scale,
      outputSetting.weight,
      outputType,
      outputSetting.reflect,
      translationScale = EuclideanVector(0.0f, 0.0f)
    )
  }

  private def createInput(input: Input): PhysicsInput = {
    PhysicsInput(
      data.PhysicsParameter(input.source.id, TargetType.Parameter),
      ParameterType(input.`type`),
      input.weight,
      input.reflect
    )
  }

  private def createSubRig(normalization: Normalization,
                           inputsInSetting: List[PhysicsInput],
                           outputsInSetting: List[CubismPhysicsOutput],
                           particleInSetting: List[CubismPhysicsParticle]): CubismPhysicsSubRig = {

    CubismPhysicsSubRig(
      PhysicsNormalization(
        normalization.position.minimum,
        normalization.position.maximum,
        normalization.position.default
      ),
      PhysicsNormalization(
        normalization.angle.minimum,
        normalization.angle.maximum,
        normalization.angle.default
      ),
      inputsInSetting, outputsInSetting, particleInSetting
    )
  }

}
