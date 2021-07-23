package moe.brianhsu.live2d.gateway.impl

import moe.brianhsu.live2d.enitiy.avatar.settings.ExpressionSettings.ExpressionParameter
import moe.brianhsu.live2d.enitiy.avatar.settings.PoseSettings.Group
import moe.brianhsu.live2d.enitiy.avatar.settings.{ExpressionSettings, MotionSetting, PoseSettings, Settings}
import moe.brianhsu.live2d.framework.model.settings.{MotionCurve, MotionMeta}
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
        shouldHaveCorrectExpressions(expressions)
        shouldHaveCorrectMotionGroup(motionGroups)
      }
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
      inside(meta) { case MotionMeta(duration, fps, loop, areBeziersRestricted, curveCount, totalSegmentCount, totalPointCount, userDataCount, totalUserDataSize) =>
        duration shouldBe 10
        fps shouldBe 30
        loop shouldBe true
        areBeziersRestricted shouldBe true
        curveCount shouldBe 65
        totalSegmentCount shouldBe 298
        totalPointCount shouldBe 789
        userDataCount shouldBe 0
        totalUserDataSize shouldBe 0
      }
      userData shouldBe Nil
      curves.size shouldBe 65
      inside(curves.head) { case MotionCurve(target, id, fadeInTime, fadeOutTime, segments) =>
        target shouldBe "Model"
        id shouldBe "Opacity"
        segments should contain theSameElementsInOrderAs List(
          0, 1, 1, 3.322f,
          1, 6.644f, 1, 9.967f,
          1, 0, 10, 1
        )
      }

    }

  }

  private def shouldHaveCorrectExpressions(expressions: Map[String, ExpressionSettings]): Unit = {
    expressions.size shouldBe 8
    expressions.keys should contain theSameElementsAs Set("f00", "f01", "f02", "f03", "f04", "f05", "f06", "f07")
    val aExpression = expressions("f07")
    inside(aExpression) { case ExpressionSettings(typeName, fadeInTime, fadeOutTime, parameters) =>
      typeName shouldBe "Live2D Expression"
      fadeInTime.value shouldBe 0.5f
      fadeOutTime.value shouldBe 0.7f
      parameters should contain theSameElementsInOrderAs List(
        ExpressionParameter("ParamEyeLOpen", 0.8f, Some("Multiply")),
        ExpressionParameter("ParamEyeROpen", 0.8f, Some("Multiply")),
        ExpressionParameter("ParamBrowLForm", -0.33f, Some("Add")),
        ExpressionParameter("ParamBrowRForm", -0.33f, Some("Add")),
        ExpressionParameter("ParamMouthForm", -1.76f, Some("Add")),
      )
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
