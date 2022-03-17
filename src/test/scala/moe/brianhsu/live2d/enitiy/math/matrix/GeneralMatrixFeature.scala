package moe.brianhsu.live2d.enitiy.math.matrix

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class GeneralMatrixFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {

  Feature("The basic attributes") {
    Scenario("Access the basic attributes") {
      Given("A general 4x4 matrix with predefined elements")
      val matrix = new GeneralMatrix(
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )
      When("request the basic attributes from matrix")
      val xScalar = matrix.xScalar
      val yScalar = matrix.yScalar
      val xTranslator = matrix.xOffset
      val yTranslator = matrix.yOffset

      Then("those attribute should have correct values")
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
      val matrix = new GeneralMatrix().scale(xScalar = 1.5f, yScalar = 1.0f).translateX(2.5f)

      When("calculate transformed X of 123.5")
      val transformedX = matrix.transformedX(123.5f)

      Then("it should be 123.5 * 1.5 + 2.5")
      transformedX shouldBe (123.5 * 1.5 + 2.5)
    }

    Scenario("Invert the transformed X to origin X") {
      Given("A matrix that scale X with 1.5")
      And("translate X with 2.5")
      val matrix = new GeneralMatrix().scale(xScalar = 1.5f, yScalar = 1.0f).translateX(2.5f)

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
      val matrix = new GeneralMatrix().scale(xScalar = 1.0f, yScalar = 1.5f).translateY(2.5f)

      When("calculate transformed Y of 123.5")
      val transformedY = matrix.transformedY(123.5f)

      Then("it should be 123.5 * 1.5 + 2.5")
      transformedY shouldBe (123.5 * 1.5 + 2.5)
    }

    Scenario("Invert the transformed Y to origin Y") {
      Given("A matrix that scale Y with 1.5")
      And("translate Y with 2.5")
      val matrix = new GeneralMatrix().scale(xScalar = 1.0f, yScalar = 1.5f).translateY(2.5f)

      When("invert the transformed Y 187.75")
      val originalY = matrix.invertedTransformedY(187.75f)

      Then("it should be 123.5")
      originalY shouldBe 123.5
    }

  }

  Feature("Create updated matrix instance") {
    Scenario("Create a matrix by set a new scale rate") {
      Given("A identity matrix")
      val matrix = new GeneralMatrix()

      When("update it to a matrix with X scale rate 1.5,  Y scale rate 2.5")
      val updatedMatrix = matrix.scale(xScalar = 1.5f, yScalar = 2.5f)

      Then("the updated matrix has xScalar=1.5, yScalar=2.5")
      updatedMatrix.xScalar shouldBe 1.5f
      updatedMatrix.yScalar shouldBe 2.5f

      Then("the original matrix scalar should remain untouched")
      matrix.xScalar shouldBe 1.0f
      matrix.yScalar shouldBe 1.0f

    }

    Scenario("Create a matrix by set a new X offset") {
      Given("A identity matrix")
      val matrix = new GeneralMatrix()

      When("update it to a matrix X offset = 20")
      val updatedMatrix = matrix.translateX(20)

      Then("the updated matrix has xTranslator=20")
      updatedMatrix.xOffset shouldBe 20

      Then("the original matrix offset should remain untouched")
      matrix.xOffset shouldBe 0
    }

    Scenario("Create a matrix by set a Y offset") {
      Given("A identity matrix")
      val matrix = new GeneralMatrix()

      When("update it to a matrix Y offset = 30")
      val updatedMatrix = matrix.translateY(30)

      Then("the updated matrix has yTranslator=30")
      updatedMatrix.yOffset shouldBe 30

      Then("the original matrix offset should remain untouched")
      matrix.yOffset shouldBe 0
    }

    Scenario("Create a matrix by set a new X / Y offset") {
      Given("A identity matrix")
      val matrix = new GeneralMatrix()

      When("update it to a matrix X offset = 20, Y offset = 30")
      val updatedMatrix = matrix.translate(20, 30)

      Then("the updated matrix has xTranslator=20, yTranslator=30")
      updatedMatrix.xOffset shouldBe 20
      updatedMatrix.yOffset shouldBe 30

      Then("the original matrix offset should remain untouched")
      matrix.xOffset shouldBe 0
      matrix.yOffset shouldBe 0
    }

    Scenario("Create a matrix by set offset relative to current matrix") {
      Given("A matrix with X scalar 1.1, y Scalar 2.2, X offset 3.3, Y offset 4.4")
      val matrix = new GeneralMatrix()
        .scale(xScalar = 1.1f, yScalar = 2.2f)
        .translate(xOffset = 3.3f, yOffset = 4.4f)

      When("update it to a matrix translate relative x = 5.5, y = 6.6")
      val updatedMatrix = matrix.translateRelative(5.5f, 6.6f)

      Then("the X offset should be 9.35")
      updatedMatrix.xOffset shouldBe 9.35f

      And("the Y offset should be 18.92")
      updatedMatrix.yOffset shouldBe 18.92f

      And("the X / Y scalar should not be changed")
      updatedMatrix.xScalar shouldBe 1.1f
      updatedMatrix.yScalar shouldBe 2.2f

      Then("the original matrix offset should remain untouched")
      matrix.xScalar shouldBe 1.1f
      matrix.yScalar shouldBe 2.2f
      matrix.xOffset shouldBe 3.3f
      matrix.yOffset shouldBe 4.4f
    }

    Scenario("Create a matrix by set scalar relative to current matrix") {
      Given("A matrix with X scalar 1.1, y Scalar 2.2, X offset 3.3, Y offset 4.4")
      val matrix = new GeneralMatrix()
        .scale(xScalar = 1.1f, yScalar = 2.2f)
        .translate(xOffset = 3.3f, yOffset = 4.4f)

      When("update it to a matrix scale relative x = 5.5, y = 6.6")
      val updatedMatrix = matrix.scaleRelative(5.5f, 6.6f)

      Then("the X scalar should be 6.05")
      updatedMatrix.xScalar shouldBe 6.05f

      And("the Y scalar should be 14.52")
      updatedMatrix.yScalar shouldBe 14.52f

      And("the X / Y scalar should not be changed")
      updatedMatrix.xOffset shouldBe 3.3f
      updatedMatrix.yOffset shouldBe 4.4f

      Then("the original matrix offset should remain untouched")
      matrix.xScalar shouldBe 1.1f
      matrix.yScalar shouldBe 2.2f
      matrix.xOffset shouldBe 3.3f
      matrix.yOffset shouldBe 4.4f
    }

  }

  Feature("Multiple with another matrix") {
    Scenario("Multiple by an identity matrix") {
      Given("An identity matrix")
      val identityMatrix = new GeneralMatrix()
      And("a normal 4x4 matrix")
      val matrix = new GeneralMatrix(Array(
        0.1f, 0.2f, 0.3f, 0.4f,
        0.5f, 0.6f, 0.7f, 0.8f,
        0.9f, 1.0f, 1.1f, 1.2f,
        1.3f, 1.4f, 1.5f, 1.6f
      ))

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
      Given("Two normal 4x4 matrix")
      val matrix1 = new GeneralMatrix(Array(
        1.1f, 2.2f, 3.3f, 4.4f,
        5.5f, 6.6f, 7.7f, 8.8f,
        9.9f, 1.0f, 2.1f, 3.2f,
        4.3f, 5.4f, 6.5f, 7.6f
      ))
      val matrix2 = new GeneralMatrix(Array(
        0.1f, 0.2f, 0.3f, 0.4f,
        0.5f, 0.6f, 0.7f, 0.8f,
        0.9f, 1.0f, 1.1f, 1.2f,
        1.3f, 1.4f, 1.5f, 1.6f
      ))

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

}
