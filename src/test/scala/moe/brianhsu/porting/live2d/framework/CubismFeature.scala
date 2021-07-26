package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.live2d.adapter.gateway.core.JnaCubismCore
import moe.brianhsu.live2d.adapter.gateway.model.CubismLive2DModel
import moe.brianhsu.live2d.enitiy.core.types.{CsmLogFunction, CsmVersion, MocVersion40}
import moe.brianhsu.porting.live2d.framework.model.Avatar
import org.scalatest.{GivenWhenThen, TryValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.io.FileNotFoundException

class CubismFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {
  private val cubism = new Cubism
  Feature("Init with custom logger") {
    Scenario("create cubism with custom logger") {
      Given("A custom logger")
      object CustomLogger extends CsmLogFunction {
        override def invoke(message: String): Unit = {
          println(message)
        }
      }

      When("create a Cubism object with that logger")
      new JnaCubismCore(CustomLogger)

      Then("Everything should be fine.")
    }
  }

  Feature("Get version information") {
    Scenario("Get core library version") {
      When("get core library version")
      val version = cubism.coreLibraryVersion

      Then("it should get the version correctly")
      version shouldBe CsmVersion(4, 0, 0)
    }

    Scenario("Get supported .moc file version") {
      When("get latest supported .moc file version")
      val version = cubism.latestSupportedMocVersion

      Then("it should get the version correctly")
      version shouldBe MocVersion40
    }
  }

  Feature("Error handling") {
    Scenario("Loading an avatar from non-exist directory") {
      Given("a non-exist directory")
      val directory = "NotExistDirectory"

      When("loading an avatar from it")
      val avatarHolder = cubism.loadAvatar(directory)

      Then("it should return a Failure")
      avatarHolder.failure.exception shouldBe a [FileNotFoundException]
    }

    Scenario("Loading an avatar from an exist directory that is not an avatar runtime") {
      Given("a directory that exist but not a runtime")
      val directory = "src/main/scala"

      When("loading an avatar from it")
      val avatarHolder = cubism.loadAvatar(directory)

      Then("it should return a Failure")
      avatarHolder.failure.exception shouldBe an[AssertionError]
    }

    Scenario("Loading an avatar from an exist directory that has corrupted settings") {
      Given("a directory that exist but not a runtime")
      val directory = "src/test/resources/models/corruptedModel/noRequiredFields"

      When("loading an avatar from it")
      val avatarHolder = cubism.loadAvatar(directory)

      Then("it should return a Failure")
      avatarHolder.failure.exception shouldBe an[AssertionError]
    }
  }

  Feature("Loading an avatar") {
    Scenario("Loading an avatar from directory") {
      Given("a runtime directory")
      val directory = "src/test/resources/models/HaruGreeter/runtime"

      When("loading an avatar from it")
      val avatarHolder = cubism.loadAvatar(directory)

      Then("it should return a Success[Avatar]")
      val avatar = avatarHolder.success.value
      avatar shouldBe a [Avatar]

      And("parse the model successfully")
      avatar.modelHolder.success.value shouldBe a [CubismLive2DModel]
    }

  }

}
