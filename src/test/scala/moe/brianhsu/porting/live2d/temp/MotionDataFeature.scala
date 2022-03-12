package moe.brianhsu.porting.live2d.temp

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.porting.live2d.framework.{CubismMotionData, PartData}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, TryValues}

class MotionDataFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues {
  Feature("Read pose parts data from Live2D avatar settings") {

    Scenario("Load motion from avatar Mark") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("Create a MotionData from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      CubismMotionData.apply(settings.motionGroups("idle")(0))
      CubismMotionData.apply(settings.motionGroups("idle")(3))
    }

    Scenario("Load motion from avatar Hiyori") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Hiyori"

      When("Create a MotionData from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      CubismMotionData.apply(settings.motionGroups("idle")(0))
    }
  }

}
