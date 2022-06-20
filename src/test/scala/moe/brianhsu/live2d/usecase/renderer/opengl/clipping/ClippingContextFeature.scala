package moe.brianhsu.live2d.usecase.renderer.opengl.clipping

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext.Layout
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureColor
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{GivenWhenThen, Inside, OptionValues}


class ClippingContextFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with OptionValues
                             with Inside with TableDrivenPropertyChecks {
  Feature("Layout and channel color") {
    Scenario("Get channel count") {
      When("Ask for channel count")
      val channelCount  = ClippingContext.ColorChannelCount

      Then("it should be 4")
      channelCount shouldBe 4
    }

    Scenario("Get channel color of Layout") {
      val testData = Table(
        ("channelNo", "expectedResult"),
        (0, TextureColor(1.0f, 0.0f, 0.0f, 0.0f)),
        (1, TextureColor(0.0f, 1.0f, 0.0f, 0.0f)),
        (2, TextureColor(0.0f, 0.0f, 1.0f, 0.0f)),
        (3, TextureColor(0.0f, 0.0f, 0.0f)),
      )

      forAll(testData) { case (channelNo, expectedResult) =>
        Given(s"A layout with channel $channelNo")
        val layout = Layout(channelNo, Rectangle())

        When("When ask for channel color")
        val channelColor = layout.channelColor

        Then(s"it should be $expectedResult")
        channelColor shouldBe expectedResult
      }
    }

  }
}
