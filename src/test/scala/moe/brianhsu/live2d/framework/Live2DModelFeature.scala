package moe.brianhsu.live2d.framework

import org.scalatest.{GivenWhenThen, Inside}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class Live2DModelFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with Inside {
  private val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
  private val cubism = new Cubism

  Feature("Reading canvas Info") {
    Scenario("Reading canvas info from model") {
      Given("A Live2D HaruGreeter Model")
      val model = cubism.loadModel(modelFile)

      When("Query the canvas info")
      val canvasInfo = model.canvasInfo

      Then("it should return the correct canvas info")
      inside(canvasInfo) { case CanvasInfo(widthInPixel, heightInPixel, (originX, originY), pixelPerUnit) =>
        widthInPixel shouldBe 2400
        heightInPixel shouldBe 4500
        originX shouldBe 1200
        originY shouldBe 2250
        pixelPerUnit shouldBe 2400
      }
    }
  }

}
