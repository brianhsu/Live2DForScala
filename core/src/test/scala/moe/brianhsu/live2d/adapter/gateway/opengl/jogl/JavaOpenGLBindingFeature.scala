package moe.brianhsu.live2d.adapter.gateway.opengl.jogl

import com.jogamp.opengl.{GL, GL2ES1, GL2ES2}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.nio.{Buffer, ByteBuffer, FloatBuffer, IntBuffer}

class JavaOpenGLBindingFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {
  Feature("OpenGL constant") {
    Scenario("Validate OpenGL constant") {
      Given("a OpenGL binding")
      val binding = new JavaOpenGLBinding(null)

      Then("it should contains correct constant values")
      binding.constants.GL_TEXTURE_2D shouldBe GL.GL_TEXTURE_2D
      binding.constants.GL_RGBA shouldBe GL.GL_RGBA
      binding.constants.GL_UNSIGNED_BYTE shouldBe GL.GL_UNSIGNED_BYTE
      binding.constants.GL_TEXTURE_MIN_FILTER shouldBe GL.GL_TEXTURE_MIN_FILTER
      binding.constants.GL_LINEAR_MIPMAP_LINEAR shouldBe GL.GL_LINEAR_MIPMAP_LINEAR
      binding.constants.GL_TEXTURE_MAG_FILTER shouldBe GL.GL_TEXTURE_MAG_FILTER
      binding.constants.GL_LINEAR shouldBe GL.GL_LINEAR
      binding.constants.GL_VERTEX_SHADER shouldBe GL2ES2.GL_VERTEX_SHADER
      binding.constants.GL_FRAGMENT_SHADER shouldBe GL2ES2.GL_FRAGMENT_SHADER
      binding.constants.GL_INFO_LOG_LENGTH shouldBe GL2ES2.GL_INFO_LOG_LENGTH
      binding.constants.GL_ZERO shouldBe GL.GL_ZERO
      binding.constants.GL_ONE shouldBe GL.GL_ONE
      binding.constants.GL_ONE_MINUS_SRC_ALPHA shouldBe GL.GL_ONE_MINUS_SRC_ALPHA
      binding.constants.GL_ONE_MINUS_SRC_COLOR shouldBe GL.GL_ONE_MINUS_SRC_COLOR
      binding.constants.GL_DST_COLOR shouldBe GL.GL_DST_COLOR
      binding.constants.GL_TEXTURE1 shouldBe GL.GL_TEXTURE1
      binding.constants.GL_TEXTURE0 shouldBe GL.GL_TEXTURE0
      binding.constants.GL_FLOAT shouldBe GL.GL_FLOAT
      binding.constants.GL_ARRAY_BUFFER_BINDING shouldBe GL.GL_ARRAY_BUFFER_BINDING
      binding.constants.GL_ELEMENT_ARRAY_BUFFER_BINDING shouldBe GL.GL_ELEMENT_ARRAY_BUFFER_BINDING
      binding.constants.GL_CURRENT_PROGRAM shouldBe GL2ES2.GL_CURRENT_PROGRAM
      binding.constants.GL_ACTIVE_TEXTURE shouldBe GL.GL_ACTIVE_TEXTURE
      binding.constants.GL_TEXTURE_BINDING_2D shouldBe GL.GL_TEXTURE_BINDING_2D
      binding.constants.GL_VERTEX_ATTRIB_ARRAY_ENABLED shouldBe GL2ES2.GL_VERTEX_ATTRIB_ARRAY_ENABLED
      binding.constants.GL_SCISSOR_TEST shouldBe GL.GL_SCISSOR_TEST
      binding.constants.GL_STENCIL_TEST shouldBe GL.GL_STENCIL_TEST
      binding.constants.GL_DEPTH_TEST shouldBe GL.GL_DEPTH_TEST
      binding.constants.GL_CULL_FACE shouldBe GL.GL_CULL_FACE
      binding.constants.GL_BLEND shouldBe GL.GL_BLEND
      binding.constants.GL_FRONT_FACE shouldBe GL.GL_FRONT_FACE
      binding.constants.GL_COLOR_WRITEMASK shouldBe GL.GL_COLOR_WRITEMASK
      binding.constants.GL_BLEND_SRC_RGB shouldBe GL.GL_BLEND_SRC_RGB
      binding.constants.GL_BLEND_DST_RGB shouldBe GL.GL_BLEND_DST_RGB
      binding.constants.GL_BLEND_SRC_ALPHA shouldBe GL.GL_BLEND_SRC_ALPHA
      binding.constants.GL_BLEND_DST_ALPHA shouldBe GL.GL_BLEND_DST_ALPHA
      binding.constants.GL_FRAMEBUFFER_BINDING shouldBe GL.GL_FRAMEBUFFER_BINDING
      binding.constants.GL_VIEWPORT shouldBe GL.GL_VIEWPORT
      binding.constants.GL_FALSE shouldBe GL.GL_FALSE
      binding.constants.GL_TRUE shouldBe GL.GL_TRUE
      binding.constants.GL_ARRAY_BUFFER shouldBe GL.GL_ARRAY_BUFFER
      binding.constants.GL_ELEMENT_ARRAY_BUFFER shouldBe GL.GL_ELEMENT_ARRAY_BUFFER
      binding.constants.GL_TEXTURE_WRAP_S shouldBe GL.GL_TEXTURE_WRAP_S
      binding.constants.GL_CLAMP_TO_EDGE shouldBe GL.GL_CLAMP_TO_EDGE
      binding.constants.GL_TEXTURE_WRAP_T shouldBe GL.GL_TEXTURE_WRAP_T
      binding.constants.GL_FRAMEBUFFER shouldBe GL.GL_FRAMEBUFFER
      binding.constants.GL_COLOR_ATTACHMENT0 shouldBe GL.GL_COLOR_ATTACHMENT0
      binding.constants.GL_COLOR_BUFFER_BIT shouldBe GL.GL_COLOR_BUFFER_BIT
      binding.constants.GL_CCW shouldBe GL.GL_CCW
      binding.constants.GL_TRIANGLES shouldBe GL.GL_TRIANGLES
      binding.constants.GL_UNSIGNED_SHORT shouldBe GL.GL_UNSIGNED_SHORT
      binding.constants.GL_SRC_ALPHA shouldBe GL.GL_SRC_ALPHA
      binding.constants.GL_DEPTH_BUFFER_BIT shouldBe GL.GL_DEPTH_BUFFER_BIT
      binding.constants.GL_TRIANGLE_FAN shouldBe GL.GL_TRIANGLE_FAN
    }
  }

