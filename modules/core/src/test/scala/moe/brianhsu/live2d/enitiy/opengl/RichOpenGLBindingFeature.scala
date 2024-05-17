package moe.brianhsu.live2d.enitiy.opengl

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding.{ColorWriteMask, ViewPort}
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureColor
import moe.brianhsu.utils.mock.OpenGLMock
import moe.brianhsu.utils.mock.OpenGLMock.Constants._
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import cucumber.api.scala.ScalaDsl
import java.nio.ByteBuffer

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
      Given("a RichOpenGL with a stubbed OpenGL binding")
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
      given Binding = createOpenGLStub() // Scala 3中使用given替代Scala 2的implicit关键字

      val richOpenGL = new RichOpenGLBinding()

      // 假设我们直接检查方法的返回值来判断是否抛出了异常（根据实际逻辑调整）
      val result = richOpenGL.openGLParameters(GL_COLOR_WRITEMASK)
      assert(result != expectedErrorValue, "Expected an exception-like condition")
      }
    }


  }
  Feature("Enable / Disable capability") {

    Scenario("Enable capability") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("enable an OpenGL capability")
      richOpenGL.setCapabilityEnabled(GL_CULL_FACE, isEnabled = true)

      Then("the stubbed OpenGL binding should be called with glEnable")
      (binding.glEnable _).verify(GL_CULL_FACE).once()
      (binding.glDisable _).verify(*).never()
    }

    Scenario("Disable capability") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("enable an OpenGL capability")
      richOpenGL.setCapabilityEnabled(GL_CULL_FACE, isEnabled = false)

      Then("the stubbed OpenGL binding should be called with glDisable")
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

        Given("a RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        When(s"update the color writing mask = $mask")
        richOpenGL.colorWriteMask = mask

        Then("the stubbed OpenGL binding should call glColorMask with correct values")
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
        Given("a RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        (binding.glGetBooleanv _).when(GL_COLOR_WRITEMASK, *).onCall { (_, buffer) =>
          buffer(0) = red.toByte
          buffer(1) = green.toByte
          buffer(2) = blue.toByte
          buffer(3) = alpha.toByte
        }

        When("read color writing mask")
        val writingMask = richOpenGL.colorWriteMask

        Then("it should have correct value")
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
        Given("a RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        And(s"the binding will write the following data: $dataAtIndex0, $dataAtIndex1, $dataAtIndex2, $dataAtIndex3")
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
        Given("a RichOpenGL with a mocked OpenGL binding")
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
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("the binding will write the following data")
      (binding.glGetIntegerv: (Int, Array[Int]) => Unit)
        .when(GL_VIEWPORT, *)
        .onCall { (_, buffer) =>
          buffer(0) = 123
          buffer(1) = 234
          buffer(2) = 456
          buffer(3) = 678
        }

      When("read view port through RichOpenGL binding")
      val viewPort = richOpenGL.viewPort

      Then("the viewPort should be correct")
      viewPort shouldBe ViewPort(123, 234, 456, 678)
    }

    Scenario("Update view port") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("update view port through RichOpenGL binding")
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

  Feature("Generate frame buffers") {
    Scenario("Intended to generate less than 1 frame buffers") {
      val testData = Table(
        "count",
        0,
        -1,
        -2
      )

      forAll(testData) { count =>
        Given("a RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        When(s"generate $count frame buffers")
        Then("it should throw IllegalArgumentException")
        an[IllegalArgumentException] shouldBe thrownBy {
          richOpenGL.generateFrameBuffers(count)
        }
      }
    }

    Scenario("OpenGL does not return enough frame buffer ids") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("the OpenGL binding only generate 1 framebuffer id when request 3")
      (binding.glGenFramebuffers _).when(3, *)
        .onCall { (_, buffer) =>
          buffer(0) = 1
          buffer(1) = 0
          buffer(2) = 0
        }

      When("request RichOpenGL binding to generate 3 frame buffers")
      Then("it should throw a RuntimeException")
      an[RuntimeException] should be thrownBy {
        richOpenGL.generateFrameBuffers(3)
      }
    }

    Scenario("Successfully generate all frame buffers") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("the OpenGL binding generate 3 framebuffer ids when request 3")
      (binding.glGenFramebuffers _).when(3, *)
        .onCall { (_, buffer) =>
          buffer(0) = 1
          buffer(1) = 2
          buffer(2) = 3
        }

      When("request RichOpenGL binding to generate 3 frame buffers")
      val frameBuffersIds = richOpenGL.generateFrameBuffers(3)

      Then("the return value should contains correct ids")
      frameBuffersIds should contain theSameElementsInOrderAs List(1, 2, 3)
    }
  }

  Feature("Generate textures") {
    Scenario("Intended to generate less than 1 textures") {
      val testData = Table(
        "count",
        0,
        -1,
        -2
      )

      forAll(testData) { count =>
        Given("a RichOpenGL with a stubbed OpenGL binding")
        val binding = createOpenGLStub()
        val richOpenGL = new RichOpenGLBinding(binding)

        When(s"generate $count textures")
        Then("it should throw IllegalArgumentException")
        an[IllegalArgumentException] shouldBe thrownBy {
          richOpenGL.generateTextures(count)
        }
      }
    }

    Scenario("OpenGL does not return enough texture ids") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("the OpenGL binding only generate 1 texture id when request 3")
      (binding.glGenTextures _).when(3, *)
        .onCall { (_, buffer) =>
          buffer(0) = 1
          buffer(1) = 0
          buffer(2) = 0
        }

      When("request RichOpenGL binding to generate 3 textures")
      Then("it should throw a RuntimeException")
      an[RuntimeException] should be thrownBy {
        richOpenGL.generateTextures(3)
      }
    }

    Scenario("Successfully generate all textures") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      And("the OpenGL binding generate 3 texture ids when request 3")
      (binding.glGenTextures _).when(3, *)
        .onCall { (_, buffer) =>
          buffer(0) = 1
          buffer(1) = 2
          buffer(2) = 3
        }

      When("request RichOpenGL binding to generate 3 textures")
      val textureIds = richOpenGL.generateTextures(3)

      Then("the return ids should contains correct ids")
      textureIds should contain theSameElementsInOrderAs List(1, 2, 3)
    }

  }

  Feature("Set color channel") {
    Scenario("Set color channel by TextureColor object") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("set color channel by TextureColor object")
      richOpenGL.setColorChannel(
        textureColor = TextureColor(0.1f, 0.2f, 0.3f, 0.4f),
        uniformChannelFlagLocation = 4
      )

      Then("it should delegated to underlay OpenGL binding")
      (binding.glUniform4f _).verify(4, 0.1f, 0.2f, 0.3f, 0.4f).once()
    }
  }

  Feature("Active and update texture variable") {
    Scenario("Active and update texture variable") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("active and update texture variable")
      val textureUnit = 1
      val textureId = 2
      val variable = 3
      val newValue = 4
      richOpenGL.activeAndUpdateTextureVariable(textureUnit, textureId, variable, newValue)

      Then("it should delegated to underlay OpenGL binding")
      inSequence {
        (binding.glActiveTexture _).verify(textureUnit).once()
        (binding.glBindTexture _).verify(GL_TEXTURE_2D, textureId).once()
        (binding.glUniform1i _).verify(variable, newValue).once()
      }
    }
  }

  Feature("Update vertex info") {
    Scenario("Update vertex info with ByteBuffer") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("update vertex info with ByteBuffer")
      val vertexArray = ByteBuffer.allocate(1)
      val uvArray = ByteBuffer.allocate(1)
      val attributePositionLocation = 1
      val attributeTexCoordLocation = 2
      richOpenGL.updateVertexInfo(vertexArray, uvArray, attributePositionLocation, attributeTexCoordLocation)

      Then("it should delegated to underlay OpenGL binding")
      type MethodType = (Int, Int, Int, Boolean, Int, ByteBuffer) => Unit
      inSequence {
        (binding.glEnableVertexAttribArray _).verify(attributePositionLocation).once()
        (binding.glVertexAttribPointer: MethodType).verify(attributePositionLocation, 2, GL_FLOAT, false, 4 * 2, vertexArray).once()
        // テクスチャ頂点の設定
        (binding.glEnableVertexAttribArray _).verify(attributeTexCoordLocation).once()
        (binding.glVertexAttribPointer: MethodType).verify(attributeTexCoordLocation, 2, GL_FLOAT, false, 4 * 2, uvArray).once()
      }

    }

    Scenario("Update when vertexArray buffer does not have remaining bytes") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("update vertex info with vertexArray ByteBuffer is empty")
      val vertexArray = ByteBuffer.allocate(0)
      val uvArray = ByteBuffer.allocate(1)
      val attributePositionLocation = 1
      val attributeTexCoordLocation = 2
      richOpenGL.updateVertexInfo(vertexArray, uvArray, attributePositionLocation, attributeTexCoordLocation)

      Then("it should NOT delegated glVertexAttribPointer of vertexArray to underlay OpenGL binding")
      type MethodType = (Int, Int, Int, Boolean, Int, ByteBuffer) => Unit
      inSequence {
        (binding.glEnableVertexAttribArray _).verify(attributePositionLocation).never()
        (binding.glVertexAttribPointer: MethodType).verify(attributePositionLocation, 2, GL_FLOAT, false, 4 * 2, vertexArray).never()
        // テクスチャ頂点の設定
        (binding.glEnableVertexAttribArray _).verify(attributeTexCoordLocation).once()
        (binding.glVertexAttribPointer: MethodType).verify(attributeTexCoordLocation, 2, GL_FLOAT, false, 4 * 2, uvArray)
      }

    }

    Scenario("Update when uvArray buffer does not have remaining bytes") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("update vertex info with uvArray ByteBuffer is empty")
      val vertexArray = ByteBuffer.allocate(1)
      val uvArray = ByteBuffer.allocate(0)
      val attributePositionLocation = 1
      val attributeTexCoordLocation = 2
      richOpenGL.updateVertexInfo(vertexArray, uvArray, attributePositionLocation, attributeTexCoordLocation)

      Then("it should NOT delegated glVertexAttribPointer of uvArray to underlay OpenGL binding")
      type MethodType = (Int, Int, Int, Boolean, Int, ByteBuffer) => Unit
      inSequence {
        (binding.glEnableVertexAttribArray _).verify(attributePositionLocation).once()
        (binding.glVertexAttribPointer: MethodType).verify(attributePositionLocation, 2, GL_FLOAT, false, 4 * 2, vertexArray).once()

        // テクスチャ頂点の設定
        (binding.glEnableVertexAttribArray _).verify(attributeTexCoordLocation).never()
        (binding.glVertexAttribPointer: MethodType).verify(attributeTexCoordLocation, 2, GL_FLOAT, false, 4 * 2, uvArray).never()
      }

    }

    Scenario("Update when both vertexArray / uvArray buffer does not have remaining bytes") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("update vertex info with both vertexArray / uvArray ByteBuffer are empty")
      val vertexArray = ByteBuffer.allocate(0)
      val uvArray = ByteBuffer.allocate(0)
      val attributePositionLocation = 1
      val attributeTexCoordLocation = 2
      richOpenGL.updateVertexInfo(vertexArray, uvArray, attributePositionLocation, attributeTexCoordLocation)

      Then("it should NOT delegated glVertexAttribPointer of uvArray to underlay OpenGL binding")
      type MethodType = (Int, Int, Int, Boolean, Int, ByteBuffer) => Unit
      inSequence {
        (binding.glEnableVertexAttribArray _).verify(attributePositionLocation).never()
        (binding.glVertexAttribPointer: MethodType).verify(attributePositionLocation, 2, GL_FLOAT, false, 4 * 2, vertexArray).never()

        // テクスチャ頂点の設定
        (binding.glEnableVertexAttribArray _).verify(attributeTexCoordLocation).never()
        (binding.glVertexAttribPointer: MethodType).verify(attributeTexCoordLocation, 2, GL_FLOAT, false, 4 * 2, uvArray).never()
      }

    }

  }
  Feature("Pre drawing") {
    Scenario("Call preDraw method on RichOpenGL object") {
      Given("a RichOpenGL with a stubbed OpenGL binding")
      val binding = createOpenGLStub()
      val richOpenGL = new RichOpenGLBinding(binding)

      When("call preDraw()")
      richOpenGL.preDraw()

      Then("it should delegated to underlay OpenGL binding")
      inSequence {
        (binding.glDisable _).verify(GL_SCISSOR_TEST).once()
        (binding.glDisable _).verify(GL_STENCIL_TEST).once()
        (binding.glDisable _).verify(GL_DEPTH_TEST).once()
        (binding.glEnable _).verify(GL_BLEND).pos
        (binding.glColorMask _).verify(true, true, true, true).once()
        (binding.glBindBuffer _).verify(GL_ELEMENT_ARRAY_BUFFER, 0).once()
        (binding.glBindBuffer _).verify(GL_ARRAY_BUFFER, 0).once()
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

