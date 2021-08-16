package moe.brianhsu.live2d.enitiy.math.matrix

import moe.brianhsu.porting.live2d.framework.math.Rectangle
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class ViewMatrixFeature extends AnyFeatureSpec with GivenWhenThen with Matchers {
  Feature("The basic attributes") {
    Scenario("Access the basic attributes") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
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
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          1.5f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          2.5f, 1.3f, 1.4f, 1.5f
        )
      )

      When("calculate transformed X of 123.5")
      val transformedX = matrix.transformedX(123.5f)

      Then("it should be 123.5 * 1.5 + 2.5")
      transformedX shouldBe (123.5 * 1.5 + 2.5)
    }

    Scenario("Invert the transformed X to origin X") {
      Given("A matrix that scale X with 1.5")
      And("translate X with 2.5")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          1.5f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          2.5f, 1.3f, 1.4f, 1.5f
        )
      )

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
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 1.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 2.5f, 1.4f, 1.5f
        )
      )

      When("calculate transformed Y of 123.5")
      val transformedY = matrix.transformedY(123.5f)

      Then("it should be 123.5 * 1.5 + 2.5")
      transformedY shouldBe (123.5 * 1.5 + 2.5)
    }

    Scenario("Invert the transformed Y to origin Y") {
      Given("A matrix that scale Y with 1.5")
      And("translate Y with 2.5")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 1.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 2.5f, 1.4f, 1.5f
        )
      )

      When("invert the transformed Y 187.75")
      val originalY = matrix.invertedTransformedY(187.75f)

      Then("it should be 123.5")
      originalY shouldBe 123.5
    }

  }

  Feature("Multiple with another matrix") {
    Scenario("Multiple by an identity matrix") {
      Given("An identity matrix")
      val identityMatrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f
      )

      And("a view matrix")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
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
      Given("Two view matrix")
      val matrix1 = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 3.0f,
        maxScale = 2.0f,
        Array(
          1.1f, 2.2f, 3.3f, 4.4f,
          5.5f, 6.6f, 7.7f, 8.8f,
          9.9f, 1.0f, 2.1f, 3.2f,
          4.3f, 5.4f, 6.5f, 7.6f
        )
      )
      val matrix2 = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 3.9171844f, 2.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
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

  Feature("Create updated matrix instance by adjust translate") {
    Scenario("Within the left / right / top / bottom boundary") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 4.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustTranslate(0.1f, 0.2f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.0f, 0.1f, 0.2f, 0.3f,
        0.4f, 0.5f, 0.6f, 0.7f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.863437f, 2.213437f, 2.563437f, 2.913437f
      )
    }

    Scenario("under left boundary") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(1.4f, 1.9585922f, 4.0f, 4.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustTranslate(0.1f, 0.2f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.0f, 0.1f, 0.2f, 0.3f,
        0.4f, 0.5f, 0.6f, 0.7f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.863437f, 2.5492961f, 3.2351556f, 3.9210148f
      )
    }

    Scenario("exceed right boundary") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(1f, 1.9585922f, 4.0f, 4.0f),
        maxRectangle = Rectangle(0.1f, 2.0f, 100f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          1.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustTranslate(0.1f, 0.2f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        1.0f, 0.1f, 0.2f, 0.3f,
        0.4f, 0.5f, 0.6f, 0.7f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.563437f, 2.099296f, 2.3351555f, 2.5710146f
      )
    }

    Scenario("under top boundary") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(1.4f, 3.0f, 4.0f, 4.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustTranslate(0.1f, 0.2f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.0f, 0.1f, 0.2f, 0.3f,
        0.4f, 0.5f, 0.6f, 0.7f,
        0.8f, 0.9f, 1.0f, 1.1f,
        2.2800002f, 3.07f, 3.8600001f, 4.65f
      )
    }

    Scenario("exceed bottom boundary") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 1.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustTranslate(0.1f, 0.2f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.0f, 0.1f, 0.2f, 0.3f,
        0.4f, 0.5f, 0.6f, 0.7f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.063437f, 1.2134368f, 1.3634368f, 1.5134369f
      )
    }

  }
  Feature("Create updated matrix instance by adjust translate") {
    Scenario("The target scale is inside range of minScale to maxScale") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 1.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 0.1f,
        maxScale = 3.0f,
        Array(
          2f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustScale(0.1f, 0.2f, 0.3f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.6f, 0.030000001f, 0.060000002f, 0.09f,
        0.120000005f, 0.15f, 0.18f, 0.21000001f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.0040001f, 1.2229999f, 1.302f, 1.381f
      )
    }

    Scenario("The target scale is under minScale and original scale is zero") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 1.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustScale(0.1f, 0.2f, 0.3f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.0f, 0.030000001f, 0.060000002f, 0.09f,
        0.120000005f, 0.15f, 0.18f, 0.21000001f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.144f, 1.2229999f, 1.302f, 1.381f
      )

    }

    Scenario("The target scale is under minScale and original scale is NOT zero") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 1.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.1f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustScale(0.1f, 0.2f, 0.3f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        2.0f, 2.0f, 4.0f, 6.0f,
        8.0f, 10.0f, 12.0f, 14.0f,
        0.8f, 0.9f, 1.0f, 1.1f,
        2.91f, 3.3899999f, 4.0600004f, 4.73f
      )

    }

    Scenario("The target scale exceed maxScale and original scale is zero") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 1.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = -3.0f,
        maxScale = -2.0f,
        Array(
          0.0f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustScale(0.1f, 0.2f, 0.3f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        0.0f, 0.030000001f, 0.060000002f, 0.09f,
        0.120000005f, 0.15f, 0.18f, 0.21000001f,
        0.8f, 0.9f, 1.0f, 1.1f,
        1.144f, 1.2229999f, 1.302f, 1.381f
      )

    }
    Scenario("The target scale exceed maxScale and original scale is NOT zero") {
      Given("A model matrix with predefined elements")
      val matrix = new ViewMatrix(
        screenRectangle = Rectangle(-1.9585922f, 1.9585922f, 4.0f, 1.0f),
        maxRectangle = Rectangle(-2.0f, 2.0f, 4.0f, 4.0f),
        minScale = 2.0f,
        maxScale = 3.0f,
        Array(
          0.1f, 0.1f, 0.2f, 0.3f,
          0.4f, 0.5f, 0.6f, 0.7f,
          0.8f, 0.9f, 1.0f, 1.1f,
          1.2f, 1.3f, 1.4f, 1.5f
        )
      )

      When("Adjust the translate")
      val updatedMatrix = matrix.adjustScale(0.1f, 0.2f, 40f)

      Then("the updated matrix should have correct elements")
      updatedMatrix.elements shouldBe Array(
        3.0f, 3.0f, 6.0f, 9.0f,
        12.0f, 15.0f, 18.0f, 21.0f,
        0.8f, 0.9f, 1.0f, 1.1f,
        3.81f, 4.49f, 5.46f, 6.4300003f
      )


    }

  }
}