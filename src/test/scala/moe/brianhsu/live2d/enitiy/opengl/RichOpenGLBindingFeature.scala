package moe.brianhsu.live2d.enitiy.opengl

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.ColorWriteMask
import moe.brianhsu.utils.mock.OpenGLMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class RichOpenGLBindingFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                               with OpenGLMock with TableDrivenPropertyChecks with UpdateColorWritingMaskScenarios {

  Feature("Enable / Disable capability") {

    Scenario("Enable capability") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("Enable an OpenGL capability")
      import binding.constants.GL_CULL_FACE
      richOpenGL.setCapabilityEnabled(GL_CULL_FACE, isEnabled = true)

      Then("The stubbed OpenGL binding should be called with glEnable")
      (binding.glEnable _).verify(GL_CULL_FACE).once()
      (binding.glDisable _).verify(*).never()
    }

    Scenario("Disable capability") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("Enable an OpenGL capability")
      import binding.constants.GL_CULL_FACE
      richOpenGL.setCapabilityEnabled(GL_CULL_FACE, isEnabled = false)

      Then("The stubbed OpenGL binding should be called with glDisable")
      (binding.glDisable _).verify(GL_CULL_FACE).once()
      (binding.glEnable _).verify(*).never()
    }

  }

  Feature("Read / update color writing mask") {
    ScenariosFor(updateColorWritingMask(ColorWriteMask(red = false, green = false, blue = false, alpha = false)))
    ScenariosFor(updateColorWritingMask(ColorWriteMask(red = true, green = false, blue = false, alpha = false)))
    ScenariosFor(updateColorWritingMask(ColorWriteMask(red = false, green = true, blue = false, alpha = false)))
    ScenariosFor(updateColorWritingMask(ColorWriteMask(red = false, green = false, blue = true, alpha = false)))
    ScenariosFor(updateColorWritingMask(ColorWriteMask(red = false, green = false, blue = false, alpha = true)))

    Scenario("Read color writing mask") {
      val constants = createOpenGLStub().constants
      import constants._
      val table = Table(
        (   "red",  "green",   "blue",  "alpha", "expectedResult"),
        (GL_FALSE, GL_FALSE, GL_FALSE, GL_FALSE, ColorWriteMask(red = false, green = false, blue = false, alpha = false)),
        ( GL_TRUE, GL_FALSE, GL_FALSE, GL_FALSE, ColorWriteMask(red = true, green = false, blue = false, alpha = false)),
        (GL_FALSE,  GL_TRUE, GL_FALSE, GL_FALSE, ColorWriteMask(red = false, green = true, blue = false, alpha = false)),
        (GL_FALSE, GL_FALSE,  GL_TRUE, GL_FALSE, ColorWriteMask(red = false, green = false, blue = true, alpha = false)),
        (GL_FALSE, GL_FALSE, GL_FALSE,  GL_TRUE, ColorWriteMask(red = false, green = false, blue = false, alpha = true)),
      )

      forAll(table) { case (red, green, blue, alpha, expectedResult) =>
        Given("A RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        import binding.constants._
        (binding.glGetBooleanv _).when(GL_COLOR_WRITEMASK, *).onCall { (_, buffer) =>
          buffer(0) = red.toByte
          buffer(1) = green.toByte
          buffer(2) = blue.toByte
          buffer(3) = alpha.toByte
        }

        When("Read color writing mask")
        val writingMask = richOpenGL.colorWriteMask

        Then("It should have correct value")
        writingMask shouldBe expectedResult

      }
    }
  }

  Feature("Read / update vertex attributes") {
    Scenario("Update vertex attribute") {
      noException shouldBe thrownBy {
        Given("A RichOpenGL with a mocked OpenGL binding")
        val binding = createOpenGLMock()
        val richOpenGL = new RichOpenGLBinding(binding)

        And("that binding expected 4 method call")
        inSequence {
          (binding.glEnableVertexAttribArray _).expects(0).once()
          (binding.glDisableVertexAttribArray _).expects(1).once()
          (binding.glEnableVertexAttribArray _).expects(2).once()
          (binding.glDisableVertexAttribArray _).expects(3).once()
        }

        When("update the vertex attributes")
        richOpenGL.setVertexAttributes(Array(true, false, true, false))

        Then("no exception should be thrown")
      }

    }
  }
}

trait UpdateColorWritingMaskScenarios {
  this: AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory with OpenGLMock =>

  def updateColorWritingMask(mask: ColorWriteMask): Unit = {
    Scenario(s"Writing color writing mask = $mask") {

      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When(s"update the color writing mask = $mask")
      richOpenGL.colorWriteMask = mask

      Then("The stubbed OpenGL binding should call glColorMask with correct values")
      (binding.glColorMask _).verify(mask.red, mask.green, mask.blue, mask.alpha).once()
    }
  }
}
