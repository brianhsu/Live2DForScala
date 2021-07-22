package moe.brianhsu.live2d.gateway.impl

import moe.brianhsu.live2d.enitiy.avatar.settings.PoseSettings.Group
import moe.brianhsu.live2d.enitiy.avatar.settings.{PoseSettings, Settings}
import org.scalatest.{GivenWhenThen, Inside, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class JsonSettingsReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with Inside with OptionValues {
  Feature("Read Live2D avatar settings") {
    Scenario("Load from Live 2D json setting folder") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("Load it using JsonSettingsReader")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings = jsonSettingsReader.loadSettings()

      Then("the loaded setting should have correct data")
      inside(settings) { case Settings(mocFile, textureFiles, pose, eyeBlinkParameterIds, expressions, motionGroups) =>
        mocFile shouldBe "src/test/resources/models/Haru/Haru.moc3"
        textureFiles shouldBe List(
          "src/test/resources/models/Haru/Haru.2048/texture_00.png",
          "src/test/resources/models/Haru/Haru.2048/texture_01.png"
        )

        eyeBlinkParameterIds shouldBe List("ParamEyeLOpen", "ParamEyeROpen")
        shouldHaveCorrectPoseSettings(pose.value)
        println(expressions)
      }
    }
  }

  private def shouldHaveCorrectPoseSettings(pose: PoseSettings) = {
    inside(pose) { case PoseSettings(typeName, fadeInTime, groups) =>
      typeName shouldBe "Live2D Pose"
      fadeInTime shouldBe Some(1.5f)
      groups should contain theSameElementsAs List(
        List(
          Group("Part01ArmLB001", List("link1", "link2")),
          Group("Part01ArmRA001", Nil)
        ),
        List(
          Group("Part01ArmRB001", Nil),
          Group("Part01ArmLA001", List("link3", "link4"))
        ),
      )
    }
  }
}
