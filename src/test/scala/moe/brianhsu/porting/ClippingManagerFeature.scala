package moe.brianhsu.porting

import moe.brianhsu.live2d.boundary.gateway.avatar.ModelBackend
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingManager
import moe.brianhsu.utils.mock.Live2DModelMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, OptionValues}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ClippingManagerFeature extends AnyFeatureSpec with Matchers with GivenWhenThen
                             with MockFactory with Live2DModelMock with OptionValues with TableDrivenPropertyChecks {

  Feature("Load ClippingManager from Live2D model") {
    Scenario("There is no drawable contains masks") {
      Given("a Live2D Model without drawable contains masks")
      val modelBackend = stub[ModelBackend]
      val live2DModel = new Live2DModel(modelBackend) {
        override lazy val containMaskedDrawables: Boolean = false
      }

      When("create a clipping manager from it")
      val managerHolder = ClippingManager.fromLive2DModel(live2DModel)

      Then("it should be None")
      managerHolder shouldBe None
    }

    Scenario("There are drawable contains masks") {
      Given("a Live2D Model some drawables contains masks")
      val mockedDrawables = Map(
        "id1" -> createDrawable("id1", index = 0, Nil),
        "id2" -> createDrawable("id2", index = 1, Nil),
        "id3" -> createDrawable("id3", index = 2, List(1, 0)),
        "id4" -> createDrawable("id4", index = 3, List(0, 1)),
        "id5" -> createDrawable("id5", index = 4, List(1)),
      )
      val backend = new MockedBackend(drawables = mockedDrawables)
      val live2DModel = new Live2DModel(backend)

      When("create a clipping manager from it")
      val manager = ClippingManager.fromLive2DModel(live2DModel).value

      Then("it should have correct value")
      manager.usingClipCount shouldBe 0
      manager.contextListForMask.size shouldBe 2
      manager.contextListForMask(0).maskDrawable.map(_.id) should contain theSameElementsInOrderAs List("id1", "id2")
      manager.contextListForMask(0).clippedDrawables.map(_.id) should contain theSameElementsAs List("id3", "id4")
      manager.contextListForMask(1).maskDrawable.map(_.id) should contain theSameElementsInOrderAs List("id2")
      manager.contextListForMask(1).clippedDrawables.map(_.id) should contain theSameElementsAs List("id5")
    }

  }

  Feature("Get ClippingContext by drawable") {
    Scenario("There is no clipping context that has queried drawable inside clippedDrawables member field") {
      val testData = Table(
        "queryDrawableId",
        "id1",
        "id2"
      )

      forAll(testData) { queryDrawableId =>
        Given("a Live2D Model some drawables contains masks")
        val mockedDrawables = Map(
          "id1" -> createDrawable("id1", index = 0, Nil),
          "id2" -> createDrawable("id2", index = 1, Nil),
          "id3" -> createDrawable("id3", index = 2, List(1, 0)),
          "id4" -> createDrawable("id4", index = 3, List(0, 1)),
          "id5" -> createDrawable("id5", index = 4, List(1)),
        )
        val backend = new MockedBackend(drawables = mockedDrawables)
        val live2DModel = new Live2DModel(backend)

        And("create a clipping manager from it")
        val manager = ClippingManager.fromLive2DModel(live2DModel).value

        When("query ClippingContext with drawable[id0]")
        val clippingContextHolder = manager.getClippingContextByDrawable(mockedDrawables(queryDrawableId))

        Then("it should be None")
        clippingContextHolder shouldBe None
      }
    }

    Scenario("There is clipping context that has queried drawable inside clippedDrawables member field") {
      val testData = Table(
        ("queryDrawableId", "expectedClippingContextIndex"),
        (            "id3",                              0),
        (            "id4",                              0),
        (            "id5",                              1),
      )

      forAll(testData) { case (queryDrawableId, expectedClippingContextIndex) =>
        Given("a Live2D Model some drawables contains masks")
        val mockedDrawables = Map(
          "id1" -> createDrawable("id1", index = 0, Nil),
          "id2" -> createDrawable("id2", index = 1, Nil),
          "id3" -> createDrawable("id3", index = 2, List(1, 0)),
          "id4" -> createDrawable("id4", index = 3, List(0, 1)),
          "id5" -> createDrawable("id5", index = 4, List(1)),
        )
        val backend = new MockedBackend(drawables = mockedDrawables)
        val live2DModel = new Live2DModel(backend)

        And("create a clipping manager from it")
        val manager = ClippingManager.fromLive2DModel(live2DModel).value

        When(s"query ClippingContext with drawable[$queryDrawableId]")
        val clippingContext = manager.getClippingContextByDrawable(mockedDrawables(queryDrawableId)).value

        Then("it should be None")
        clippingContext shouldBe manager.contextListForMask(expectedClippingContextIndex)

      }
    }

  }

}
