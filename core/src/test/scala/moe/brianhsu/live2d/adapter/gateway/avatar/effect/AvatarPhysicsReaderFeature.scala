package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.ParameterType.{Angle, X}
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.TargetType.Parameter
import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics.{PhysicsData, PhysicsEffect, PhysicsInput, PhysicsNormalization, PhysicsOutput, PhysicsParameter, PhysicsParticle}
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.Physics
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Inside, OptionValues, TryValues}

class AvatarPhysicsReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues with OptionValues with Inside {
  Feature("Read avatar physics data from Live2D avatar settings") {
    Scenario("Load data from a avatar without physics") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("create a physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarPhysicsReader(settings)
      val physicsHolder = reader.loadPhysics

      Then("the result should be a None")
      physicsHolder shouldBe None
    }

    Scenario("Load data from a avatar with physics effects") {
      Given("a folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("create a physics effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarPhysicsReader(settings)
      val physics = reader.loadPhysics.value

      Then("the result should contains the correct data")
      inside(physics) { case Physics(physicsData, gravityDirection, windDirection) =>
        inside(physicsData) { case PhysicsData(effects, gravity, wind) =>
          effects.size shouldBe 1
          inside(effects.head) { case PhysicsEffect(normalizationPosition,
                                                    normalizationAngle,
                                                    inputs, outputs, initialParticles) =>
            normalizationPosition shouldBe PhysicsNormalization(-10.0f, 10.0f, 0)
            normalizationAngle shouldBe PhysicsNormalization(-10.0f, 10.0f, 0)
            inputs should contain theSameElementsInOrderAs List(
              PhysicsInput(PhysicsParameter("ParamAngleX", Parameter), X, 60.0f, isReflect = false),
              PhysicsInput(PhysicsParameter("ParamAngleZ", Parameter), Angle, 60.0f, isReflect = false),
              PhysicsInput(PhysicsParameter("ParamBodyAngleX",Parameter), X, 40.0f, isReflect = false),
              PhysicsInput(PhysicsParameter("ParamBodyAngleZ", Parameter), Angle, 40.0f, isReflect = false)
            )
            outputs should contain theSameElementsInOrderAs List(
              PhysicsOutput(PhysicsParameter("ParamHairFront", Parameter), Angle, 1, EuclideanVector(0.0f, 0.0f), 1.397f, isReflect = false, weight = 100.0f),
              PhysicsOutput(PhysicsParameter("ParamHairSide", Parameter), Angle, 1, EuclideanVector(0.0f, 0.0f), 1.397f, isReflect = false, weight = 100.0f),
              PhysicsOutput(PhysicsParameter("ParamHairBack", Parameter), Angle, 1, EuclideanVector(0.0f, 0.0f), 1.397f, isReflect = false, weight = 100.0f)
            )

            initialParticles should contain theSameElementsInOrderAs List(
              PhysicsParticle(1.0f, 1.0f, 1.0f, 0.0f, EuclideanVector(0.0f, 0.0f), EuclideanVector(0.0f, 0.0f), EuclideanVector(0.0f, 0.0f), EuclideanVector(0.0f, 1.0f), EuclideanVector(0.0f, 0.0f)),
              PhysicsParticle(0.95f, 0.8f, 1.5f, 15.0f, EuclideanVector(0.0f, 15.0f), EuclideanVector(0.0f, 15.0f), EuclideanVector(0.0f, 15.0f), EuclideanVector(0.0f, 1.0f), EuclideanVector(0.0f, 0.0f))
            )
          }
          gravity shouldBe EuclideanVector(0.0f, -1.0f)
          wind shouldBe EuclideanVector(0.0f, 0.0f)
        }
        gravityDirection shouldBe EuclideanVector(0.0f, -1.0f)
        windDirection shouldBe EuclideanVector(0.0f, 0.0f)
      }
    }

  }

}