package moe.brianhsu.live2d.enitiy.avatar.effect.data.physics

import moe.brianhsu.live2d.enitiy.avatar.effect.data.physics
import moe.brianhsu.live2d.enitiy.math.EuclideanVector
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class PhysicOutputFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TableDrivenPropertyChecks {

  Feature("Check vertex index is valid") {
    Scenario("The vertex index is >= 1 and <= particleCount") {
      val expectation = Table(
        ("particleCount", "vertexIndex"),
        (              3,             1),
        (              5,             2),
        (              7,             3),
      )

      forAll(expectation) { (particleCount, vertexIndex) =>
        Given("a PhysicOutput with assigned vertexIndex")
        val output = PhysicsOutput(
          PhysicsParameter("id", TargetType.Parameter),
          ParameterType.X,
          vertexIndex,
          EuclideanVector(0.0f, 0.0f),
          angleScale = 1.0f,
          isReflect = false,
          weight = 1.0f
        )

        Then("the hasValidVertexIndex should be true")
        output.hasValidVertexIndex(particleCount) shouldBe true
      }
    }

    Scenario("The vertexIndex is invalid") {
      val expectation = Table(
        ("particleCount", "vertexIndex"),
        (              3,             0), // The vertexIndex <= 0
        (              5,             5), // The vertexIndex == particleCount
        (              7,             8), // The vertexIndex > particleCount
      )

      forAll(expectation) { (particleCount, vertexIndex) =>
        Given("a PhysicOutput with assigned vertexIndex")
        val output = physics.PhysicsOutput(
          PhysicsParameter("id", TargetType.Parameter),
          ParameterType.X,
          vertexIndex,
          EuclideanVector(0.0f, 0.0f),
          angleScale = 1.0f,
          isReflect = false,
          weight = 1.0f
        )

        Then("the hasValidVertexIndex should be false")
        output.hasValidVertexIndex(particleCount) shouldBe false
      }
    }

  }

}
