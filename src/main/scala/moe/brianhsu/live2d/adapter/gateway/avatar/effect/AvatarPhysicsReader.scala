package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import moe.brianhsu.live2d.boundary.gateway.avatar.physics.PhysicsReader
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics._
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Physics
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PhysicsSetting.{Input, Normalization, Output, Vertex}
import moe.brianhsu.live2d.enitiy.math.EuclideanVector

class AvatarPhysicsReader(avatarSettings: Settings) extends PhysicsReader {
  /**
   * Load and create Pose effect
   *
   * @return `None` if there is no physics setting, or `Some[CubismPhysics]` contains
   *         the [[moe.brianhsu.live2d.enitiy.avatar.effect.impl.Pose]] effect.
   */
  override def loadPhysics: Option[Physics] = {
    avatarSettings.physics.map(x => createCubismPhysics(x))
  }

  private def createCubismPhysics(physicsSetting: PhysicsSetting): Physics = {
    val effectiveForces = physicsSetting.meta.effectiveForces
    new Physics(
      createPhysicData(physicsSetting),
      gravityDirection = EuclideanVector(effectiveForces.gravity.x, effectiveForces.gravity.y),
      windDirection = EuclideanVector(effectiveForces.wind.x, effectiveForces.wind.y)
    )
  }

  private def createPhysicData(json: PhysicsSetting): PhysicsData = {

    val effects: List[PhysicsEffect] = json.physicsSettings.map { setting =>
      createPhysicEffect(
        setting.normalization,
        setting.input.map(createInput),
        setting.output.map(createOutput),
        createParticleList(setting.vertices)
      )
    }

    physics.PhysicsData(
      effects,
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

  private def createParticleList(vertexSettings: List[Vertex]): List[PhysicsParticle] = {
    var previousParticle: Option[PhysicsParticle] = None
    var resultList: List[PhysicsParticle] = Nil

    for (vertexSetting <- vertexSettings) {
      val particle = createParticle(previousParticle, vertexSetting)
      resultList ::= particle
      previousParticle = Some(particle)
    }

    resultList.reverse
  }

  private def createParticle(previousParticle: Option[PhysicsParticle],
                             vertexSetting: Vertex): PhysicsParticle = {
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

    physics.PhysicsParticle(
      vertexSetting.mobility, vertexSetting.delay,
      vertexSetting.acceleration, vertexSetting.radius,
      initialPosition, position, lastPosition,
      EuclideanVector(0.0f, 1.0f),
      EuclideanVector(0.0f, 0.0f)
    )
  }

  private def createOutput(outputSetting: Output): PhysicsOutput = {
    val outputType = ParameterType(outputSetting.`type`)
    physics.PhysicsOutput(
      PhysicsParameter(outputSetting.destination.id, TargetType.Parameter),
      outputType,
      outputSetting.vertexIndex,
      translationScale = EuclideanVector(0.0f, 0.0f),
      outputSetting.scale,
      outputSetting.reflect,
      outputSetting.weight
    )
  }

  private def createInput(input: Input): PhysicsInput = {
    PhysicsInput(
      PhysicsParameter(input.source.id, TargetType.Parameter),
      ParameterType(input.`type`),
      input.weight,
      input.reflect
    )
  }

  private def createPhysicEffect(normalization: Normalization,
                           inputsInSetting: List[PhysicsInput],
                           outputsInSetting: List[PhysicsOutput],
                           particleInSetting: List[PhysicsParticle]): PhysicsEffect = {

    physics.PhysicsEffect(
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
