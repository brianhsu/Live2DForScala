package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureInfo
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SpriteShader
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import java.nio.FloatBuffer

class SpriteRenderFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock with TableDrivenPropertyChecks {

  private val StubbedTextureId = 1
  private val StubbedProgramId = 2
  private val StubbedPositionLocation = 3
  private val StubbedUvLocation = 4
  private val StubbedTextureLocation = 5
  private val StubbedBaseColorLocation = 6

  Feature("Render sprite") {
    Scenario("Render a dummy sprite") {
      Given("a stubbed Sprite")
      implicit val binding: OpenGLBinding = createOpenGLStub()

      val stubbedTextureInfo = TextureInfo(StubbedTextureId, 24, 24)
      val stubbedShader = new SpriteShader() {
        override val programId: Int = StubbedProgramId
        override val positionLocation: Int = StubbedPositionLocation
        override val uvLocation: Int = StubbedUvLocation
        override val textureLocation: Int = StubbedTextureLocation
        override val baseColorLocation: Int = StubbedBaseColorLocation
      }
      val canvasInfoReader = stub[DrawCanvasInfoReader]
      (() => canvasInfoReader.currentCanvasWidth).when().returns(1024)
      (() => canvasInfoReader.currentCanvasHeight).when().returns(720)

      val sprite = new Sprite(canvasInfoReader, stubbedTextureInfo) {
        override protected def calculatePositionAndSize(): Rectangle = Rectangle(10, 20, 30, 40)
      }

      And("create a SpriteRenderer")
      val spriteRenderer = new SpriteRenderer(stubbedShader)
      val expectedPositionVertex = Array(
        -0.921875f, -0.8333333f, -0.98046875f, -0.8333333f,
        -0.98046875f, -0.9444444f, -0.921875f, -0.9444444f
      )
      val expectedUvVertex = Array(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 1.0f, 1.0f
      )

      val stubbedPositionBuffer = FloatBuffer.wrap(expectedPositionVertex)
      val stubbedUvBuffer = FloatBuffer.wrap(expectedUvVertex)

      (binding.newDirectFloatBuffer _)
        .when(where{x: Array[Float] => x sameElements expectedPositionVertex})
        .returns(stubbedPositionBuffer)

      (binding.newDirectFloatBuffer _)
        .when(where{x: Array[Float] => x sameElements expectedUvVertex})
        .returns(stubbedUvBuffer)

      When("draw the sprite")
      spriteRenderer.drawSprite(sprite)

      Then("it should delegate it to underlay OpenGL binding")
      inSequence {
        import binding.constants._

        (binding.glUseProgram _).verify(StubbedProgramId).once()
        (binding.glEnable _).verify(GL_TEXTURE_2D).once()

        (binding.glEnableVertexAttribArray _).verify(StubbedPositionLocation).once()
        (binding.glEnableVertexAttribArray _).verify(StubbedUvLocation).once()
        (binding.glUniform1i _).verify(StubbedTextureLocation, 0).once()

        (binding.glVertexAttribPointer: (Int, Int, Int, Boolean, Int, FloatBuffer) => Unit).verify(StubbedPositionLocation, 2, GL_FLOAT, false, 0, stubbedPositionBuffer).once()
        (binding.glVertexAttribPointer: (Int, Int, Int, Boolean, Int, FloatBuffer) => Unit).verify(StubbedUvLocation, 2, GL_FLOAT, false, 0, stubbedUvBuffer).once()

        (binding.glUniform4f _).verify(StubbedBaseColorLocation, 1.0f, 1.0f, 1.0f, 1.0f).once()
        (binding.glBindTexture _).verify(GL_TEXTURE_2D, StubbedTextureId).once()
        (binding.glDrawArrays _).verify(GL_TRIANGLE_FAN, 0, 4).once()
      }


    }

  }
}
