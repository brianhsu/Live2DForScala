package moe.brianhsu.live2d.adapter.gateway.avatar.motion

import moe.brianhsu.live2d.adapter.gateway.avatar.settings.json.JsonSettingsReader
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.Expression.{Add, Multiply, Overwrite, Parameter}
import moe.brianhsu.live2d.enitiy.avatar.settings.Settings
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, OptionValues, TryValues}

class AvatarExpressionReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with TryValues with OptionValues {
  Feature("Read expression from Live2D avatar settings") {
    Scenario("Load expression mapping from avatar json files") {
      Given("a folder path contains json files for Haru Live2D avatar model")
      val folderPath = "src/test/resources/models/Haru"

      When("create a expression map from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarExpressionReader(settings)
      val expressions: Map[String, Expression] = reader.loadExpressions

      Then("it should have correct expression map")
      expressions.size shouldBe 8
      expressions.keySet should contain theSameElementsAs Set("f00", "f01", "f02", "f03", "f04", "f05", "f06", "f07")

      And("the f00 expression should have correct value")
      val f00 = expressions.get("f00").value
      f00.fadeInTimeInSeconds shouldBe Some(1.0f)
      f00.fadeOutTimeInSeconds shouldBe Some(1.0f)
      f00.events shouldBe Nil
      f00.parameters should contain theSameElementsInOrderAs List(
        Parameter("ParamMouthForm", Add, 0.27f)
      )

      And("the f07 expression should have correct value")
      val f07 = expressions.get("f07").value
      f07.fadeInTimeInSeconds shouldBe Some(0.5f)
      f07.fadeOutTimeInSeconds shouldBe Some(0.7f)
      f07.events shouldBe Nil
      f07.parameters should contain theSameElementsInOrderAs List(
        Parameter("ParamEyeLOpen", Multiply, 0.8f),
        Parameter("ParamEyeROpen", Multiply, 0.8f),
        Parameter("ParamBrowLForm", Add, -0.33f),
        Parameter("ParamBrowRForm", Add, -0.33f),
        Parameter("ParamMouthForm", Overwrite, -1.76f)
      )

    }

    Scenario("Load expression mapping from avatar without any expression") {
      Given("a folder path contains json files for Mark Live2D avatar model")
      val folderPath = "src/test/resources/models/Mark"

      When("create a expression map from this Live2D avatar settings")
      val jsonSettingsReader = new JsonSettingsReader(folderPath)
      val settings: Settings = jsonSettingsReader.loadSettings().success.value
      val reader = new AvatarExpressionReader(settings)
      val expressions: Map[String, Expression] = reader.loadExpressions

      Then("the expressions map should be empty")
      expressions.isEmpty shouldBe true
    }

  }

}
