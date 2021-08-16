package moe.brianhsu.live2d.enitiy.math.matrix

import moe.brianhsu.porting.live2d.framework.math.Rectangle
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ModelMatrixFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("The basic attributes") {
    Scenario("Access the basic attributes") {
      Given("A model matrix with predefined elements")
      val matrix = new ModelMatrix(
        canvasWidth = 1.5f, canvasHeight = 1.875f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )
      When("request the basic attributes from matrix")
      val width = matrix.canvasWidth
      val height = matrix.canvasHeight
      val xScalar = matrix.xScalar
      val yScalar = matrix.yScalar
      val xTranslator = matrix.xOffset
      val yTranslator = matrix.yOffset

      Then("those attribute should have correct values")
      width shouldBe 1.5f
      height shouldBe 1.875f
      xScalar shouldBe 0.0f
      yScalar shouldBe 0.5f
      xTranslator shouldBe 1.2f
      yTranslator shouldBe 1.3f
    }
  }

  Feature("Calculate the transformed X value") {
    Scenario("Calculate transformed X") {
      Given("A matrix that scale X with 1.5")
      And("translate X with 2.5")
      val matrix = new ModelMatrix(canvasWidth = 1.5f, canvasHeight = 1.875f)
        .scale(xScalar = 1.5f, yScalar = 1.0f)
        .translateX(2.5f)

      When("calculate transformed X of 123.5")
      val transformedX = matrix.transformedX(123.5f)

      Then("it should be 123.5 * 1.5 + 2.5")
      transformedX shouldBe (123.5 * 1.5 + 2.5)
    }

    Scenario("Invert the transformed X to origin X") {
      Given("A matrix that scale X with 1.5")
      And("translate X with 2.5")
      val matrix = new ModelMatrix(canvasWidth = 1.5f, canvasHeight = 1.875f)
        .scale(xScalar = 1.5f, yScalar = 1.0f)
        .translateX(2.5f)

      When("invert the transformed X 187.75")
      val originalX = matrix.invertedTransformedX(187.75f)

      Then("it should be 123.5")
      originalX shouldBe 123.5
    }

  }

  Feature("Calculate the transformed Y value") {
    Scenario("Calculate transformed X") {
      Given("A matrix that scale Y with 1.5")
      And("translate Y with 2.5")
      val matrix = new ModelMatrix(canvasWidth = 1.5f, canvasHeight = 1.875f)
        .scale(xScalar = 1.0f, yScalar = 1.5f)
        .translateY(2.5f)

      When("calculate transformed Y of 123.5")
      val transformedY = matrix.transformedY(123.5f)

      Then("it should be 123.5 * 1.5 + 2.5")
      transformedY shouldBe (123.5 * 1.5 + 2.5)
    }

    Scenario("Invert the transformed Y to origin Y") {
      Given("A matrix that scale Y with 1.5")
      And("translate Y with 2.5")
      val matrix = new ModelMatrix(canvasWidth = 1.5f, canvasHeight = 1.875f)
        .scale(xScalar = 1.0f, yScalar = 1.5f)
        .translateY(2.5f)

      When("invert the transformed Y 187.75")
      val originalY = matrix.invertedTransformedY(187.75f)

      Then("it should be 123.5")
      originalY shouldBe 123.5
    }

  }

  Feature("Multiple with another matrix") {
    Scenario("Multiple by an identity matrix") {
      Given("An identity matrix")
      val identityMatrix = new ModelMatrix(canvasWidth = 1.5f, canvasHeight = 1.875f)

      And("a model matrix")
      val matrix = new ModelMatrix(
        canvasWidth = 1.5f, canvasHeight = 1.875f,
        Array(
          0.1f, 0.2f, 0.3f, 0.4f,
          0.5f, 0.6f, 0.7f, 0.8f,
          0.9f, 1.0f, 1.1f, 1.2f,
         1.3f, 1.4f, 1.5f, 1.6f
        )
      )

      When("multiple them")
      val updatedMatrix = matrix * identityMatrix

      Then("the elements in the updated matrix should be the same as original matrix")
      updatedMatrix.elements should contain theSameElementsInOrderAs Array(
        0.1f, 0.2f, 0.3f, 0.4f,
        0.5f, 0.6f, 0.7f, 0.8f,
        0.9f, 1.0f, 1.1f, 1.2f,
        1.3f, 1.4f, 1.5f, 1.6f
      )
    }

    Scenario("Multiple two normal matrix") {
      Given("Two model matrix")
      val matrix1 = new ModelMatrix(
        canvasWidth = 1.5f, canvasHeight = 1.875f,
        Array(
         1.1f, 2.2f, 3.3f, 4.4f,
         5.5f, 6.6f, 7.7f, 8.8f,
         9.9f, 1.0f, 2.1f, 3.2f,
         4.3f, 5.4f, 6.5f, 7.6f
        )
      )
      val matrix2 = new ModelMatrix(
        canvasWidth = 1.5f, canvasHeight = 1.875f,
        Array(
          0.1f, 0.2f, 0.3f, 0.4f,
          0.5f, 0.6f, 0.7f, 0.8f,
          0.9f, 1.0f, 1.1f, 1.2f,
          1.3f, 1.4f, 1.5f, 1.6f
        )
      )

      When("multiple them")
      val updatedMatrix = matrix1 * matrix2

      Then("the elements in the updated matrix should be the same as original matrix")
      updatedMatrix.elements should contain theSameElementsInOrderAs Array(
        9.9f, 11.0f, 12.1f, 13.200001f,
        22.22f, 25.08f, 27.94f, 30.800003f,
        7.54f, 9.16f, 10.780001f, 12.4f,
        18.859999f, 21.24f, 23.619999f, 26.0f
      )

    }
  }

  Feature("Create updated matrix instance") {

    Scenario("Create a matrix by update x/y axis position directly") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update left position")
      val updatedMatrix = matrix.position(1.5f, 2.5f)

      Then("the updated matrix should have xOffset=1.5 / yOffset=2.5")
      updatedMatrix.xOffset shouldBe 1.5f
      updatedMatrix.yOffset shouldBe 2.5f

      And("other attribute should be the same")
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update left position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update left position")
      val updatedMatrix = matrix.left(1.5f)

      Then("the updated matrix should have xOffset 12.5f")
      updatedMatrix.xOffset shouldBe 1.5f

      And("other attribute should be the same")
      updatedMatrix.yOffset shouldBe 0
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update right position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(canvasWidth = 1.5f, canvasHeight = 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update left position")
      val updatedMatrix = matrix.right(2f)

      Then("the updated matrix should have xOffset 0.3499999")
      updatedMatrix.xOffset shouldBe 0.3499999f

      And("other attribute should be the same")
      updatedMatrix.yOffset shouldBe 0
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update top position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update top position")
      val updatedMatrix = matrix.top(1.5f)

      Then("the updated matrix should have yOffset 1.5f")
      updatedMatrix.yOffset shouldBe 1.5f

      And("other attribute should be the same")
      updatedMatrix.xOffset shouldBe 0
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update bottom position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update bottom position")
      val updatedMatrix = matrix.bottom(6f)

      Then("the updated matrix should have yOffset 1.875")
      updatedMatrix.yOffset shouldBe 1.875f

      And("other attribute should be the same")
      updatedMatrix.xOffset shouldBe 0
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update center X position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update center X position")
      val updatedMatrix = matrix.centerX(1.5f)

      Then("the updated matrix should have xOffset 0.67499995")
      updatedMatrix.xOffset shouldBe 0.67499995f

      And("other attribute should be the same")
      updatedMatrix.yOffset shouldBe 0
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update center Y position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update centerY position")
      val updatedMatrix = matrix.centerY(3.5f)

      Then("the updated matrix should have yOffset 1.4375")
      updatedMatrix.yOffset shouldBe 1.4375f

      And("other attribute should be the same")
      updatedMatrix.xOffset shouldBe 0
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by update center X/Y position") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("update center X position")
      val updatedMatrix = matrix.centerPosition(1.5f, 3.5f)

      Then("the updated matrix should have xOffset=0.67499995 / yOffset=1.4375")
      updatedMatrix.xOffset shouldBe 0.67499995f
      updatedMatrix.yOffset shouldBe 1.4375f

      And("other attribute should be the same")
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f
    }

    Scenario("Create a matrix by scale to certain width but maintain aspect ratio") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("scale width to 2.0")
      val updatedMatrix = matrix.scaleToWidth(2.0f)

      Then("the updated matrix should have xScalar=1.3333334 / 1.3333334")
      updatedMatrix.xScalar shouldBe 1.3333334f
      updatedMatrix.yScalar shouldBe 1.3333334f

      And("other attribute should be the same")
      updatedMatrix.xOffset shouldBe 0
      updatedMatrix.yOffset shouldBe 0
    }

    Scenario("Create a matrix by scale to certain height but maintain aspect ratio") {
      Given("a model matrix with xScalar=1.1 and yScalar=2.2")
      val matrix = new ModelMatrix(1.5f, 1.875f)
        .scale(xScalar = 1.1f, yScalar = 2.2f)

      When("scale height to 2.0")
      val updatedMatrix = matrix.scaleToHeight(2.0f)

      Then("the updated matrix should have xScalar=1.0666667 / 1.0666667")
      updatedMatrix.xScalar shouldBe 1.0666667f
      updatedMatrix.yScalar shouldBe 1.0666667f

      And("other attribute should be the same")
      updatedMatrix.xOffset shouldBe 0
      updatedMatrix.yOffset shouldBe 0
    }

  }

}
