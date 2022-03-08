package moe.brianhsu.porting.live2d.framework.effect.impl

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.porting.live2d.framework.{PartData, Pose}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

class PoseFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {
  Feature("Read Live2D avatar settings") {
    Scenario("Load from Live 2D json setting folder") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("Load it using JsonSettingsReader")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val pose = Pose(settings)

      pose.partGroups should contain theSameElementsInOrderAs List(
        PartData("Part01ArmLB001", List(PartData("link1", Nil), PartData("link2", Nil))),
        PartData("Part01ArmRA001", Nil),
        PartData("Part01ArmRB001", Nil),
        PartData("Part01ArmLA001", List(PartData("link3", Nil), PartData("link4", Nil)))
      )
      pose.partGroupCount shouldBe List(2, 2)
      pose.fadeTimeInSeconds shouldBe 1.5f
    }
  }

}
