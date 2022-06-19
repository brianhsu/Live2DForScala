package moe.brianhsu.live2d.enitiy.opengl

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.{BlendFunction, ColorWriteMask, ViewPort}
import moe.brianhsu.utils.mock.OpenGLMock
import moe.brianhsu.utils.mock.OpenGLMock.Constants._
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class RichOpenGLBindingFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory
                               with OpenGLMock with TableDrivenPropertyChecks {

  Feature("Singleton by OpenGL binding") {
    Scenario("Get instance with same OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val binding = createOpenGLStub()

      When("create a wrap from that binding")
      val thisWrap = RichOpenGLBinding.wrapOpenGLBinding(binding)

      And("create another wrap from that binding again")
      val thatWrap = RichOpenGLBinding.wrapOpenGLBinding(binding)

      Then("these two wrapping should be same instance")
      thisWrap should be theSameInstanceAs thatWrap
    }

    Scenario("Get instance with different OpenGL binding") {
      Given("a stubbed OpenGL binding")
      val binding = createOpenGLStub()

      When("create a wrap from that binding")
      val thisWrap = RichOpenGLBinding.wrapOpenGLBinding(binding)

      And("create another wrap from another binding")
      val anotherBinding = createOpenGLStub()
      val thatWrap = RichOpenGLBinding.wrapOpenGLBinding(anotherBinding)

      Then("these two wrapping should be different instance")
      thisWrap should not be theSameInstanceAs (thatWrap)
    }

  }
  Feature("Read OpenGL parameter") {
    Scenario("Read integer parameter") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("it will write the GL_ARRAY_BUFFER_BINDING / GL_CURRENT_PROGRAM data to buffer")
      addDummyIntegerVariable(binding, GL_ARRAY_BUFFER_BINDING, 0, 123)
      addDummyIntegerVariable(binding, GL_CURRENT_PROGRAM, 0, 456)

      When("read those parameters through RichOpenGL binding")
      val bufferBinding: Int = richOpenGL.openGLParameters(GL_ARRAY_BUFFER_BINDING)
      val currentProgram: Int = richOpenGL.openGLParameters(GL_CURRENT_PROGRAM)

      Then("they should have correct value")
      bufferBinding shouldBe 123
      currentProgram shouldBe 456

    }

    Scenario("Read boolean parameter") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      an[Exception] should be thrownBy {
        When("read a boolean parameter")
        Then("it should thrown exception")
        richOpenGL.openGLParameters[Boolean](GL_COLOR_WRITEMASK)
      }
    }

  }
  Feature("Enable / Disable capability") {

    Scenario("Enable capability") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("Enable an OpenGL capability")
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
      richOpenGL.setCapabilityEnabled(GL_CULL_FACE, isEnabled = false)

      Then("The stubbed OpenGL binding should be called with glDisable")
      (binding.glDisable _).verify(GL_CULL_FACE).once()
      (binding.glEnable _).verify(*).never()
    }

  }

  Feature("Read / update color writing mask") {
    val testDataForUpdateColorWritingMask = Table(
      ("red", "green", "blue", "alpha"),
      (false,   false,  false,   false),
      (false,    true,  false,   false),
      (false,   false,   true,   false),
      (false,   false,  false,    true),
    )

    forAll(testDataForUpdateColorWritingMask) { (red, green, blue, alpha) =>
      val mask = ColorWriteMask(red, green, blue, alpha)

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

    Scenario("Read color writing mask") {
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
    Scenario("Read vertex attributes") {
      val testData = Table(
        ("dataAtIndex0", "dataAtIndex1", "dataAtIndex2", "dataAtIndex3", "expectedResult"),
        (GL_FALSE, GL_FALSE, GL_FALSE, GL_FALSE, List(false, false, false, false)),
        ( GL_TRUE, GL_FALSE, GL_FALSE, GL_FALSE, List( true, false, false, false)),
        (GL_FALSE,  GL_TRUE, GL_FALSE, GL_FALSE, List(false,  true, false, false)),
        (GL_FALSE, GL_FALSE,  GL_TRUE, GL_FALSE, List(false, false,  true, false)),
        (GL_FALSE, GL_FALSE, GL_FALSE,  GL_TRUE, List(false, false, false,  true)),
      )

      forAll(testData) { (dataAtIndex0, dataAtIndex1, dataAtIndex2, dataAtIndex3, expectedResult) =>
        Given("A RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        And(s"The binding will write the following data: $dataAtIndex0, $dataAtIndex1, $dataAtIndex2, $dataAtIndex3")
        addDummyVertexAttribute(binding, 0, dataAtIndex0)
        addDummyVertexAttribute(binding, 1, dataAtIndex1)
        addDummyVertexAttribute(binding, 2, dataAtIndex2)
        addDummyVertexAttribute(binding, 3, dataAtIndex3)

        When("read vertexAttributes from RichOpenGL wrapping")
        val vertexAttributes = richOpenGL.vertexAttributes

        Then(s"the returned attributes should be $expectedResult")
        vertexAttributes should contain theSameElementsInOrderAs expectedResult

      }
    }

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
        richOpenGL.vertexAttributes = Array(true, false, true, false)

        Then("no exception should be thrown")
      }

    }
  }

  Feature("Read / update view port") {
    Scenario("Read view port") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("The binding will write the following data")
      (binding.glGetIntegerv: (Int, Array[Int]) => Unit)
        .when(GL_VIEWPORT, *)
        .onCall { (_, buffer) =>
          buffer(0) = 123
          buffer(1) = 234
          buffer(2) = 456
          buffer(3) = 678
        }

      When("Read view port through RichOpenGL binding")
      val viewPort = richOpenGL.viewPort

      Then("The viewPort should be correct")
      viewPort shouldBe ViewPort(123, 234, 456, 678)
    }

    Scenario("Update view port") {
      Given("A RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("Update view port through RichOpenGL binding")
      richOpenGL.viewPort = ViewPort(123, 456, 789, 987)

      Then("the stubbed OpenGL binding should update view port")
      (binding.glViewport _).verify(123, 456, 789, 987).once()
    }
  }

  Feature("Read / update blend function") {
    Scenario("Read blen function") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      addDummyIntegerVariable(binding, GL_BLEND_SRC_RGB, 0, 123)
      addDummyIntegerVariable(binding, GL_BLEND_DST_RGB, 1, 456)
      addDummyIntegerVariable(binding, GL_BLEND_SRC_ALPHA, 2, 789)
      addDummyIntegerVariable(binding, GL_BLEND_DST_ALPHA, 3, 912)

      When("read blendFunction through RichOpenGL binding")
      val blendFunction = richOpenGL.blendFunction

      Then("it should have correct data")
      blendFunction shouldBe BlendFunction(123, 456, 789, 912)

    }

    Scenario("Update blend function") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("update blend function through RichOpenGL binding")
      richOpenGL.blendFunction = BlendFunction(1, 2, 3, 4)

      Then("the binding should execute delegated method call")
      (binding.glBlendFuncSeparate _).verify(1, 2, 3, 4).once()
    }
  }

  Feature("Read / update texture binding") {

    Scenario("Read texture binding") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("it will write the following data")
      addDummyIntegerVariable(binding, GL_TEXTURE_BINDING_2D, 0, 123)

      When("read texture binding")
      val textureBinding = richOpenGL.textureBinding2D(GL_TEXTURE0)

      Then("it should have correct value")
      textureBinding shouldBe 123

      And("the binding should call active texture")
      (binding.glActiveTexture _).verify(GL_TEXTURE0).once()

    }

    val testDataForActiveAndBinding = Table(
      ("textureUnit", "textureName"),
      (GL_TEXTURE0, 123),
      (GL_TEXTURE1, 456)
    )

    forAll(testDataForActiveAndBinding) { (textureUnit, textureName) =>
      Scenario(s"Active and binding 2d texture(textureUnit = $textureUnit, textureName = $textureName") {
        Given("a RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        When("active and binding textureName")
        richOpenGL.activeAndBinding2DTexture(textureUnit, textureName)

        Then("the stubbed binding should call corresponding methods")
        binding.glActiveTexture(textureUnit)
        binding.glBindTexture(textureUnit, textureName)
      }

    }
  }

  private def addDummyIntegerVariable(binding: OpenGLBinding, pname: Int, offset: Int, value: Int): Unit = {
    (binding.glGetIntegerv: (Int, Array[Int], Int) => Unit)
      .when(pname, *, offset)
      .onCall((_, buffer, index) => buffer(index) = value)

  }

  private def addDummyVertexAttribute(binding: OpenGLBinding, index: Int, value: Int): Unit = {
    import binding.constants._
    (binding.glGetVertexAttribiv _)
      .when(index, GL_VERTEX_ATTRIB_ARRAY_ENABLED, *, index)
      .onCall { (index, _, buffer, _) =>
        buffer(index) = value
      }
  }
}
