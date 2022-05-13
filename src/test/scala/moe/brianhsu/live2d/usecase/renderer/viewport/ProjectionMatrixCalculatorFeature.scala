package moe.brianhsu.live2d.usecase.renderer.viewport

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewOrientation.{Horizontal, Vertical}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ViewPortMatrix
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Outcome, featurespec}

class ProjectionMatrixCalculatorFeature extends featurespec.FixtureAnyFeatureSpec with GivenWhenThen with Matchers with MockFactory {

  case class FixtureParam(calculator: ProjectionMatrixCalculator,
                          viewPortMatrix: ViewPortMatrix,
                          stubbedDrawCanvasInfo: StubbedDrawCanvasInfoReader,
                          stubbedCallback: ViewOrientation => Unit)


  Feature("Calculate projection matrix") {
    Scenario("Calculate projection first time") { fixtureParam =>
      val FixtureParam(calculator, viewPortMatrix, stubbedDrawCanvasInfo, stubbedCallback) = fixtureParam

      Given("a stubbed DrawCanvasInfoReader")
      stubbedDrawCanvasInfo.canvasWidth = 1920
      stubbedDrawCanvasInfo.canvasHeight = 1080

      When("Calculate the projection matrix")
      val projectMatrix = calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      Then("the element in the projection matrix should have correct")
      projectMatrix.elements shouldBe Array(
        0.5625f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
      )

      And("the callback should be called")
      (stubbedCallback.apply _).verify(Horizontal).once()
    }

    Scenario("Calculate projection twice and canvas size is not modified") { fixtureParam =>
      val FixtureParam(calculator, viewPortMatrix, stubbedDrawCanvasInfo, stubbedCallback) = fixtureParam

      Given("a stubbed DrawCanvasInfoReader")
      stubbedDrawCanvasInfo.canvasWidth = 1920
      stubbedDrawCanvasInfo.canvasHeight = 1080

      When("Calculate the projection matrix twice")
      calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)
      val projectMatrix = calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      Then("the element in the projection matrix should have correct")
      projectMatrix.elements shouldBe Array(
        0.5625f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
      )

      And("the callback should be called only once")
      (stubbedCallback.apply _).verify(Horizontal).noMoreThanOnce()
    }

    Scenario("Calculate projection twice and canvas width is modified") { fixtureParam =>
      val FixtureParam(calculator, viewPortMatrix, stubbedDrawCanvasInfo, stubbedCallback) = fixtureParam

      Given("a stubbed DrawCanvasInfoReader")
      stubbedDrawCanvasInfo.canvasWidth = 1920
      stubbedDrawCanvasInfo.canvasHeight = 1080

      When("Calculate the projection matrix first time")
      calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      And("update the width of draw canvas")
      stubbedDrawCanvasInfo.canvasWidth = 1800

      And("calculate the projection matrix again")
      val projectMatrix = calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      Then("the element in the projection matrix should have correct")
      projectMatrix.elements shouldBe Array(
        0.6f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
      )

      And("the callback should be called twice")
      (stubbedCallback.apply _).verify(Horizontal).twice()
    }

    Scenario("Calculate projection twice and canvas height is modified") { fixtureParam =>
      val FixtureParam(calculator, viewPortMatrix, stubbedDrawCanvasInfo, stubbedCallback) = fixtureParam

      Given("a stubbed DrawCanvasInfoReader")
      stubbedDrawCanvasInfo.canvasWidth = 1920
      stubbedDrawCanvasInfo.canvasHeight = 1080

      When("Calculate the projection matrix first time")
      calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      And("update the height of draw canvas")
      stubbedDrawCanvasInfo.canvasHeight = 720

      And("calculate the projection matrix again")
      val projectMatrix = calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      Then("the element in the projection matrix should have correct")
      projectMatrix.elements shouldBe Array(
        0.375f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
      )

      And("the callback should be called twice")
      (stubbedCallback.apply _).verify(Horizontal).twice()
    }

    Scenario("Calculate projection twice and canvas orientation has changed to vertical") { fixtureParam =>
      val FixtureParam(calculator, viewPortMatrix, stubbedDrawCanvasInfo, stubbedCallback) = fixtureParam

      Given("a stubbed DrawCanvasInfoReader")
      stubbedDrawCanvasInfo.canvasWidth = 1920
      stubbedDrawCanvasInfo.canvasHeight = 1080

      When("Calculate the projection matrix first time")
      calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      And("update the height of draw canvas")
      stubbedDrawCanvasInfo.canvasWidth = 1080
      stubbedDrawCanvasInfo.canvasHeight = 1920

      And("calculate the projection matrix again")
      val projectMatrix = calculator.calculate(viewPortMatrix, isForceUpdate = false, stubbedCallback)

      Then("the element in the projection matrix should have correct")
      projectMatrix.elements shouldBe Array(
        1.7777778f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
      )

      And("the callback should be called twice, first is Horizontal")
      (stubbedCallback.apply _).verify(Horizontal).once()

      And("second is Vertical")
      (stubbedCallback.apply _).verify(Vertical).once()
    }

  }

  class StubbedDrawCanvasInfoReader(var canvasWidth: Int, var canvasHeight: Int) extends DrawCanvasInfoReader {
    override def currentCanvasWidth: Int = canvasWidth
    override def currentCanvasHeight: Int = canvasHeight
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val stubbedDrawCanvasInfoReader = new StubbedDrawCanvasInfoReader(0, 0)
    val calculator = new ProjectionMatrixCalculator(stubbedDrawCanvasInfoReader)
    val stubbedCallback = stub[ViewOrientation => Unit]
    val viewPortMatrix = new ViewPortMatrix(
      screenRectangle = Rectangle(-1.966736f, 1.966736f, 3.933472f, 2.0f),
      maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
      minScale = 0.8f, maxScale = 2.0f,
      Array(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
      )
    )

    val fixture = FixtureParam(
      calculator,
      viewPortMatrix,
      stubbedDrawCanvasInfoReader,
      stubbedCallback
    )
    withFixture(test.toNoArgTest(fixture))
  }
}
