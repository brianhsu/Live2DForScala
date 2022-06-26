package moe.brianhsu.live2d.adapter.gateway.avatar

import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import org.json4s.MappingException
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

import java.io.FileNotFoundException

class AvatarFileLoaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {
  private implicit val cubismCore = new JnaNativeCubismAPILoader

  Feature("Error handling") {
    Scenario("Loading an avatar from non-exist directory") {
      Given("a non-exist directory")
      val directory = "NotExistDirectory"

      When("loading an avatar from it")
      val avatarHolder = new AvatarFileReader(directory).loadAvatar()

      Then("it should return a Failure")
      avatarHolder.failure.exception shouldBe a [FileNotFoundException]
    }

    Scenario("Loading an avatar from an exist directory that is not an avatar runtime") {
      Given("a directory that exist but not a runtime")
      val directory = "src/main/scala"

      When("loading an avatar from it")
      val avatarHolder = new AvatarFileReader(directory).loadAvatar()

      Then("it should return a Failure")
      avatarHolder.failure.exception shouldBe an[FileNotFoundException]
    }

    Scenario("Loading an avatar from an exist directory that has corrupted settings") {
      Given("a directory that exist but not a runtime")
      val directory = "src/test/resources/models/corruptedModel/noRequiredFields"

      When("loading an avatar from it")
      val avatarHolder = new AvatarFileReader(directory).loadAvatar()

      Then("it should return a Failure")
      avatarHolder.failure.exception shouldBe an[MappingException]
    }
  }

  Feature("Loading an avatar") {
    Scenario("Loading an avatar from directory") {
      Given("a runtime directory")
      val directory = "src/test/resources/models/HaruGreeter/runtime"

      When("loading an avatar from it")
      val avatarHolder = new AvatarFileReader(directory).loadAvatar()

      Then("it should return a Success[Avatar]")
      val avatar = avatarHolder.success.value
      avatar shouldBe a [Avatar]
    }

  }

}
