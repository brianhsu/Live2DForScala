package moe.brianhsu.live2d.enitiy.opengl.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.{TextureColor, TextureInfo}
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class SpriteFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock with TableDrivenPropertyChecks {


  Feature("Default sprite color") {
    Scenario("Get sprite color from a Sprite that is not override default value") {
      Given("a stubbed Sprite")
      val stubbedTextureInfo = TextureInfo(0, 24, 24)
      val sprite = new Sprite(stub[DrawCanvasInfoReader], stubbedTextureInfo) {
        override protected def calculatePositionAndSize(): Rectangle = Rectangle(0, 0, 0, 0)
      }

      When("ask for the sprite color")
      val textureColor = sprite.spriteColor

      Then("it should have default value TextureColor(1.0f, 1.0f, 1.0f, 1.0f)")
      textureColor shouldBe TextureColor(1.0f, 1.0f, 1.0f, 1.0f)

    }

  }

  Feature("Calculate position") {
    Scenario("calculate position twice and has different position") {
      Given("a stubbed Sprite that return different position when calculating position")
      val stubbedTextureInfo = TextureInfo(0, 24, 24)
      val mockedPosition = List(
        Rectangle(0, 0, 1024, 768),
        Rectangle(500, 400, 300, 200)
      )
      var calculateTimes: Int = -1
      val sprite = new Sprite(stub[DrawCanvasInfoReader], stubbedTextureInfo) {
        override protected def calculatePositionAndSize(): Rectangle = {
          println("calculatePositionAndSize:" + calculateTimes)
          calculateTimes += 1
          mockedPosition(calculateTimes)
        }
      }

      When("ask for position / size immediately after sprite is created")
      Then(s"it should be ${mockedPosition.head}")
      sprite.positionAndSize shouldBe mockedPosition.head

      And("resize it")
      sprite.resize()

      Then(s"the position / size should be ${mockedPosition(1)}")
      sprite.positionAndSize shouldBe mockedPosition(1)
    }

  }

  Feature("Check if a point is hitting inside the sprite") {
    Scenario("The point is inside sprite") {
      val testData = Table(
        ("pointX", "pointY"),
        (500.0f, 600.0f),           // Top left limit
        (560.0f, 600.0f),           // Bottom right limit
        (500.0f, 530.0f),           // Bottom left limit
        (560.0f, 530.0f),           // Top right limit
        (550.0f, 550.0f)            // Inside sprite
      )
      forAll(testData) { case (pointX, pointY) =>
        Given("a canvas that has height 1024")
        val canvasInfoReader = stub[DrawCanvasInfoReader]
        (() => canvasInfoReader.currentCanvasHeight).when().returns(1000)

        And("a sprite that at (500, 400) position and has size (60, 70)")
        val stubbedTextureInfo = TextureInfo(0, 24, 24)
        val sprite = new Sprite(canvasInfoReader, stubbedTextureInfo) {
          override protected def calculatePositionAndSize(): Rectangle = Rectangle(500, 400, 60, 70)
        }

        When(s"ask if ($pointX, $pointY) is inside the sprite")
        Then("it should be true")
        sprite.isHit(pointX, pointY) shouldBe true

      }

    }
    Scenario("The point is NOT inside sprite") {
      val testData = Table(
        ("pointX", "pointY"),
        (499.0f, 600.0f),     // Exceed top left limit
        (560.0f, 601.0f),     // Exceed bottom right limit
        (500.0f, 529.0f),     // Exceed bottom left limit
        (560.0f, 529.0f),     // Exceed top right limit
      )
      forAll(testData) { case (pointX, pointY) =>
        Given("a canvas that has height 1024")
        val canvasInfoReader = stub[DrawCanvasInfoReader]
        (() => canvasInfoReader.currentCanvasHeight).when().returns(1000)

        And("a sprite that at (500, 400) position and has size (60, 70)")
        val stubbedTextureInfo = TextureInfo(0, 24, 24)
        val sprite = new Sprite(canvasInfoReader, stubbedTextureInfo) {
          override protected def calculatePositionAndSize(): Rectangle = Rectangle(500, 400, 60, 70)
        }

        When(s"ask if ($pointX, $pointY) is inside the sprite")
        Then("it should be false")
        sprite.isHit(pointX, pointY) shouldBe false

      }

    }

  }

}
