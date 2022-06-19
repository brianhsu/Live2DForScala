package moe.brianhsu.live2d.usecase.renderer.viewport

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewOrientation.{Horizontal, Vertical}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ViewOrientationFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  Feature("Create ViewOrientation from DrawCanvasInfo") {
    Scenario("The view is Horizontal (width == height)") {
      Given("a DrawCanvasInfo that is horizontal")
      val drawCanvasInfo = stub[DrawCanvasInfoReader]
      (() => drawCanvasInfo.currentCanvasWidth).when().returns(1024)
      (() => drawCanvasInfo.currentCanvasHeight).when().returns(1024)

      When("create ViewOrientation")
      val orientation = ViewOrientation(drawCanvasInfo)

      Then("the orientation should be Horizontal")
      orientation shouldBe Horizontal
    }

    Scenario("The view is Horizontal (width > height)") {
      Given("a DrawCanvasInfo that is horizontal")
      val drawCanvasInfo = stub[DrawCanvasInfoReader]
      (() => drawCanvasInfo.currentCanvasWidth).when().returns(1024)
      (() => drawCanvasInfo.currentCanvasHeight).when().returns(768)

      When("create ViewOrientation")
      val orientation = ViewOrientation(drawCanvasInfo)

      Then("the orientation should be Horizontal")
      orientation shouldBe Horizontal
    }

    Scenario("The view is Vertical (height > weight)") {
      Given("a DrawCanvasInfo that is horizontal")
      val drawCanvasInfo = stub[DrawCanvasInfoReader]
      (() => drawCanvasInfo.currentCanvasWidth).when().returns(768)
      (() => drawCanvasInfo.currentCanvasHeight).when().returns(1024)

      When("create ViewOrientation")
      val orientation = ViewOrientation(drawCanvasInfo)

      Then("the orientation should be Vertical")
      orientation shouldBe Vertical
    }

  }
}
