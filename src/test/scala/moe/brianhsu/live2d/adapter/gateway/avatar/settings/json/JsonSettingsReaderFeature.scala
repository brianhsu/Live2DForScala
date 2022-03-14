package moe.brianhsu.live2d.adapter.gateway.avatar.settings.json

import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.ExpressionSetting.Parameters
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.MotionSetting.{Curve, Meta}
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.PoseSetting.Part
import moe.brianhsu.live2d.enitiy.avatar.settings.detail.{ExpressionSetting, HitAreaSetting, MotionSetting, PoseSetting}
import moe.brianhsu.testUtil.FilePathMatcher
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Inside, OptionValues, TryValues}

import java.io.FileNotFoundException

class JsonSettingsReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with Inside with OptionValues with TryValues with FilePathMatcher {
  Feature("Read Live2D avatar settings") {
    Scenario("Load from Live 2D json setting folder") {
      Given("A folder path contains json files for a Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("Load it using JsonSettingsReader")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings = jsonSettingsReader.loadSettings().success.value

      Then("the success loaded setting should have correct data")
      inside(settings) { case Settings(mocFile, textureFiles, pose, eyeBlinkParameterIds, expressions, motionGroups, hitArea) =>
        mocFile should endWithPath("/src/test/resources/models/Haru/Haru.moc3")

        textureFiles.size shouldBe 2
        textureFiles(0) should endWithPath("src/test/resources/models/Haru/Haru.2048/texture_00.png")
        textureFiles(1) should endWithPath("src/test/resources/models/Haru/Haru.2048/texture_01.png")

        eyeBlinkParameterIds shouldBe List("ParamEyeLOpen", "ParamEyeROpen")
        shouldHaveCorrectPoseSettings(pose.value)
        shouldHaveCorrectExpressions(expressions)
        shouldHaveCorrectMotionGroup(motionGroups)
        hitArea should contain theSameElementsInOrderAs List(
          HitAreaSetting("Head", "HitArea"),
          HitAreaSetting("Body", "HitArea2"),
        )
      }
    }

    Scenario("Load model fom a non exist folder") {
      Given("A folder path that does not exist")
      val folderPath = "src/test/resources/models/DoNoExist"

      When("Load it with JsonSettingReder")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val loadedSettings = jsonSettingsReader.loadSettings()

      Then("it should be a failure contains FileNotFoundException")
      loadedSettings.failure.exception shouldBe a[FileNotFoundException]
    }

    Scenario("Load model fom a folder not containing the main json file") {
      Given("A folder path that does not have .model3.json")
      val folderPath = "src/test/resources/models/corruptedModel/noMainModelJson"

      When("Load it with JsonSettingReder")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val loadedSettings = jsonSettingsReader.loadSettings()

      Then("it should be a failure contains FileNotFoundException")
      loadedSettings.failure.exception shouldBe a[FileNotFoundException]
    }

    Scenario("Load model fom a folder not containing the .moc file") {
      Given("A folder path that does not have .moc file")
      val folderPath = "src/test/resources/models/corruptedModel/noMocFile"

      When("Load it with JsonSettingReder")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val loadedSettings = jsonSettingsReader.loadSettings()

      Then("it should be a failure contains FileNotFoundException")
      loadedSettings.failure.exception shouldBe a[FileNotFoundException]
    }

  }

  def shouldHaveCorrectMotionGroup(motionGroups: Map[String, List[MotionSetting]]): Unit = {
    motionGroups.keySet should contain theSameElementsAs List("idle", "tapBody")
    motionGroups("idle").size shouldBe 2
    motionGroups("tapBody").size shouldBe 4
    val firstIdleMotion = motionGroups("idle").head
    inside(firstIdleMotion) { case MotionSetting(version, fadeInTime, fadeOutTime, meta, userData, curves) =>
      version shouldBe "3"
      fadeInTime.value shouldBe 0.5
      fadeOutTime.value shouldBe 0.5
      inside(meta) { case Meta(duration, fps, loop, areBeziersRestricted, curveCount, totalSegmentCount, totalPointCount, userDataCount) =>
        duration shouldBe 10
        fps shouldBe 30
        loop shouldBe true
        areBeziersRestricted shouldBe true
        curveCount shouldBe 65
        totalSegmentCount shouldBe 298
        totalPointCount shouldBe 789
        userDataCount shouldBe 0
      }
      userData shouldBe Nil
      curves.size shouldBe 65
      inside(curves.head) { case Curve(target, id, fadeInTime, fadeOutTime, segments) =>
        target shouldBe "Model"
        id shouldBe "Opacity"
        fadeInTime shouldBe None
        fadeOutTime shouldBe None
        segments should contain theSameElementsInOrderAs List(
          0, 1, 1, 3.322f,
          1, 6.644f, 1, 9.967f,
          1, 0, 10, 1
        )
      }

    }

  }

  private def shouldHaveCorrectExpressions(expressions: Map[String, ExpressionSetting]): Unit = {
    expressions.size shouldBe 8
    expressions.keys should contain theSameElementsAs Set("f00", "f01", "f02", "f03", "f04", "f05", "f06", "f07")
    val aExpression = expressions("f07")
    inside(aExpression) { case ExpressionSetting(fadeInTime, fadeOutTime, parameters) =>
      fadeInTime.value shouldBe 0.5f
      fadeOutTime.value shouldBe 0.7f
      parameters should contain theSameElementsInOrderAs List(
        Parameters("ParamEyeLOpen", 0.8f, Some("Multiply")),
        Parameters("ParamEyeROpen", 0.8f, Some("Multiply")),
        Parameters("ParamBrowLForm", -0.33f, Some("Add")),
        Parameters("ParamBrowRForm", -0.33f, None),
        Parameters("ParamMouthForm", -1.76f, Some("Overwrite")),
      )
    }
  }

  private def shouldHaveCorrectPoseSettings(pose: PoseSetting) = {
    inside(pose) { case PoseSetting(fadeInTime, groups) =>
      fadeInTime shouldBe Some(1.5f)
      groups should contain theSameElementsAs List(
        List(
          Part("Part01ArmLB001", List("link1", "link2")),
          Part("Part01ArmRA001", Nil)
        ),
        List(
          Part("Part01ArmRB001", List("Part01ArmRA001")),
          Part("Part01ArmLA001", List("link3", "link4"))
        ),
      )
    }
  }
}
