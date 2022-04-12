package moe.brianhsu.live2d.usecase.renderer

import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.usecase.renderer.viewport.ViewPortMatrixCalculator
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ViewPortMatrix
import org.scalatest.{GivenWhenThen, Inside}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ViewPortMatrixCalculatorFeature extends AnyFeatureSpec with GivenWhenThen with Matchers with Inside with TableDrivenPropertyChecks {
  private val IdentityMatrix = Array(
    1.0f, 0.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f, 0.0f,
    0.0f, 0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
  )

  Feature("Calculate device to screen / viewport matrix") {
    Scenario("The initial status") {
      Given("a clean ViewPortMatrixCalculator")
      val calculator = new ViewPortMatrixCalculator

      Then("the initial drawCanvasToModelMatrix should be an identity matrix")
      calculator.drawCanvasToModelMatrix.elements shouldBe IdentityMatrix

      And("the initial viewport matrix should be an identity matrix")
      inside(calculator.viewPortMatrix) { case ViewPortMatrix(screenRectangle, maxRectangle, minScale, maxScale, elements) =>
        inside(screenRectangle) { case Rectangle(leftX, bottomY, width, height) =>
          leftX shouldBe 0.0f
          bottomY shouldBe 0.0f
          width shouldBe 0.0f
          height shouldBe 0.0f
        }
        inside(maxRectangle) { case Rectangle(leftX, bottomY, width, height) =>
          leftX shouldBe 0.0f
          bottomY shouldBe 0.0f
          width shouldBe 0.0f
          height shouldBe 0.0f
        }
        minScale shouldBe 0.0f
        maxScale shouldBe 0.0f
        elements shouldBe IdentityMatrix
      }
    }

    Scenario("Update ViewPort but weight / height is zero") {

      val table = Table(
        ("width", "height"),
        (      0,        0),
        (   1024,        0),
        (      0,      768)
      )

      forAll(table) { (width, height) =>

        Given("a clean ViewPortMatrixCalculator")
        val calculator = new ViewPortMatrixCalculator

        And(s"update viewport to ($width, $height)")
        calculator.updateViewPort(width, height)

        Then("the initial drawCanvasToModelMatrix should be an identity matrix")
        calculator.drawCanvasToModelMatrix.elements shouldBe IdentityMatrix

        And("the initial viewport matrix should be an identity matrix")
        inside(calculator.viewPortMatrix) { case ViewPortMatrix(screenRectangle, maxRectangle, minScale, maxScale, elements) =>
          inside(screenRectangle) { case Rectangle(leftX, bottomY, width, height) =>
            leftX shouldBe 0.0f
            bottomY shouldBe 0.0f
            width shouldBe 0.0f
            height shouldBe 0.0f
          }
          inside(maxRectangle) { case Rectangle(leftX, bottomY, width, height) =>
            leftX shouldBe 0.0f
            bottomY shouldBe 0.0f
            width shouldBe 0.0f
            height shouldBe 0.0f
          }
          minScale shouldBe 0.0f
          maxScale shouldBe 0.0f
          elements shouldBe IdentityMatrix
        }

      }
    }

    Scenario("Calculate the matrix for horizontal viewport") {
      Given("a clean ViewPortMatrixCalculator")
      val calculator = new ViewPortMatrixCalculator

      And("Update the viewport")
      calculator.updateViewPort(width = 1024, height = 768)

      When("the drawCanvasToModelMatrix should have correct element")
      calculator.drawCanvasToModelMatrix.elements shouldBe Array(
        0.0026041667f, 0.0f, 0.0f, 0.0f,
        0.0f, -0.0026041667f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        -1.3333334f, 1.0f, 0.0f, 1.0f
      )

      And("the initial viewport matrix should be an identity matrix and has correct attributes")
      inside(calculator.viewPortMatrix) { case ViewPortMatrix(screenRectangle, maxRectangle, minScale, maxScale, elements) =>
        inside(screenRectangle) { case Rectangle(leftX, bottomY, width, height) =>
          leftX shouldBe -1.3333334f
          bottomY shouldBe 1.3333334f
          width shouldBe 2.6666667f
          height shouldBe 2.0
        }
        inside(maxRectangle) { case Rectangle(leftX, bottomY, width, height) =>
          leftX shouldBe -2.0f
          bottomY shouldBe 2.0
          width shouldBe 4.0
          height shouldBe 4.0
        }
        minScale shouldBe 0.8f
        maxScale shouldBe 2.0f
        elements shouldBe IdentityMatrix
      }
    }

    Scenario("Calculate the matrix for vertical viewport") {
      Given("a clean ViewPortMatrixCalculator")
      val calculator = new ViewPortMatrixCalculator

      And("Update the viewport")
      calculator.updateViewPort(width = 768, height = 1024)

      When("the drawCanvasToModelMatrix should have correct element")
      calculator.drawCanvasToModelMatrix.elements shouldBe Array(
        0.001953125, 0.0f, 0.0f, 0.0f,
        0.0f, -0.001953125f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        -0.75f, 1.0f, 0.0f, 1.0f
      )

      And("the initial viewport matrix should be an identity matrix and has correct attributes")
      inside(calculator.viewPortMatrix) { case ViewPortMatrix(screenRectangle, maxRectangle, minScale, maxScale, elements) =>
        inside(screenRectangle) { case Rectangle(leftX, bottomY, width, height) =>
          leftX shouldBe -0.75
          bottomY shouldBe 0.75
          width shouldBe 1.5
          height shouldBe 2.0
        }
        inside(maxRectangle) { case Rectangle(leftX, bottomY, width, height) =>
          leftX shouldBe -2.0f
          bottomY shouldBe 2.0
          width shouldBe 4.0
          height shouldBe 4.0
        }
        minScale shouldBe 0.8f
        maxScale shouldBe 2.0f
        elements shouldBe IdentityMatrix
      }
    }

  }


}
