package moe.brianhsu.live2d.framework

import org.scalatest.{GivenWhenThen, Inside, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class Live2DModelFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with Inside with OptionValues {

  private val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
  private val cubism = new Cubism

  Feature("Reading canvas Info") {
    Scenario("Reading canvas info from model") {
      Given("A Live2D HaruGreeter Model")
      val model = cubism.loadModel(modelFile)

      When("Get the canvas info")
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

  Feature("Get parts data") {
    Scenario("reading parts data from model") {
      Given("A Live2D HaruGreeter Model")
      val model = cubism.loadModel(modelFile)

      When("Get the parts")
      val parts = model.parts

      Then("it should have correct number of parts")
      val expectedParts = Source.fromResource("expectation/PartIdList.txt").getLines().toList
      parts.size shouldBe expectedParts.size

      And("all expected part id should have corresponding Part object")
      expectedParts.foreach { partId =>
        val part = parts.get(partId)
        part.value shouldBe a[Part]
        part.value.id shouldBe partId
        part.value.parentIdHolder shouldBe None
        part.value.opacity shouldBe 1.0f
      }
    }
  }

}
