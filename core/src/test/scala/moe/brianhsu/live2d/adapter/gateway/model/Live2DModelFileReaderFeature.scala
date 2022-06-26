package moe.brianhsu.live2d.adapter.gateway.model

import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.core.NativeCubismAPILoader
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.exception.TextureSizeMismatchException
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, TryValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class Live2DModelFileReaderFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory with TryValues {

  private implicit val core: NativeCubismAPILoader = new JnaNativeCubismAPILoader

  Feature("Load Live2D Model") {
    Scenario("Load a valid model") {
      Given("a mocInfoReader that load .moc3 correctly")
      val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
      val mocInfoFileReader = new MocInfoFileReader(modelFile)

      When("load a Live2D model with correct texture file list")
      val mockedTextureFiles = List("texture1.png", "texture2.png")
      val reader = new Live2DModelFileReader(mocInfoFileReader, mockedTextureFiles)
      val modelHolder = reader.loadModel()

      Then("it should return a Success[Live2DModel]")
      modelHolder shouldBe a[Success[_]]
      modelHolder.success.value shouldBe a [Live2DModel]
    }

    Scenario("The .moc3 file does not read correctly") {
      Given("a mocInfoReader that return failure when loading .moc3")
      val mocInfoReader = stub[MocInfoFileReader]
      val mockedException = new Exception("Cannot read .moc3 file")
      (mocInfoReader.loadMocInfo _).when().returning(Failure(mockedException))

      When("load a Live2D model with it")
      val reader = new Live2DModelFileReader(mocInfoReader, Nil)
      val modelHolder = reader.loadModel()

      Then("it should be a Failure contains the exception of mocInfoReader returned")
      modelHolder shouldBe a[Failure[_]]
      modelHolder.failure.exception shouldBe mockedException
    }

    Scenario("The Live2D Model backend does not pass validation") {
      Given("A mocInfoReader that load .moc3 correctly")
      val modelFile = "src/test/resources/models/HaruGreeter/runtime/haru_greeter_t03.moc3"
      val mocInfoFileReader = new MocInfoFileReader(modelFile)

      When("load a Live2D model with it but contains non-match texture file list")
      val reader = new Live2DModelFileReader(mocInfoFileReader, Nil)
      val modelHolder = reader.loadModel()

      Then("it should be a Failure because Live2DModel does not pass validation")
      modelHolder shouldBe a[Failure[_]]
      modelHolder.failure.exception shouldBe a[TextureSizeMismatchException]
    }
  }
}