  Feature("Create direct float buffer") {
    Scenario("Create from Array[Float]") {
      Given("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, null)

      When("create a buffer from that an Array[Float]")
      val floats = Array[Float](0.1f, 0.2f, 0.3f, 0.4f)
      val buffer = binding.newDirectFloatBuffer(floats)

      Then("the buffer should be a direct buffer")
      buffer.isDirect shouldBe true

      And("the data in the buffer should be same as Array[Float]")
      buffer.get(0) shouldBe 0.1f
      buffer.get(1) shouldBe 0.2f
      buffer.get(2) shouldBe 0.3f
      buffer.get(3) shouldBe 0.4f

      And("should throw IndexOfBound if access out of range index")
      an[IndexOutOfBoundsException] shouldBe thrownBy {
        buffer.get(4)
      }

    }
  }
  Feature("Delegate OpenGL method to underlay OpenGL binding") {
    Scenario("glGenTextures") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glGenTextures")
      val array = Array(0, 1, 2)
      binding.glGenTextures(0, array)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glGenTextures: (Int, Array[Int], Int) => Unit).verify(0, array, 0).once()
    }

    Scenario("glBindTexture") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glBindTexture")
      binding.glBindTexture(0, 1)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glBindTexture _).verify(0, 1).once()
    }

    Scenario("glTexImage2D") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glTexImage2D")
      val byteBuffer = ByteBuffer.allocate(1)
      binding.glTexImage2D(0, 1, 2, 3, 4, 5, 6, 7, byteBuffer)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glTexImage2D: (Int, Int, Int, Int, Int, Int, Int, Int, Buffer) => Unit)
        .verify(0, 1, 2, 3, 4, 5, 6, 7, byteBuffer)
        .once()
    }

    Scenario("glGenerateMipmap") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glGenerateMipmap")
      binding.glGenerateMipmap(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glGenerateMipmap _).verify(0).once()
    }

    Scenario("glTexParameteri") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glTexParameteri")
      binding.glTexParameteri(0, 1, 2)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glTexParameteri _).verify(0, 1, 2).once()
    }

    Scenario("glViewport") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glViewport")
      binding.glViewport(0, 1, 2, 3)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glViewport _).verify(0, 1, 2, 3).once()
    }

    Scenario("glActiveTexture") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glActiveTexture")
      binding.glActiveTexture(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glActiveTexture _).verify(0).once()
    }

    Scenario("glBlendFuncSeparate") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glBlendFuncSeparate")
      binding.glBlendFuncSeparate(0, 1, 2, 3)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glBlendFuncSeparate _).verify(0, 1, 2, 3).once()
    }

    Scenario("glIsEnabled") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glIsEnabled")
      binding.glIsEnabled(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glIsEnabled _).verify(0).once()
    }

    Scenario("glGetBooleanv") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glGetBooleanv")
      val data = Array[Byte](0)
      binding.glGetBooleanv(0, data)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glGetBooleanv: (Int, Array[Byte], Int) => Unit).verify(0, data, 0).once()
    }

    Scenario("glEnable") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glEnable")
      binding.glEnable(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glEnable _).verify(0).once()
    }

    Scenario("glDisable") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glDisable")
      binding.glDisable(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glDisable _).verify(0).once()
    }

    Scenario("glFrontFace") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glFrontFace")
      binding.glFrontFace(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glFrontFace _).verify(0).once()
    }

    Scenario("glColorMask") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glColorMask")
      binding.glColorMask(red = true, green = false, blue = true, alpha = false)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glColorMask _).verify(true, false, true, false).once()
    }

    Scenario("glBindBuffer") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glBindBuffer")
      binding.glBindBuffer(0, 1)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glBindBuffer _).verify(0, 1).once()
    }

    Scenario("glGetIntegerv") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glGetIntegerv")
      val params = Array[Int](0)
      binding.glGetIntegerv(0, params)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glGetIntegerv: (Int, Array[Int], Int) => Unit).verify(0, params, 0).once()
    }

    Scenario("glGetIntegerv with index") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glGetIntegerv")
      val params = Array[Int](0)
      binding.glGetIntegerv(0, params, 1)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL.glGetIntegerv: (Int, Array[Int], Int) => Unit).verify(0, params, 1).once()
    }

    Scenario("glGenFramebuffers") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glGenFramebuffers")
      val params = Array[Int](0)
      binding.glGenFramebuffers(0, params)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glGenFramebuffers: (Int, Array[Int], Int) => Unit).verify(0, params, 0).once()
    }

    Scenario("glBindFramebuffer") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glBindFramebuffer")
      binding.glBindFramebuffer(0, 1)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glBindFramebuffer _).verify(0, 1).once()
    }

    Scenario("glFramebufferTexture2D") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glFramebufferTexture2D")
      binding.glFramebufferTexture2D(0, 1, 2, 3, 4)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glFramebufferTexture2D _).verify(0, 1, 2, 3, 4).once()
    }

    Scenario("glClearColor") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glClearColor")
      binding.glClearColor(0.1f, 0.2f, 0.3f, 0.4f)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glClearColor _).verify(0.1f, 0.2f, 0.3f, 0.4f).once()
    }

    Scenario("glClear") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glClear")
      binding.glClear(0)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glClear _).verify(0).once()
    }

    Scenario("glDeleteTextures") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glDeleteTextures")
      val textures = Array[Int](0)
      binding.glDeleteTextures(0, textures)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glDeleteTextures: (Int, Array[Int], Int) => Unit)
        .verify(0, textures, 0)
        .once()
    }

    Scenario("glDeleteFramebuffers") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glDeleteFramebuffers")
      val textures = Array[Int](0)
      binding.glDeleteFramebuffers(0, textures)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glDeleteFramebuffers: (Int, Array[Int], Int) => Unit)
        .verify(0, textures, 0)
        .once()
    }

    Scenario("glBlendFunc") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glBlendFunc")
      binding.glBlendFunc(0, 1)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glBlendFunc _).verify(0, 1).once()
    }

    Scenario("glDrawArrays") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null)

      When("call glDrawArrays")
      binding.glDrawArrays(0, 1, 2)

      Then("the stubbed GL object should receive the delegated call")
      (stubbedGL.glDrawArrays _).verify(0, 1, 2).once()
    }

    Scenario("glDrawElements") {
      Given("a mocked GL2ES1 object")
      val stubbedGL2ES1 = stub[GL2ES1]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, stubbedGL2ES1, null)

      When("call glDrawArrays")
      val buffer = ByteBuffer.allocate(1)
      binding.glDrawElements(0, 1, 2, buffer)

      Then("the stubbed GL2ES1 object should receive the delegated call")
      (stubbedGL2ES1.glDrawElements: (Int, Int, Int, Buffer) => Unit).verify(0, 1, 2, buffer).once()
    }

    Scenario("glUseProgram") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glUseProgram")
      binding.glUseProgram(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glUseProgram _).verify(0).once()
    }

    Scenario("glCompileShader") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glCompileShader")
      binding.glCompileShader(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glCompileShader _).verify(0).once()
    }

    Scenario("glDeleteProgram") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glDeleteProgram")
      binding.glDeleteProgram(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDeleteProgram _).verify(0).once()
    }

    Scenario("glCreateProgram") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glDeleteProgram")
      binding.glCreateProgram()

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glCreateProgram _).verify().once()
    }

    Scenario("glAttachShader") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glAttachShader")
      binding.glAttachShader(0, 1)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glAttachShader _).verify(0, 1).once()
    }

    Scenario("glDetachShader") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glDetachShader")
      binding.glDetachShader(0, 1)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDetachShader _).verify(0, 1).once()
    }

    Scenario("glDeleteShader") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glDeleteShader")
      binding.glDeleteShader(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDeleteShader _).verify(0).once()
    }

    Scenario("glLinkProgram") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glLinkProgram")
      binding.glLinkProgram(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glLinkProgram _).verify(0).once()
    }

    Scenario("glCreateShader") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glCreateShader")
      binding.glCreateShader(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glCreateShader _).verify(0).once()
    }

    Scenario("glShaderSource") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glShaderSource")
      val source = Array[String]("sourceCode")
      binding.glShaderSource(0, 1, source)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glShaderSource: (Int, Int, Array[String], Array[Int], Int) => Unit)
        .verify(0, 1, source, null, 0)
        .once()
    }

    Scenario("glGetProgramiv") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetProgramiv")
      val params = Array[Int](0)
      binding.glGetProgramiv(0, 1, params)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetProgramiv: (Int, Int, Array[Int], Int) => Unit)
        .verify(0, 1, params, 0)
        .once()
    }

    Scenario("glGetProgramInfoLog") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetProgramInfoLog")
      val buffer = ByteBuffer.allocate(1)
      binding.glGetProgramInfoLog(0, 1, buffer)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetProgramInfoLog: (Int, Int, IntBuffer, ByteBuffer) => Unit)
        .verify(
          where { (program: Int, maxLength: Int, length: IntBuffer, infoLog: ByteBuffer) =>
            program shouldBe 0
            maxLength shouldBe 1
            length should not be null
            infoLog shouldBe buffer
            true
          }
        )
        .once()
    }

    Scenario("glGetShaderiv") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetShaderiv")
      val params = Array[Int](0)
      binding.glGetShaderiv(0, 1, params)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetShaderiv: (Int, Int, Array[Int], Int) => Unit)
        .verify(0, 1, params, 0)
        .once()
    }

    Scenario("glGetShaderInfoLog") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetShaderInfoLog")
      val buffer = ByteBuffer.allocate(1)
      binding.glGetShaderInfoLog(0, 1, buffer)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetShaderInfoLog: (Int, Int, IntBuffer, ByteBuffer) => Unit)
        .verify(
          where { (program: Int, maxLength: Int, length: IntBuffer, infoLog: ByteBuffer) =>
            program shouldBe 0
            maxLength shouldBe 1
            length should not be null
            infoLog shouldBe buffer
            true
          }
        )
        .once()
    }

    Scenario("glGetAttribLocation") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetAttribLocation")
      binding.glGetAttribLocation(0, "name")

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetAttribLocation _).verify(0, "name").once()
    }

    Scenario("glGetUniformLocation") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetUniformLocation")
      binding.glGetUniformLocation(0, "name")

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetUniformLocation _).verify(0, "name").once()
    }

    Scenario("glUniformMatrix4fv") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glUniformMatrix4fv")
      val value = Array[Float](0.1f)
      binding.glUniformMatrix4fv(0, 1, transpose = true, value)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glUniformMatrix4fv: (Int, Int, Boolean, Array[Float], Int) => Unit)
        .verify(0, 1, true, value, 0)
        .once()
    }

    Scenario("glUniform4f") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glUniform4f")
      binding.glUniform4f(0, 0.1f, 0.2f, 0.3f, 0.4f)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glUniform4f _).verify(0, 0.1f, 0.2f, 0.3f, 0.4f).once()
    }

    Scenario("glUniform1i") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glUniform1i")
      binding.glUniform1i(0, 1)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glUniform1i _).verify(0, 1).once()
    }

    Scenario("glEnableVertexAttribArray") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glEnableVertexAttribArray")
      binding.glEnableVertexAttribArray(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glEnableVertexAttribArray _).verify(0).once()
    }

    Scenario("glGetVertexAttribiv") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glGetVertexAttribiv")
      val params = Array[Int](0)
      binding.glGetVertexAttribiv(0, 1, params, 2)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetVertexAttribiv: (Int, Int, Array[Int], Int) => Unit)
        .verify(0, 1, params, 2)
        .once()
    }

    Scenario("glDisableVertexAttribArray") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glDisableVertexAttribArray")
      binding.glDisableVertexAttribArray(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDisableVertexAttribArray _).verify(0).once()
    }

    Scenario("glClearDepth") {
      Given("a mocked GL2ES2 object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2)

      When("call glClearDepth")
      binding.glClearDepth(0)

      Then("the stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glClearDepth _).verify(0).once()
    }

    Scenario("glVertexAttribPointer for ByteBuffer") {
      Given("a mocked GL2 object")
      var isMethodCalled: Boolean = false
      val stubbedGL2 = new MockedGL2() {
        override def glVertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: Buffer): Unit = {
          isMethodCalled = true
        }
      }

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, stubbedGL2, null, null)

      When("call glVertexAttribPointer")
      val byteBuffer = ByteBuffer.allocate(1)
      binding.glVertexAttribPointer(0, 1, 2, normalized = true, 3, byteBuffer)

      Then("the stubbed GL2 object should receive the delegated call")
      isMethodCalled shouldBe true
    }

    Scenario("glVertexAttribPointer for FloatBuffer") {
      Given("a mocked GL2 object")
      var isMethodCalled: Boolean = false
      val stubbedGL2 = new MockedGL2() {
        override def glVertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: Buffer): Unit = {
          isMethodCalled = true
        }
      }

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, stubbedGL2, null, null)

      When("call glVertexAttribPointer")
      val floatBuffer = FloatBuffer.allocate(1)
      binding.glVertexAttribPointer(0, 1, 2, normalized = true, 3, floatBuffer)

      Then("the stubbed GL2 object should receive the delegated call")
      isMethodCalled shouldBe true
    }

  }

}

