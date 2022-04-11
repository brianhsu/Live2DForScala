package moe.brianhsu.live2d.usecase.renderer

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewOrientation
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewOrientation.{Horizontal, Vertical}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ViewPortCalculatorFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  Feature("Calculate device to screen / viewport matrix") {
    Scenario("The view is Horizontal (width == height)") {
      Given("A DrawCanvasInfo that is horizontal")
      val drawCanvasInfo = stub[DrawCanvasInfoReader]
      (() => drawCanvasInfo.currentCanvasWidth).when().returns(1024)
      (() => drawCanvasInfo.currentCanvasHeight).when().returns(1024)

      When("Create ViewOrientation")
      val orientation = ViewOrientation(drawCanvasInfo)

      Then("The orientation should be Horizontal")
      orientation shouldBe Horizontal
    }

    Scenario("The view is Horizontal (width > height)") {
      Given("A DrawCanvasInfo that is horizontal")
      val drawCanvasInfo = stub[DrawCanvasInfoReader]
      (() => drawCanvasInfo.currentCanvasWidth).when().returns(1024)
      (() => drawCanvasInfo.currentCanvasHeight).when().returns(768)

      When("Create ViewOrientation")
      val orientation = ViewOrientation(drawCanvasInfo)

      Then("The orientation should be Horizontal")
      orientation shouldBe Horizontal
    }

    Scenario("The view is Vertical (height > weight)") {
      Given("A DrawCanvasInfo that is horizontal")
      val drawCanvasInfo = stub[DrawCanvasInfoReader]
      (() => drawCanvasInfo.currentCanvasWidth).when().returns(768)
      (() => drawCanvasInfo.currentCanvasHeight).when().returns(1024)

      When("Create ViewOrientation")
      val orientation = ViewOrientation(drawCanvasInfo)

      Then("The orientation should be Vertical")
      orientation shouldBe Vertical
    }

  }
}
