package moe.brianhsu.live2d.adapter.gateway.avatar.effect

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.effect.data.PosePart
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

class AvatarPoseReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {
  Feature("Read pose parts data from Live2D avatar settings") {
    Scenario("Load pose with fade in time specific inside json file") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("create a Pose effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarPoseReader(settings)
      val pose = reader.loadPose.get

      Then("it should have correct pose part data")
      pose.posePartGroups should contain theSameElementsInOrderAs List(
        List(
          PosePart("Part01ArmLB001", List(PosePart("link1", Nil), PosePart("link2", Nil))),
          PosePart("Part01ArmRA001", Nil)
        ),
        List(
          PosePart("Part01ArmRB001", List(PosePart("Part01ArmRA001", Nil))),
          PosePart("Part01ArmLA001", List(PosePart("link3", Nil), PosePart("link4", Nil)))
        )
      )

      And("the fade in time should has same value as in json file")
      pose.fadeTimeInSeconds shouldBe 1.5f
    }

    Scenario("Load pose with default fade in time") {
      Given("A folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/HaruGreeter/runtime"

      When("create a Pose effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarPoseReader(settings)
      val pose = reader.loadPose.get

      Then("it should have correct pose part data")
      pose.posePartGroups should contain theSameElementsInOrderAs List(
        List(
          PosePart("Part01ArmRA001", Nil),
          PosePart("Part01ArmRB001", Nil)
        ),
        List(
          PosePart("Part01ArmLA001", Nil),
          PosePart("Part01ArmLB001", Nil)
        )
      )

      And("the fade in time should has same value as in json file")
      pose.fadeTimeInSeconds shouldBe 0.5
    }

    Scenario("Load pose from avatar that does not utilize Pose") {
      Given("a folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("create a Pose effect from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarPoseReader(settings)
      val pose = reader.loadPose

      Then("the result should be a None")
      pose shouldBe None
    }

  }

}
