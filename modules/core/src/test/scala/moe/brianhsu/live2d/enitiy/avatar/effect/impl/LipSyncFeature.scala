package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueAdd
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import javax.sound.sampled.Mixer

class LipSyncFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TableDrivenPropertyChecks {

  private val model: Live2DModel = stub[Live2DModel]
  private val lipSyncParameters: List[String] = List("lipSyncP1", "lipSyncP2")

  Feature("Find available input mixer") {

    Scenario("There is no mixer at all") {
      Given("a input mixer list without any mixer")
      val getMixerInfo: Array[Mixer.Info] = Array.empty
      val getMixer = { _: Mixer.Info => stub[Mixer] }

      When("try to find the input mixer")
      val inputMixer = LipSyncFromMic.findInputMixers(getMixerInfo, getMixer)

      Then("the inputMixer should be Nil")
      inputMixer shouldBe Nil
    }

    Scenario("All mixer are not support the input format we requested") {
      Given("a input mixer list mixers that does not support the input format we requested")
      val getMixerInfo: Array[Mixer.Info] = Array(stub[Mixer.Info], stub[Mixer.Info], stub[Mixer.Info])
      val getMixer = { _: Mixer.Info =>
        val stubbedMixer = stub[Mixer]
        (stubbedMixer.isLineSupported _).when(*).returns(false)
        stubbedMixer
      }

      When("try to find the input mixer")
      val inputMixer = LipSyncFromMic.findInputMixers(getMixerInfo, getMixer)

      Then("the inputMixer should be Nil")
      inputMixer shouldBe Nil
    }

    Scenario("Some mixer support the input format we requested") {
      Given("a input mixer list mixers that two of them (2, 3) support the input format we requested")
      val stubbedMixerInfo1 = stub[Mixer.Info]
      val stubbedMixerInfo2 = stub[Mixer.Info]
      val stubbedMixerInfo3 = stub[Mixer.Info]
      val stubbedMixerInfo4 = stub[Mixer.Info]
      val mixerInfoToMixer = Map(
        stubbedMixerInfo1 -> stub[Mixer],
        stubbedMixerInfo2 -> stub[Mixer],
        stubbedMixerInfo3 -> stub[Mixer],
        stubbedMixerInfo4 -> stub[Mixer],
      )

      val getMixerInfo: Array[Mixer.Info] = Array(stubbedMixerInfo1, stubbedMixerInfo2, stubbedMixerInfo3, stubbedMixerInfo4)
      val getMixer = { mixerInfo: Mixer.Info =>
        val stubbedMixer = mixerInfoToMixer(mixerInfo)
        val isLineSupported = mixerInfo == stubbedMixerInfo2 || mixerInfo == stubbedMixerInfo3
        (stubbedMixer.isLineSupported _).when(*).returns(isLineSupported)
        stubbedMixer
      }

      When("try to find the input mixer")
      val inputMixer = LipSyncFromMic.findInputMixers(getMixerInfo, getMixer)

      Then("the inputMixer should be contains input mixer 2, 3")
      inputMixer should contain theSameElementsAs List(
        mixerInfoToMixer(stubbedMixerInfo2),
        mixerInfoToMixer(stubbedMixerInfo3)
      )
    }
  }
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
    override def start(): Unit = {}
    override def stop(): Unit = {}
  }
}
