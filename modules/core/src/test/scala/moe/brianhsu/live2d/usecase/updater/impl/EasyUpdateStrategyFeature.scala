package moe.brianhsu.live2d.usecase.updater.impl

import moe.brianhsu.live2d.boundary.gateway.avatar.effect.FaceDirectionCalculator
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl._
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}
import moe.brianhsu.utils.mock.MixerMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Retries}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import javax.sound.sampled.Mixer

class EasyUpdateStrategyFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with Retries with MockFactory with MixerMock {

  override def withFixture(test: NoArgTest) = withRetry {
    super.withFixture(test)
  }

  Feature("Initial status") {
    Scenario("Initial effects after strategy is created") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      Then("default effects should be enabled")
      strategy.isEyeBlinkEnabled shouldBe true
      strategy.isBreathEnabled shouldBe true
      strategy.isFaceDirectionEnabled shouldBe true
      strategy.isLipSyncFromMotionEnabled shouldBe true

      And("the effects should contains correct effect objects")
      strategy.findEffects(_.isInstanceOf[EyeBlink], BeforeExpression).nonEmpty shouldBe true
      strategy.findEffects(_.isInstanceOf[Breath], BeforeExpression).nonEmpty shouldBe false
      strategy.findEffects(_.isInstanceOf[FaceDirection], BeforeExpression).nonEmpty shouldBe false
      strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], BeforeExpression).nonEmpty shouldBe false
      strategy.findEffects(_.isInstanceOf[LipSyncFromMic], BeforeExpression).nonEmpty shouldBe false

      strategy.findEffects(_.isInstanceOf[EyeBlink], AfterExpression).nonEmpty shouldBe false
      strategy.findEffects(_.isInstanceOf[Breath], AfterExpression).nonEmpty shouldBe true
      strategy.findEffects(_.isInstanceOf[FaceDirection], AfterExpression).nonEmpty shouldBe true
      strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression).nonEmpty shouldBe true
      strategy.findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression).nonEmpty shouldBe false

    }
  }

  Feature("Enable / disable eye blink effect") {

    Scenario("Enable eye blink effect when it's already enabled by default") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable eye blink")
      strategy.enableEyeBlink(true)

      Then("the isEyeBlinkEnabled should still be true")
      strategy.isEyeBlinkEnabled shouldBe true

      And("the BeforeExpression effect list contains EyeBlink object")
      strategy.findEffects(_.isInstanceOf[EyeBlink], BeforeExpression).nonEmpty shouldBe true
    }

    Scenario("Disable eye blink effect") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable eye blink")
      strategy.enableEyeBlink(false)

      Then("the isEyeBlinkEnabled should change to false")
      strategy.isEyeBlinkEnabled shouldBe false

      And("the BeforeExpression effect list does NOT contains EyeBlink object")
      strategy.findEffects(_.isInstanceOf[EyeBlink], BeforeExpression).isEmpty shouldBe true
    }

    Scenario("Enable eye blink after disable eye blink") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable eye blink")
      strategy.enableEyeBlink(false)

      Then("the isEyeBlinkEnabled should change to false")
      strategy.isEyeBlinkEnabled shouldBe false

      When("enable eye blink again")
      strategy.enableEyeBlink(true)

      Then("the isEyeBlinkEnabled should change to true")
      strategy.isEyeBlinkEnabled shouldBe true

      And("the BeforeExpression effect list contains EyeBlink object")
      strategy.findEffects(_.isInstanceOf[EyeBlink], BeforeExpression).nonEmpty shouldBe true
    }

  }

  Feature("Enable / disable breath effect") {

    Scenario("Enable breath effect when it's already enabled by default") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable breath")
      strategy.enableBreath(true)

      Then("the isBreathEnabled should still be true")
      strategy.isBreathEnabled shouldBe true

      And("the AfterExpression effect list contains Breath object")
      strategy.findEffects(_.isInstanceOf[Breath], AfterExpression).nonEmpty shouldBe true
    }

    Scenario("Disable breath effect") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable breath")
      strategy.enableBreath(false)

      Then("the isBreathEnabled should still be false")
      strategy.isBreathEnabled shouldBe false

      And("the AfterExpression effect list does NOT contains Breath object")
      strategy.findEffects(_.isInstanceOf[Breath], AfterExpression).isEmpty shouldBe true
    }

    Scenario("Enable breath again") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable breath first")
      strategy.enableBreath(false)

      Then("the isBreathEnabled should be false")
      strategy.isBreathEnabled shouldBe false

      When("enable breath again")
      strategy.enableBreath(true)

      Then("the isBreathEnabled should be true")
      strategy.isBreathEnabled shouldBe true

      And("the AfterExpression effect list contains Breath object")
      strategy.findEffects(_.isInstanceOf[Breath], AfterExpression).nonEmpty shouldBe true
    }

  }

  Feature("Enable / disable face direction effect") {

    Scenario("Enable face direction effect when it's already enabled by default") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable face direction")
      strategy.enableFaceDirection(true)

      Then("the isBreathEnabled should still be true")
      strategy.isFaceDirectionEnabled shouldBe true

      And("the AfterExpression effect list contains FaceDirection object")
      strategy.findEffects(_.isInstanceOf[FaceDirection], AfterExpression).nonEmpty shouldBe true
    }

    Scenario("Disable face direction effect") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable face direction")
      strategy.enableFaceDirection(false)

      Then("the isFaceDirectionEnabled should change to false")
      strategy.isFaceDirectionEnabled shouldBe false

      And("the AfterExpression effect list does NOT contains FaceDirection object")
      strategy.findEffects(_.isInstanceOf[FaceDirection], AfterExpression).isEmpty shouldBe true
    }

    Scenario("Enable face direction again") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable face direction")
      strategy.enableFaceDirection(false)

      Then("the isFaceDirectionEnabled should be false")
      strategy.isFaceDirectionEnabled shouldBe false

      When("enable face direction again")
      strategy.enableFaceDirection(true)

      Then("the isFaceDirectionEnabled should be true")
      strategy.isFaceDirectionEnabled shouldBe true

      And("the AfterExpression effect list contains FaceDirection object")
      strategy.findEffects(_.isInstanceOf[FaceDirection], AfterExpression).nonEmpty shouldBe true
    }

  }

  Feature("Enable / disable lip sync from motion effect") {

    Scenario("Enable lip sync from motion effect when it's already enabled by default") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable lip sync from motion")
      strategy.enableLipSyncFromMotion(true)

      Then("the isLipSyncFromMotionEnabled should still be true")
      strategy.isLipSyncFromMotionEnabled shouldBe true

      And("the AfterExpression effect list contains LipSyncFromMotionSound object")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression).nonEmpty shouldBe true
    }

    Scenario("Disable lip sync from motion effect") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable lip sync from motion")
      strategy.enableLipSyncFromMotion(false)

      Then("the isLipSyncFromMotionEnabled should change to false")
      strategy.isLipSyncFromMotionEnabled shouldBe false

      And("the AfterExpression effect list does NOT contains LipSyncFromMotionSound object")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression).isEmpty shouldBe true
    }

    Scenario("Enable lip sync from motion again") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("disable lip sync from motion")
      strategy.enableLipSyncFromMotion(false)

      Then("the isLipSyncFromMotionEnabled should be false")
      strategy.isLipSyncFromMotionEnabled shouldBe false

      When("enable lip sync from motion again")
      strategy.enableLipSyncFromMotion(true)

      Then("the isLipSyncFromMotionEnabled should be true")
      strategy.isLipSyncFromMotionEnabled shouldBe true

      And("the AfterExpression effect list contains LipSyncFromMotionSound object")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression).nonEmpty shouldBe true
    }

    Scenario("Update weight of lip sync from motion") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      And("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      When("update the weight to 25")
      strategy.updateLipSyncFromMotionWeight(25)

      Then("the weight of lip sync from motion should be 2.5")
      val lipSync = strategy
        .findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
        .map(_.asInstanceOf[LipSyncFromMotionSound])
        .head

      lipSync.weight shouldBe 2.5f
    }

    Scenario("Update volume of lip sync from motion") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      And("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      When("update the volume to 35")
      strategy.updateLipSyncFromMotionVolume(35)

      Then("the weight of lip sync from motion should be 35")
      val lipSync = strategy
        .findEffects(_.isInstanceOf[LipSyncFromMotionSound], AfterExpression)
        .map(_.asInstanceOf[LipSyncFromMotionSound])
        .head

      lipSync.volume shouldBe 35
    }

  }

  Feature("Enable / disable lip sync from mic effect") {
    Scenario("Enable lip sync from mic successfully") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable lip sync from mic")
      val (mixer, _) = createStubbedMixer()
      strategy.enableMicLipSync(mixer, 50, forceEvenNoSetting = true)

      Then("the AfterExpression should contain LipSyncFromMic effect")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression).nonEmpty shouldBe true
    }

    Scenario("Failure to enable lip sync from mic") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("create a mixer that will throw exception when try to get line info")
      val mixer = stub[Mixer]

      And("use that mixer to enable LipSyncFromMic")
      strategy.enableMicLipSync(mixer, 50, forceEvenNoSetting = true)

      Then("the AfterExpression should NOT contain LipSyncFromMic effect")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression).isEmpty shouldBe true
    }

    Scenario("Disable lip sync from mic effect") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      When("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable lip sync from mic")
      val (mixer, _) = createStubbedMixer()
      strategy.enableMicLipSync(mixer, 50, forceEvenNoSetting = true)

      Then("the AfterExpression should contain LipSyncFromMic effect")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression).nonEmpty shouldBe true

      When("disable lip sync from mic")
      strategy.disableMicLipSync()

      Then("the AfterExpression should not contain LipSyncFromMic effect")
      strategy.findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression).isEmpty shouldBe true
    }

    Scenario("Update weight of lip sync from motion") {
      Given("stubbed avatar faceDirection calculator")
      val mockContext = new MockFactory {}
      val faceDirectionCalculator = stub[FaceDirectionCalculator](mockContext)
      val avatar = createStubAvatar()

      And("create an EasyUpdateStrategy from that avatar")
      val strategy = new EasyUpdateStrategy(avatar, faceDirectionCalculator)

      And("enable lip sync from mic")
      val (mixer, _) = createStubbedMixer()
      strategy.enableMicLipSync(mixer, 50, forceEvenNoSetting = true)

      When("update the weight to 25")
      strategy.updateMicLipSyncWeight(25)

      Then("the weight of lip sync from mic should be 2.5")
      val lipSync = strategy
        .findEffects(_.isInstanceOf[LipSyncFromMic], AfterExpression)
        .map(_.asInstanceOf[LipSyncFromMic])
        .head

      lipSync.weight shouldBe 2.5f
    }


  }

  Feature("Start motion callback") {
    Scenario("Test") {
      Given("stubbed avatar and a stubbed LipSyncFromMotionSound")
      val avatar = createStubAvatar()
      val mockContext = new MockFactory {}
      val lipSyncFromMotionSound = stub[LipSyncFromMotionSound](mockContext)

      And("create an EasyUpdateStrategy from that avatar and stubbed LipSyncFromMotionSound")
      val strategy = new EasyUpdateStrategy(avatar, stub[EyeBlink], stub[Breath], lipSyncFromMotionSound, stub[FaceDirection])

      When("start a dummy MotionSetting")
      val motionSetting = MotionSetting("3", None, None, Some("soundFile.wav"), MotionSetting.Meta(1.0f, 30.0f, loop = true, areBeziersRestricted = true, 0, 0, 0, 0), Nil, Nil)
      strategy.startMotion(motionSetting, isLoop = false)

      Then("the lipSyncFromMotionSound should receive a request to call startWith() method")
      (lipSyncFromMotionSound.startWith _).verify(Some("soundFile.wav")).once()
    }
  }

  private def createStubAvatar(): Avatar = {
    val mockContext = new MockFactory {}
    val model: Live2DModel = mock[Live2DModel](mockContext)
    val avatarSettings = Settings(null, Nil, None, None, Nil, Nil, Map.empty, Map.empty, Nil)
    Avatar(avatarSettings, model)
  }
}
