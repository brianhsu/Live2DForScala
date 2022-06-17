package moe.brianhsu.live2d.usecase.renderer.texture

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.utils.expectation.ExpectedBitmap
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Inside}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class TextureManagerFeature extends AnyFeatureSpec with GivenWhenThen with Matchers
                            with MockFactory with Inside with FeatureSpecStackBehaviors
                            with OpenGLMock {

  Feature("Singleton by OpenGL binding") {
    Scenario("Get TextureManager twice by same OpenGL binding") {
      Given("an implicit stubbed OpenGL binding")
      implicit val binding: OpenGLBinding = createOpenGLStub()

      When("get TextureManager twice")
      val textureManager1 = TextureManager.getInstance
      val textureManager2 = TextureManager.getInstance

      Then("two texture manager should be same instance")
      textureManager1 should be theSameInstanceAs textureManager2
    }

    Scenario("Get TextureManager twice by different OpenGL binding") {
      Given("an implicit stubbed OpenGL binding")
      val binding1: OpenGLBinding = createOpenGLStub()
      val binding2: OpenGLBinding = createOpenGLStub()

      When("get TextureManager twice")
      val textureManager1 = TextureManager.getInstance(binding1)
      val textureManager2 = TextureManager.getInstance(binding2)

      Then("two texture manager should not be same instance")
      textureManager1 should not be theSameInstanceAs (textureManager2)
    }
  }

  Feature("Load Texture file into OpenGL") {
    ScenariosFor(
      loadTexture(
        fileType = "PNG (8bit RGBA)",
        textureFilename = "src/test/resources/texture/pngTexture.png",
        expectationFilename = "expectation/pngTextureBitmap.txt"
      )
    )

    ScenariosFor(
      loadTexture(
        fileType = "JPEG",
        textureFilename = "src/test/resources/texture/jpgTexture.jpg",
        expectationFilename = "expectation/jpgTextureBitmap.txt"
      )
    )

  }

}

trait FeatureSpecStackBehaviors {
  this: AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with Inside with OpenGLMock =>

  def loadTexture(fileType: String, textureFilename: String, expectationFilename: String): Unit = {
    Scenario(s"Load and cache a $fileType texture file") {

      Given("A stubbed OpenGL binding that will set mocked textureId")
      val binding = createOpenGLStub()
      val mockedTextureId = 1234
      val bitmap: Array[Byte] = new Array[Byte](1048576)

      (binding.glGenTextures _).when(*, *).onCall((_, textureIds) => textureIds(0) = mockedTextureId)
      (binding.glTexImage2D _)
        .when(*, *, *, *, *, *, *, *, *)
        .onCall { (_, _, _, _, _, _, _, _, byteBuffer) => byteBuffer.get(bitmap) }

      And("A texture manager using that binding")
      val textureManager = new TextureManager()(binding)

      When(s"Load a $fileType png file")
      val textureInfo = textureManager.loadTexture(textureFilename)

      Then("The texture info should contains the correct data")
      inside(textureInfo) { case TextureInfo(textureId, width, height) =>
        textureId shouldBe 1234
        width shouldBe 512
        height shouldBe 512
      }

      And("the bitmap buffer should have correct bytes")
      val expectedBitmap = ExpectedBitmap.getBitmap(expectationFilename)
      bitmap should contain theSameElementsInOrderAs expectedBitmap

      When(s"Load that $fileType file again")
      val textureInfo2 = textureManager.loadTexture(textureFilename)

      Then("it should be the same instance as previous TextureInfo")
      textureInfo2 should be theSameInstanceAs textureInfo

      And("the stubbed OpenGL binding should have correct calls to setup texture only once")
      import binding.openGLConstants._
      inSequence {
        (binding.glGenTextures _).verify(1, *).once()
        (binding.glBindTexture _).verify(GL_TEXTURE_2D, 1234).once()
        (binding.glTexImage2D _).verify(GL_TEXTURE_2D, 0, GL_RGBA, 512, 512, 0, GL_RGBA, GL_UNSIGNED_BYTE, *).once()
        (binding.glGenerateMipmap _).verify(GL_TEXTURE_2D).once()
        (binding.glTexParameteri _).verify(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR).once()
        (binding.glTexParameteri _).verify(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR).once()
      }
    }
  }

}
