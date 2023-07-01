package moe.brianhsu.live2d.usecase.renderer.opengl.clipping

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.model.drawable.Drawable.ColorFetcher
import moe.brianhsu.live2d.enitiy.model.drawable.{ConstantFlags, Drawable, DrawableColor, DynamicFlags, VertexInfo}
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureColor
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext.Layout
import org.scalamock.scalatest.MockFactory
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{GivenWhenThen, Inside, OptionValues}

class ClippingContextFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with OptionValues
                             with Inside with TableDrivenPropertyChecks with MockFactory {
  Feature("Layout and channel color") {
    Scenario("Get channel count") {
      When("Ask for channel count")
      val channelCount  = ClippingContext.ColorChannelCount

      Then("it should be 4")
      channelCount shouldBe 4
    }

    Scenario("Get channel color of Layout") {
      val testData = Table(
        ("channelNo", "expectedResult"),
        (0, TextureColor(1.0f, 0.0f, 0.0f, 0.0f)),
        (1, TextureColor(0.0f, 1.0f, 0.0f, 0.0f)),
        (2, TextureColor(0.0f, 0.0f, 1.0f, 0.0f)),
        (3, TextureColor(0.0f, 0.0f, 0.0f, 1.0f)),
      )

      forAll(testData) { case (channelNo, expectedResult) =>
        Given(s"A layout with channel $channelNo")
        val layout = Layout(channelNo, Rectangle())

        When("When ask for channel color")
        val channelColor = layout.channelColor

        Then(s"it should be $expectedResult")
        channelColor shouldBe expectedResult
      }
    }

  }

  Feature("Check if is in using") {
    Scenario("There is no clipped draw bounds") {
      Given("a ClippingContext without clipped draw bounds")
      val clippingContext = new ClippingContext(maskDrawable = Nil, clippedDrawables = Nil, allClippedDrawableBounds = None)

      When("ask for isUsing")
      val isUsing = clippingContext.isUsing

      Then("it should be false")
      isUsing shouldBe false
    }

    Scenario("There are some clipped draw bounds") {
      Given("a ClippingContext with a clipped draw bounds")
      val clippingContext = new ClippingContext(
        maskDrawable = Nil,
        clippedDrawables = Nil,
        allClippedDrawableBounds = Some(Rectangle(0, 0, 10, 20))
      )

      When("ask for isUsing")
      val isUsing = clippingContext.isUsing

      Then("it should be true")
      isUsing shouldBe true
    }

  }

  Feature("Calculate the matrixForMask / matrixForDraw") {
    Scenario("When there is no allClippedDrawRect") {
      Given("a ClippingContext without clipped draw bounds")
      val clippingContext = new ClippingContext(maskDrawable = Nil, clippedDrawables = Nil, allClippedDrawableBounds = None)

      When("calculate the matrix on that context")
      val newContext = clippingContext.calculateMatrix()

      Then("the returned context should not be the same object as clippingContext")
      newContext should not be theSameInstanceAs (clippingContext)

      And("the matrixForDraw in newContext should be all of NaN")
      newContext.matrixForDraw.elements.forall(_.isNaN) shouldBe true
      newContext.matrixForMask.elements.forall(_.isNaN) shouldBe true

    }

    Scenario("When there is allClippedDrawRect") {
      val testData = Table(
        ("width", "height", "expectedMatrixForDraw", "expectedMatrixForMask"),
        (0.0f, 0.0f, List(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f), List(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f)),
        (1920.0f, 0.0f, List(0.90909094f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 87.27273f, 0.0f, 0.0f, 1.0f), List(1.8181819f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 173.54546f, -1.0f, 0.0f, 1.0f)),
        (1920.0f, 1080.0f, List(0.90909094f, 0.0f, 0.0f, 0.0f, 0.0f, 0.90909094f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 87.27273f, 49.09091f, 0.0f, 1.0f), List(1.8181819f, 0.0f, 0.0f, 0.0f, 0.0f, 1.8181819f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 173.54546f, 97.18182f, 0.0f, 1.0f)),
      )

      forAll(testData) { case (width, height, expectedMatrixForDraw, expectedMatrixForMask) =>
        Given("a ClippingContext with a clipped draw bounds with layout bounds width=0, height=0")
        val clippingContext = new ClippingContext(
          maskDrawable = Nil,
          clippedDrawables = Nil,
          allClippedDrawableBounds = Some(Rectangle(0, 0, 1920, 1080)),
          layout = Layout(0, Rectangle(0, 0, width, height))
        )

        When("calculate the matrix on that context")
        val newContext = clippingContext.calculateMatrix()

        Then("the returned context should not be the same object as clippingContext")
        newContext should not be theSameInstanceAs (clippingContext)

        And("the matrixForDraw in newContext should be")
        newContext.matrixForDraw.elements should contain theSameElementsInOrderAs expectedMatrixForDraw
        newContext.matrixForMask.elements should contain theSameElementsInOrderAs expectedMatrixForMask
      }
    }
  }

  Feature("Calculate all clipped drawable bounds") {
    Scenario("There is no clipped drawable") {
      Given("a ClippingContext without any clipped drawables")
      val clippingContext = new ClippingContext(maskDrawable = Nil, clippedDrawables = Nil)

      When("calculate the all clipped drawable bounds")
      val newContext = clippingContext.calculateAllClippedDrawableBounds()

      Then("the returned context should not be the same object as clippingContext")
      newContext should not be theSameInstanceAs(clippingContext)

      And("the allClippedDrawableBounds should be None")
      newContext.allClippedDrawableBounds shouldBe None
    }

    Scenario("All position in vertexInfo are positive") {
      Given("a ClippingContext without some clipped drawables")
      val clippingContext = new ClippingContext(
        maskDrawable = Nil,
        clippedDrawables = List(
          createStubbedDrawable((10.2f, 3.4f) :: (0.0f, 22.0f) :: (32.0f, 0.0f) :: Nil),
          createStubbedDrawable((13.2f, 23.4f) :: (50.0f, 21.0f) :: (42.0f, 0.0f) :: Nil),
        )
      )
      When("calculate the all clipped drawable bounds")
      val newContext = clippingContext.calculateAllClippedDrawableBounds()

      Then("the returned context should not be the same object as clippingContext")
      newContext should not be theSameInstanceAs(clippingContext)

      And("the allClippedDrawableBounds should have correct value")
      newContext.allClippedDrawableBounds.value shouldBe Rectangle(0.0f, 0.0f, 50.0f, 23.4f)

    }

    Scenario("All position in vertexInfo are negative") {
      Given("a ClippingContext without some clipped drawables")
      val clippingContext = new ClippingContext(
        maskDrawable = Nil,
        clippedDrawables = List(
          createStubbedDrawable((-10.2f, -3.4f) :: (-3.0f, -22.0f) :: (-32.0f, -10.0f) :: Nil),
          createStubbedDrawable((-13.2f, -23.4f) :: (-50.0f, -21.0f) :: (-42.0f, -30.0f) :: Nil),
        )
      )
      When("calculate the all clipped drawable bounds")
      val newContext = clippingContext.calculateAllClippedDrawableBounds()

      Then("the returned context should not be the same object as clippingContext")
      newContext should not be theSameInstanceAs(clippingContext)

      And("the allClippedDrawableBounds should have correct value")
      newContext.allClippedDrawableBounds.value shouldBe Rectangle(-50.0f, -30.0f, 50.0f, 30.0f)

    }

    Scenario("Positions in vertexInfo has some positive and some negative numbers") {
      Given("a ClippingContext without some clipped drawables")
      val clippingContext = new ClippingContext(
        maskDrawable = Nil,
        clippedDrawables = List(
          createStubbedDrawable((-10.2f, -3.4f) :: (-3.0f, -22.0f) :: (-32.0f, 10.0f) :: Nil),
          createStubbedDrawable((13.2f, 23.4f) :: (50.0f, -21.0f) :: (-42.0f, -30.0f) :: Nil),
        )
      )
      When("calculate the all clipped drawable bounds")
      val newContext = clippingContext.calculateAllClippedDrawableBounds()

      Then("the returned context should not be the same object as clippingContext")
      newContext should not be theSameInstanceAs(clippingContext)

      And("the allClippedDrawableBounds should have correct value")
      newContext.allClippedDrawableBounds.value shouldBe Rectangle(-42.0f, -30.0f, 92.0f, 53.4f)

    }

  }

  Feature("Get vertex position changed drawable") {
    Scenario("There is no mask drawable at all") {
      Given("a ClippingContext without any mask drawables")
      val clippingContext = new ClippingContext(maskDrawable = Nil, clippedDrawables = Nil)

      When("request the vertex position changed drawable")
      val drawables = clippingContext.vertexPositionChangedMaskDrawable

      Then("it should be Nil")
      drawables shouldBe Nil

    }

    Scenario("All mask drawable are not changed") {
      Given("a ClippingContext with some mask drawables that does have any position changed flag")
      val originalMaskDrawables = List(
        createStubbedDrawable("1", isVertexPositionChanged = false),
        createStubbedDrawable("2", isVertexPositionChanged = false),
        createStubbedDrawable("3", isVertexPositionChanged = false),
      )
      val clippingContext = new ClippingContext(
        maskDrawable = originalMaskDrawables,
        clippedDrawables = Nil
      )

      When("request the vertex position changed drawable")
      val drawables = clippingContext.vertexPositionChangedMaskDrawable

      Then("it should be Nil")
      drawables shouldBe Nil

    }
    Scenario("Some mask drawable are changed") {
      Given("a ClippingContext with some mask drawables, and some of them are changed")
      val originalMaskDrawables = List(
        createStubbedDrawable("1", isVertexPositionChanged = false),
        createStubbedDrawable("2", isVertexPositionChanged = true),
        createStubbedDrawable("3", isVertexPositionChanged = true),
        createStubbedDrawable("4", isVertexPositionChanged = false),
      )
      val clippingContext = new ClippingContext(
        maskDrawable = originalMaskDrawables,
        clippedDrawables = Nil
      )

      When("request the vertex position changed drawable")
      val drawables = clippingContext.vertexPositionChangedMaskDrawable

      Then("it should be the second and the third elements in original drawables")
      drawables shouldBe List(originalMaskDrawables(1), originalMaskDrawables(2))

    }

  }

  private val mockedFetcher: ColorFetcher = () => DrawableColor(1.0f, 1.0f, 1.0f, 1.0f)

  private def createStubbedDrawable(vertexPositions: List[(Float, Float)]): Drawable = {
    val vertexInfo = stub[VertexInfo]
    (() => vertexInfo.positions).when().returns(vertexPositions)
    Drawable("1", 0, None, ConstantFlags(0), new DynamicFlags(null), 0, Nil, vertexInfo, null, null, null, mockedFetcher, mockedFetcher)
  }

  private def createStubbedDrawable(id: String, isVertexPositionChanged: Boolean): Drawable = {
    val dynamicFlags = stub[DynamicFlags]
    (() => dynamicFlags.vertexPositionChanged).when().returns(isVertexPositionChanged)
    Drawable(id, 0, None, ConstantFlags(0), dynamicFlags, 0, Nil, null, null, null, null, mockedFetcher, mockedFetcher)
  }

}
