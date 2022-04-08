package moe.brianhsu.live2d.enitiy.avatar.effect.data.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.data
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel}
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Inside}

class PhysicsEffectFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with Inside {
  Feature("Calculate particle update parameter") {
    Scenario("The input is an empty list") {
      Given("a PhysicsEffect that has empty input list")
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 100, 50),
        PhysicsNormalization(0, 100, 50),
        inputs = Nil,
        outputs = Nil,
        initialParticles = Nil
      )

      And("A mocked Live2D Model")
      val model = mock[Live2DModel]

      When("calculate particle update parameter")
      val particleUpdateParameter = physicsEffect.calculateParticleUpdateParameter(model)

      Then("X / Y / Angle should all be zero")
      inside(particleUpdateParameter.translation) { case EuclideanVector(x, y) =>
        x shouldBe 0.0f
        y shouldBe 0.0f
      }
      particleUpdateParameter.angle shouldBe 0.0f
    }

    Scenario("The input list only contains one element and type is angle") {
      Given("a PhysicsEffect that has empty input list")
      val inputList = List(
        PhysicsInput(
          PhysicsParameter("someParameterId", TargetType.Parameter),
          ParameterType.Angle, weight = 1.0f, isReflect = false
        )
      )
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 100, 50),
        PhysicsNormalization(0, 100, 50),
        inputs = inputList,
        outputs = Nil,
        initialParticles = Nil
      )

      And("A mocked Live2D Model with mocked parameter")
      val model = stub[Live2DModel]
      val mockedParameters = Map(
        "someParameterId" -> new JavaVMParameter("someParameterId", min = 5, max = 50, default = 25, value = 10)
      )
      (() => model.parameters).when().returns(mockedParameters)

      When("calculate particle update parameter")
      val particleUpdateParameter = physicsEffect.calculateParticleUpdateParameter(model)

      Then("X / Y should be zero")
      inside(particleUpdateParameter.translation) { case EuclideanVector(x, y) =>
        x shouldBe 0.0f
        y shouldBe 0.0f
      }

      And("the angle should have correct value")
      particleUpdateParameter.angle shouldBe -0.111111104f
    }

    Scenario("The input list only contains one element and type is X") {
      Given("a PhysicsEffect that has empty input list")
      val inputList = List(
        PhysicsInput(
          PhysicsParameter("someParameterId", TargetType.Parameter),
          ParameterType.X, weight = 1.0f, isReflect = false
        )
      )
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 100, 50),
        PhysicsNormalization(0, 100, 50),
        inputs = inputList,
        outputs = Nil,
        initialParticles = Nil
      )

      And("A mocked Live2D Model with mocked parameter")
      val model = stub[Live2DModel]
      val mockedParameters = Map(
        "someParameterId" -> new JavaVMParameter("someParameterId", min = 5, max = 50, default = 25, value = 10)
      )
      (() => model.parameters).when().returns(mockedParameters)

      When("calculate particle update parameter")
      val particleUpdateParameter = physicsEffect.calculateParticleUpdateParameter(model)

      Then("X should have correct value and Y is zero")
      inside(particleUpdateParameter.translation) { case EuclideanVector(x, y) =>
        x shouldBe -0.111111104f
        y shouldBe 0.0f
      }

      And("the angle should be zero")
      particleUpdateParameter.angle shouldBe 0
    }

    Scenario("The input list only contains one element and type is Y") {
      Given("a PhysicsEffect that has empty input list")
      val inputList = List(
        PhysicsInput(
          PhysicsParameter("someParameterId", TargetType.Parameter),
          ParameterType.Y, weight = 1.0f, isReflect = false
        )
      )
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 100, 50),
        PhysicsNormalization(0, 100, 50),
        inputs = inputList,
        outputs = Nil,
        initialParticles = Nil
      )

      And("A mocked Live2D Model with mocked parameter")
      val model = stub[Live2DModel]
      val mockedParameters = Map(
        "someParameterId" -> new JavaVMParameter("someParameterId", min = 5, max = 50, default = 25, value = 10)
      )
      (() => model.parameters).when().returns(mockedParameters)

      When("calculate particle update parameter")
      val particleUpdateParameter = physicsEffect.calculateParticleUpdateParameter(model)

      Then("X should be zero and Y is has correct value")
      inside(particleUpdateParameter.translation) { case EuclideanVector(x, y) =>
        x shouldBe 0.0f
        y shouldBe -0.111111104f
      }

      And("the angle should be zero")
      particleUpdateParameter.angle shouldBe 0
    }

    Scenario("The input list only multiple items") {
      Given("a PhysicsEffect that has empty input list")
      val inputList = List(
        PhysicsInput(
          PhysicsParameter("someParameterId1", TargetType.Parameter),
          ParameterType.X, weight = 1.0f, isReflect = false
        ),
        PhysicsInput(
          PhysicsParameter("someParameterId2", TargetType.Parameter),
          ParameterType.Y, weight = 1.0f, isReflect = false
        ),
        PhysicsInput(
          PhysicsParameter("someParameterId3", TargetType.Parameter),
          ParameterType.Angle, weight = 1.0f, isReflect = false
        ),
        PhysicsInput(
          PhysicsParameter("someParameterId4", TargetType.Parameter),
          ParameterType.Angle, weight = 1.0f, isReflect = false
        )
      )
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 100, 50),
        PhysicsNormalization(0, 100, 50),
        inputs = inputList,
        outputs = Nil,
        initialParticles = Nil
      )

      And("A mocked Live2D Model with mocked parameter")
      val model = stub[Live2DModel]
      val mockedParameters = Map(
        "someParameterId1" -> new JavaVMParameter("someParameterId1", min = 5, max = 50, default = 25, value = 10),
        "someParameterId2" -> new JavaVMParameter("someParameterId2", min = 5, max = 50, default = 25, value = 12),
        "someParameterId3" -> new JavaVMParameter("someParameterId3", min = 5, max = 50, default = 25, value = 14),
        "someParameterId4" -> new JavaVMParameter("someParameterId4", min = 5, max = 50, default = 25, value = 16)
      )
      (() => model.parameters).when().returns(mockedParameters)

      When("calculate particle update parameter")
      val particleUpdateParameter = physicsEffect.calculateParticleUpdateParameter(model)

      Then("X / Y should have correct value")
      inside(particleUpdateParameter.translation) { case EuclideanVector(x, y) =>
        x shouldBe -0.10990112f
        y shouldBe -0.15641274f
      }

      And("the angle should be zero")
      particleUpdateParameter.angle shouldBe -0.4444444f
    }

  }

  Feature("Calculate new particles") {
    Scenario("delay is not 0") {
      val particle1 = data.physics.PhysicsParticle(
        mobility = 1,
        delay = 1,
        acceleration = 1,
        radius = 0,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(0, 0),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particle2 = data.physics.PhysicsParticle(
        mobility = 0.95f,
        delay = 0.8f,
        acceleration = 1.5f,
        radius = 15,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(1f, 15),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particles = List(particle1, particle2)
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 2000, 0),
        PhysicsNormalization(0, 1000, 0),
        Nil, Nil, particles
      )

      val List(updatedParticle1, updateParticle2) = physicsEffect.calculateNewParticleStatus(
        particles,
        data.physics.ParticleUpdateParameter(EuclideanVector(0, 0), 0.0f),
        EuclideanVector(0, 0),
        0.333f,
        0.1f
      )

      updatedParticle1.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.mobility shouldBe 1.0f
      updatedParticle1.delay shouldBe 1.0f
      updatedParticle1.acceleration shouldBe 1.0f
      updatedParticle1.radius shouldBe 0.0f
      updatedParticle1.position shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastGravity shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.velocity shouldBe EuclideanVector(0.0f, 0.0f)

      updateParticle2.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updateParticle2.mobility shouldBe 0.95f
      updateParticle2.delay shouldBe 0.8f
      updateParticle2.acceleration shouldBe 1.5f
      updateParticle2.radius shouldBe 15.0f
      updateParticle2.position shouldBe EuclideanVector(0.0f, 14.998851f)
      updateParticle2.lastPosition shouldBe EuclideanVector(1.0f, 15.0f)
      updateParticle2.lastGravity shouldBe EuclideanVector(0.0f, 1.0f)
      updateParticle2.velocity shouldBe EuclideanVector(-0.118868865f, -1.3660143E-4f)
    }

    Scenario("delay is 0") {
      val particle1 = PhysicsParticle(
        mobility = 1,
        delay = 1,
        acceleration = 1,
        radius = 0,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(0, 0),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particle2 = data.physics.PhysicsParticle(
        mobility = 0.95f,
        delay = 0f,
        acceleration = 1.5f,
        radius = 15,
        initialPosition = EuclideanVector(0.0f, 0.0f),
        position = EuclideanVector(1f, 15),
        lastPosition = EuclideanVector(0.0f, 0.0f),
        lastGravity = EuclideanVector(0.0f, 0.0f),
        velocity = EuclideanVector(0.0f, 0.0f),
      )

      val particles = List(particle1, particle2)
      val physicsEffect = PhysicsEffect(
        PhysicsNormalization(0, 2000, 0),
        PhysicsNormalization(0, 1000, 0),
        Nil, Nil, particles
      )

      val List(updatedParticle1, updateParticle2) = physicsEffect.calculateNewParticleStatus(
        particles,
        ParticleUpdateParameter(EuclideanVector(0, 0), 0.0f),
        windDirection = EuclideanVector(0, 0),
        deltaTimeSeconds = 0.0f,
        0.1f
      )

      updatedParticle1.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.mobility shouldBe 1.0f
      updatedParticle1.delay shouldBe 1.0f
      updatedParticle1.acceleration shouldBe 1.0f
      updatedParticle1.radius shouldBe 0.0f
      updatedParticle1.position shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.lastGravity shouldBe EuclideanVector(0.0f, 0.0f)
      updatedParticle1.velocity shouldBe EuclideanVector(0.0f, 0.0f)

      updateParticle2.initialPosition shouldBe EuclideanVector(0.0f, 0.0f)
      updateParticle2.mobility shouldBe 0.95f
      updateParticle2.delay shouldBe 0.0f
      updateParticle2.acceleration shouldBe 1.5f
      updateParticle2.radius shouldBe 15.0f
      updateParticle2.position shouldBe EuclideanVector(0.0f, -14.966778f)
      updateParticle2.lastPosition shouldBe EuclideanVector(1.0f, 15.0f)
      updateParticle2.lastGravity shouldBe EuclideanVector(0.0f, 1.0f)
      updateParticle2.velocity shouldBe EuclideanVector(0.0f, 0.0f)
    }


    Scenario("A particle list contains multiple items") {
      Given("A particle list")
      val particles = List(
        data.physics.PhysicsParticle(
          mobility = 0.1f, delay = 0.2f, acceleration = 0.3f, radius = 0.4f,
          initialPosition = EuclideanVector(0.5f, 0.6f),
          position = EuclideanVector(0.7f, 0.8f),
          lastPosition = EuclideanVector(0.9f, 1.0f),
          lastGravity = EuclideanVector(1.1f, 1.2f),
          velocity = EuclideanVector(1.3f, 1.4f)
        ),
        data.physics.PhysicsParticle(
          mobility = 1.5f, delay = 1.6f, acceleration = 1.7f, radius = 1.8f,
          initialPosition = EuclideanVector(1.5f, 1.6f),
          position = EuclideanVector(1.7f, 1.8f),
          lastPosition = EuclideanVector(1.9f, 1.0f),
          lastGravity = EuclideanVector(1.1f, 1.2f),
          velocity = EuclideanVector(1.3f, 1.4f)
        ),
        data.physics.PhysicsParticle(
          mobility = 2.1f, delay = 2.2f, acceleration = 2.3f, radius = 2.4f,
          initialPosition = EuclideanVector(2.5f, 2.6f),
          position = EuclideanVector(2.7f, 2.8f),
          lastPosition = EuclideanVector(2.9f, 2.0f),
          lastGravity = EuclideanVector(2.1f, 2.2f),
          velocity = EuclideanVector(2.3f, 2.4f)
        ),
        data.physics.PhysicsParticle(
          mobility = 3.1f, delay = 3.2f, acceleration = 3.3f, radius = 3.4f,
          initialPosition = EuclideanVector(3.5f, 3.6f),
          position = EuclideanVector(3.7f, 3.8f),
          lastPosition = EuclideanVector(3.9f, 3.0f),
          lastGravity = EuclideanVector(3.1f, 3.2f),
          velocity = EuclideanVector(3.3f, 3.4f)
        )
      )

      And("A PhysicsEffect based on above particles")
      val physicsEffect = PhysicsEffect(
        normalizationPosition = PhysicsNormalization(0, 100f, 0f),
        normalizationAngle = PhysicsNormalization(0, 100f, 0f),
        inputs = Nil, outputs = Nil,
        initialParticles = particles
      )

      When("Calculate new particle status")
      val particleUpdateParameter = data.physics.ParticleUpdateParameter(EuclideanVector(1.0f, 2.1f), 0.5f)
      val newParticles = physicsEffect.calculateNewParticleStatus(
        particles, particleUpdateParameter,
        windDirection = EuclideanVector(0.1f, 0.2f),
        deltaTimeSeconds = 0.333f
      )

      Then("The size of new particle list should be the same as old one")
      newParticles.size shouldBe particles.size

      And("the update particle status should be updated")
      newParticles shouldNot be (particles)

      And("the updated list should contains correct data")

      newParticles should contain theSameElementsInOrderAs List(
        data.physics.PhysicsParticle(
          0.1f, 0.2f, 0.3f, 0.4f,
          EuclideanVector(0.5f, 0.6f),
          EuclideanVector(1.0f, 2.1f),
          EuclideanVector(0.9f, 1.0f),
          EuclideanVector(1.1f, 1.2f),
          EuclideanVector(1.3f, 1.4f)
        ),
        data.physics.PhysicsParticle(
          1.5f, 1.6f, 1.7f, 1.8f,
          EuclideanVector(1.5f, 1.6f),
          EuclideanVector(1.179439f, 3.8910336f),
          EuclideanVector(1.7f, 1.8f),
          EuclideanVector(0.008726536f, 0.9999619f),
          EuclideanVector(-0.048851453f, 0.19623064f)
        ),
        data.physics.PhysicsParticle(
          2.1f, 2.2f, 2.3f, 2.4f,
          EuclideanVector(2.5f, 2.6f),
          EuclideanVector(1.3886662f, 6.281896f),
          EuclideanVector(2.7f, 2.8f),
          EuclideanVector(0.008726536f, 0.9999619f),
          EuclideanVector(-0.12529807f, 0.33269548f)
        ),
        data.physics.PhysicsParticle(
          3.1f, 3.2f, 3.3f, 3.4f,
          EuclideanVector(3.5f, 3.6f),
          EuclideanVector(1.6095365f, 9.674715f),
          EuclideanVector(3.7f, 3.8f),
          EuclideanVector(0.008726536f, 0.9999619f),
          EuclideanVector(-0.20271638f, 0.56968266f)
        )
      )

      println(newParticles)
    }
  }

}
