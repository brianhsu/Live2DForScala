package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueAdd
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class LipSyncFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TableDrivenPropertyChecks {

  private val model: Live2DModel = stub[Live2DModel]
  private val lipSyncParameters: List[String] = List("lipSyncP1", "lipSyncP2")

  Feature("Calculate the lip sync operation") {
    Scenario("The rms is 0") {
      When("the current RMS is 0")
      val lipSync = new MockLipSync(0, 1.0f)

      Then("it should return an empty list when calculate the parameter")
      lipSync.calculateOperations(model, 0, 0) shouldBe Nil
    }

    Scenario("The rms is not 0") {

      val testData = Table(
        ("RMS", "weight"),
        (0.123f, 0.5f),
        (0.321f, 1.0f),
        (0.456f, 2.3f)
      )

      forAll(testData) { case (rms, weight) =>
        When("the current RMS is not 0")
        val lipSync = new MockLipSync(rms, weight)

        Then("it should return an empty list when calculate the parameter")
        val expectedOperation = List(
          ParameterValueAdd("lipSyncP1", rms, weight),
          ParameterValueAdd("lipSyncP2", rms, weight)
        )

        lipSync.calculateOperations(model, 0, 0) should contain theSameElementsInOrderAs expectedOperation
      }

    }
  }

  class MockLipSync(mRMS: Float, mWeight: Float) extends LipSync {
    override protected var weight: Float = mWeight
    override protected def lipSyncIds: List[String] = lipSyncParameters
    override protected def currentRms: Float = mRMS
  }
}
