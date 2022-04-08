package moe.brianhsu.live2d.enitiy.avatar.physics.data

import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import moe.brianhsu.live2d.enitiy.model.{JavaVMParameter, Live2DModel}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Inside}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

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
    Scenario("Calculate new particles") {
      pending
    }
  }

}
