package moe.brianhsu.live2d.enitiy.model

import com.sun.jna.Memory
import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.model.drawable.{ConstantFlags, Drawable, DynamicFlags, VertexInfo}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class Live2DModelFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with MockFactory
  with TableDrivenPropertyChecks {

  private val mockedCanvasInfo = CanvasInfo(1980, 1020, (0, 0), 1)
  Feature("Use containMaskedDrawables to get whether drawable has mask or not") {
    Scenario("No drawable at all") {
      Given("A model without any drawable")
      val backend = new MockedBackend(drawables = Map.empty)
      val live2DModel = new Live2DModel(backend)

      Then("containMaskedDrawables should be false")
      live2DModel.containMaskedDrawables shouldBe false
    }

    Scenario("No drawable has mask") {
      Given("A model has three drawable, and none of them has mask")
      val mockedDrawables = Map(
        "id1" -> createDrawable("id1", index = 0),
        "id2" -> createDrawable("id2", index = 1),
        "id3" -> createDrawable("id3", index = 2),
      )
      val backend = new MockedBackend(drawables = mockedDrawables)
      val live2DModel = new Live2DModel(backend)

      Then("containMaskedDrawables should be false")
      live2DModel.containMaskedDrawables shouldBe false
    }

    Scenario("One drawable has mask") {
      Given("A model has three drawable, and one of them has mask")
      val mockedDrawables = Map(
        "id1" -> createDrawable("id1", index = 0),
        "id2" -> createDrawable("id2", index = 1, hasMask = true),
        "id3" -> createDrawable("id3", index = 2),
      )
      val backend = new MockedBackend(drawables = mockedDrawables)
      val live2DModel = new Live2DModel(backend)

      Then("containMaskedDrawables should be true")
      live2DModel.containMaskedDrawables shouldBe true
    }

    Scenario("All drawable has mask") {
      Given("A model has three drawable, and all of them has mask")
      val mockedDrawables = Map(
        "id1" -> createDrawable("id1", index = 0, hasMask = true),
        "id2" -> createDrawable("id2", index = 1, hasMask = true),
        "id3" -> createDrawable("id3", index = 2, hasMask = true),
      )
      val backend = new MockedBackend(drawables = mockedDrawables)
      val live2DModel = new Live2DModel(backend)

      Then("containMaskedDrawables should be true")
      live2DModel.containMaskedDrawables shouldBe true
    }

  }

  Feature("Get drawable sorted by index") {
    Scenario("No drawable at all") {
      Given("A model without any drawable")
      val backend = new MockedBackend(drawables = Map.empty)
      val live2DModel = new Live2DModel(backend)

      Then("the drawableByIndex should be an empty list")
      live2DModel.drawablesByIndex shouldBe Nil
    }

    Scenario("Model contains multiple drawables") {
      Given("An model 4 drawable")
      val mockedDrawables = Map(
        "id0" -> createDrawable("id0", index = 0),
        "id3" -> createDrawable("id3", index = 3),
        "id1" -> createDrawable("id1", index = 1),
        "id2" -> createDrawable("id2", index = 2),
        "id4" -> createDrawable("id4", index = 4),

      )
      val backend = new MockedBackend(drawables = mockedDrawables)
      val live2DModel = new Live2DModel(backend)

      When("get the drawable by index")
      val drawableByIndex = live2DModel.drawablesByIndex

      Then("it should have same size as the map")
      drawableByIndex.size shouldBe mockedDrawables.size

      And("they should sorted by index")
      drawableByIndex.head.id shouldBe "id0"
      drawableByIndex(1).id shouldBe "id1"
      drawableByIndex(2).id shouldBe "id2"
      drawableByIndex(3).id shouldBe "id3"
      drawableByIndex(4).id shouldBe "id4"
    }

  }

  Feature("Get drawable sorted by render order") {
    Scenario("No drawable at all") {
      Given("A model without any drawable")
      val backend = new MockedBackend(drawables = Map.empty)
      val live2DModel = new Live2DModel(backend)

      Then("the drawableByIndex should be an empty list")
      live2DModel.sortedDrawables shouldBe Nil
    }

    Scenario("Model contains multiple drawables") {
      Given("An model 4 drawable")
      val mockedDrawables = Map(
        "id0" -> createDrawable("id0", index = 0, renderOrder = 1),
        "id1" -> createDrawable("id1", index = 1, renderOrder = 0),
        "id2" -> createDrawable("id2", index = 2, renderOrder = 3),
        "id3" -> createDrawable("id3", index = 3, renderOrder = 4),
        "id4" -> createDrawable("id4", index = 4, renderOrder = 2),
      )
      val backend = new MockedBackend(drawables = mockedDrawables)
      val live2DModel = new Live2DModel(backend)

      When("get the drawable by render order")
      val drawableByRenderOrder = live2DModel.sortedDrawables

      Then("it should have same size as the map")
      drawableByRenderOrder.size shouldBe mockedDrawables.size

      And("they should sorted by index")
      drawableByRenderOrder.head.id shouldBe "id1"
      drawableByRenderOrder(1).id shouldBe "id0"
      drawableByRenderOrder(2).id shouldBe "id4"
      drawableByRenderOrder(3).id shouldBe "id2"
      drawableByRenderOrder(4).id shouldBe "id3"
    }

  }

  Feature("It should delegate access basic information / updateModel to backend") {
    Scenario("Access basic information") {
      Given("A mocked model backend")
      val mockedTextureFiles = List("texture1", "texture2")
      val mockedParameters: Map[String, Parameter] = Map("p1" -> stub[Parameter])
      val mockedParts: Map[String, Part] = Map("p1" -> stub[Part])
      val mockedDrawables: Map[String, Drawable] = Map("d1" -> stub[Drawable])
      val mockedCanvasInfo: CanvasInfo = CanvasInfo(1920, 1080, (0, 0), 1)

      val mockedBackend = new ModelBackend {
        override def textureFiles: List[String] = mockedTextureFiles
        override def parameters: Map[String, Parameter] = mockedParameters
        override def parts: Map[String, Part] = mockedParts
        override def drawables: Map[String, Drawable] = mockedDrawables
        override def canvasInfo: CanvasInfo = mockedCanvasInfo
        override def validateAllData(): Unit = canvasInfo
        override def update(): Unit = {}
      }

      And("a Live2DModel backed by that backend")
      val model = new Live2DModel(mockedBackend)

      Then("the model should delegate basic access to the model backend")
      model.textureFiles shouldBe mockedTextureFiles
      model.parameters shouldBe mockedParameters
      model.parts shouldBe mockedParts
      model.drawables shouldBe mockedDrawables
      model.canvasInfo shouldBe mockedCanvasInfo
    }

    Scenario("Update model") {
      Given("A Live2DModel backed by mocked model backend")
      val mockedBackend = new MockedBackend()
      val model = new Live2DModel(mockedBackend)

      When("update model")
      model.update()

      Then("it should delegate to mocked backend")
      mockedBackend.updatedCount shouldBe 1
    }
  }

  Feature("Snapshot and restore parameters") {
    Scenario("Only snapshot the parameters") {
      Given("A Live 2D Model with following parameters")
      val parameters = Map(
        "id1" -> new JavaVMParameter("id1", value = 0.1f),
        "id2" -> new JavaVMParameter("id2", value = 0.2f),
        "id3" -> new JavaVMParameter("id3", value = 0.3f),
      )
      val backend = new MockedBackend(parameters = parameters)
      val live2DModel = new Live2DModel(backend)

      When("snapshot the Live 2D model parameter")
      live2DModel.snapshotParameters()

      Then("the parameter itself should not be changed")
      live2DModel.parameters("id1").current shouldBe 0.1f
      live2DModel.parameters("id2").current shouldBe 0.2f
      live2DModel.parameters("id3").current shouldBe 0.3f
    }

    Scenario("There are only parameters from backend") {
      Given("A Live 2D Model with following parameters")
      val parameters = Map(
        "id1" -> new JavaVMParameter("id1", value = 0.1f),
        "id2" -> new JavaVMParameter("id2", value = 0.2f),
        "id3" -> new JavaVMParameter("id3", value = 0.3f),
      )
      val backend = new MockedBackend(parameters = parameters)
      val live2DModel = new Live2DModel(backend)

      And("Update a fallback parameter created by getParameterWithFallback")
      val fallbackParameter = live2DModel.parameterWithFallback("notExistId")
      fallbackParameter.update(0.3f)

      And("snapshot the Live 2D model parameters")
      live2DModel.snapshotParameters()

      And("Change the value of parameters after snapshot")
      parameters("id1").update(0.5f)
      parameters("id2").update(0.6f)
      parameters("id3").update(0.7f)
      fallbackParameter.update(0.8f)

      When("restore parameters")
      live2DModel.restoreParameters()

      Then("values of parameters backed by backend should be restored to the value before change")
      live2DModel.parameters("id1").current shouldBe 0.1f
      live2DModel.parameters("id2").current shouldBe 0.2f
      live2DModel.parameters("id3").current shouldBe 0.3f

      And("the fallback parameter's value should remain untouched")
      fallbackParameter.current shouldBe 0.8f

    }
  }

  Feature("Reset model") {
    Scenario("Reset a Live 2D Model") {
      Given("A Live 2D Model with following parameters with default value")
      val parameters = Map(
        "id1" -> new JavaVMParameter("id1", default = 0.6f, value = 0.1f),
        "id2" -> new JavaVMParameter("id2", default = 0.7f, value = 0.2f),
        "id3" -> new JavaVMParameter("id3", default = 0.8f, value = 0.3f),
      )
      val backend = new MockedBackend(parameters = parameters)
      val live2DModel = new Live2DModel(backend)

      When("reset the model")
      live2DModel.reset()

      Then("all of following parameters should set to default value:")
      parameters.values.foreach { parameter =>
        info(s"${parameter.id}'s value should be ${parameter.default}")
        parameter.current shouldBe parameter.default
      }

      And("it should call update on backend")
      backend.updatedCount shouldBe 1
    }
  }


  Feature("getParameterWithFallback on parameters") {
    Scenario("Get parameter that is backed by backend") {
      Given("A Live2D Model with only one parameter")
      val parameters = Map(
        "id1" -> new JavaVMParameter("id1", value = 0.1f),
      )
      val backend = new MockedBackend(parameters = parameters)
      val live2DModel = new Live2DModel(backend)

      When("get a parameter using getParameterWithFallback")
      val parameter = live2DModel.parameterWithFallback("id1")

      And("update the value of that parameter")
      parameter.update(0.5f)

      Then("the parameter of backend should also be updated")
      backend.parameters("id1").current shouldBe 0.5f
    }

    Scenario("Get parameter that is NOT backed by backend") {
      Given("A Live2D Model without any parameter")
      val backend = new MockedBackend(parameters = Map.empty)
      val live2DModel = new Live2DModel(backend)

      And("get a parameter using getParameterWithFallback")
      val parameter = live2DModel.parameterWithFallback("nonExistId")

      When("update the value of that parameter")
      parameter.update(0.5f)

      Then("we should able to get same value when call getParameterWithFallback again")
      live2DModel.parameterWithFallback("nonExistId").current shouldBe 0.5f
    }
  }

  Feature("Hit test for hit area") {
    Scenario("The provided coordinate is inside the drawable boundary") {
      Given("A Live2D Model with a drawable for hit test")
      val canvasInfo = CanvasInfo(
        widthInPixel = 2400, heightInPixel = 4500,
        originInPixel = (1200, 2250), pixelPerUnit = 2400
      )
      val drawables = Map(
        "hitArea" -> createDrawableForHitTest("hitArea")
      )
      val backend = new MockedBackend(drawables = drawables, canvasInfo = canvasInfo)
      val live2DModel = new Live2DModel(backend)

      When("The coordinate is inside the drawable boundary")
      val isHit = live2DModel.isHit("hitArea", -0.004149437f, 0.19502074f)

      Then("it should judge as a hit")
      isHit shouldBe true
    }

    Scenario("The provided coordinate is not within the drawable's boundary") {
      val invalidCombos = Table(
        ("side",         "pointX",     "pointY"),
        ("left",      -0.7572614f,  0.01659751f),
        ("right",      0.5788381f, -0.13070542f),
        ("up",      -0.010373473f,   0.9273858f),
        ("bottom",   -0.01452291f, -0.92531115f),
      )

      forAll(invalidCombos) { (side, pointX, pointY) =>
        Given("A Live2D Model with a drawable for hit test")
        val canvasInfo = CanvasInfo(
          widthInPixel = 2400, heightInPixel = 4500,
          originInPixel = (1200, 2250), pixelPerUnit = 2400
        )
        val drawables = Map(
          "hitArea" -> createDrawableForHitTest("hitArea")
        )
        val backend = new MockedBackend(drawables = drawables, canvasInfo = canvasInfo)
        val live2DModel = new Live2DModel(backend)

        When(s"The coordinate is inside the drawable's $side boundary")
        val isHit = live2DModel.isHit("hitArea", pointX, pointY)

        Then("it should judge as a non hit")
        isHit shouldBe false

      }

    }

  }


  private def createDrawableForHitTest(id: String): Drawable = {
    val vertexInfo = stub[VertexInfo]
    val boundary = List(
      (0.20498684f,0.54640603f), (-0.19323085f,0.5464755f),
      (0.2045293f,-0.46879172f), (-0.19252025f,-0.46875f)
    )

    (() => vertexInfo.positions).when().returning(boundary)

    drawable.Drawable(
      id, 0, ConstantFlags(0), DynamicFlags(null),
      textureIndex = 0, Nil,
      vertexInfo, drawOrderPointer = null,
      renderOrderPointer = null, opacityPointer = null
    )

  }

  private def createDrawable(id: String, index: Int, hasMask: Boolean = false, renderOrder: Int = 0): Drawable = {
    val masks = if (hasMask) List(1, 2, 3) else Nil

    val renderOrderPointer = new Memory(4)
    renderOrderPointer.setInt(0, renderOrder)

    drawable.Drawable(
      id, index, ConstantFlags(0), drawable.DynamicFlags(null),
      textureIndex = 0, masks,
      vertexInfo = null, drawOrderPointer = null,
      renderOrderPointer = renderOrderPointer, opacityPointer = null
    )
  }


  class MockedBackend(
    override val textureFiles: List[String] = Nil,
    override val parameters: Map[String, Parameter] = Map.empty,
    override val parts: Map[String, Part] = Map.empty,
    override val drawables: Map[String, Drawable] = Map.empty,
    override val canvasInfo: CanvasInfo = mockedCanvasInfo
  ) extends ModelBackend {
    var updatedCount: Int = 0
    override def validateAllData(): Unit = {}
    override def update(): Unit = {
      updatedCount += 1
    }
  }


}
