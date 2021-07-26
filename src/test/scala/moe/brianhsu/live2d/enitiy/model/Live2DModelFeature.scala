package moe.brianhsu.live2d.enitiy.model

import com.sun.jna.Memory
import moe.brianhsu.porting.live2d.framework.model.{CanvasInfo, Part}
import moe.brianhsu.porting.live2d.framework.model.drawable.{ConstantFlags, Drawable, DynamicFlags}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class Live2DModelFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("Use containMaskedDrawables to get whether drawable has mask or not") {
    Scenario("No drawable at all") {
      Given("A model without any drawable")
      val live2DModel = new MockedLive2DModel(drawables = Map.empty)

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
      val live2DModel = new MockedLive2DModel(drawables = mockedDrawables)

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
      val live2DModel = new MockedLive2DModel(drawables = mockedDrawables)

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
      val live2DModel = new MockedLive2DModel(drawables = mockedDrawables)

      Then("containMaskedDrawables should be true")
      live2DModel.containMaskedDrawables shouldBe true
    }

  }

  Feature("Get drawable sorted by index") {
    Scenario("No drawable at all") {
      Given("A model without any drawable")
      val live2DModel = new MockedLive2DModel(drawables = Map.empty)

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
      val live2DModel = new MockedLive2DModel(drawables = mockedDrawables)

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
      val live2DModel = new MockedLive2DModel(drawables = Map.empty)

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
      val live2DModel = new MockedLive2DModel(drawables = mockedDrawables)

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

  private def createDrawable(id: String, index: Int, hasMask: Boolean = false, renderOrder: Int = 0): Drawable = {
    val masks = if (hasMask) List(1, 2, 3) else Nil

    val renderOrderPointer = new Memory(4)
    renderOrderPointer.setInt(0, renderOrder)

    Drawable(
      id, index, ConstantFlags(0), DynamicFlags(null),
      textureIndex = 0, masks,
      vertexInfo = null, drawOrderPointer = null,
      renderOrderPointer = renderOrderPointer, opacityPointer = null
    )
  }

  class MockedLive2DModel(
    override val textureFiles: List[String] = Nil,
    override val parameters: Map[String, Parameter] = Map.empty,
    override val parts: Map[String, Part] = Map.empty,
    override val drawables: Map[String, Drawable] = Map.empty
  ) extends Live2DModel {
    override val validateAllData: Live2DModel = this
    override def canvasInfo: CanvasInfo = ???
    override def update(): Unit = ???
  }
}
