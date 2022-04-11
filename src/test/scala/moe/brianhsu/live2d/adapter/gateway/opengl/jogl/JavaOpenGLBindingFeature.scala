package moe.brianhsu.live2d.adapter.gateway.opengl.jogl

import com.jogamp.opengl.{GL, GL2ES1, GL2ES2}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.nio.{Buffer, ByteBuffer, IntBuffer}

class JavaOpenGLBindingFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {
  Feature("OpenGL constant") {
    Scenario("Validate OpenGL constant") {
      Given("A OpenGL binding")
      val binding = new JavaOpenGLBinding(null)

      Then("it should contains correct constant values")
      binding.GL_TEXTURE_2D shouldBe GL.GL_TEXTURE_2D
      binding.GL_RGBA shouldBe GL.GL_RGBA
      binding.GL_UNSIGNED_BYTE shouldBe GL.GL_UNSIGNED_BYTE
      binding.GL_TEXTURE_MIN_FILTER shouldBe GL.GL_TEXTURE_MIN_FILTER
      binding.GL_LINEAR_MIPMAP_LINEAR shouldBe GL.GL_LINEAR_MIPMAP_LINEAR
      binding.GL_TEXTURE_MAG_FILTER shouldBe GL.GL_TEXTURE_MAG_FILTER
      binding.GL_LINEAR shouldBe GL.GL_LINEAR
      binding.GL_VERTEX_SHADER shouldBe GL2ES2.GL_VERTEX_SHADER
      binding.GL_FRAGMENT_SHADER shouldBe GL2ES2.GL_FRAGMENT_SHADER
      binding.GL_INFO_LOG_LENGTH shouldBe GL2ES2.GL_INFO_LOG_LENGTH
      binding.GL_ZERO shouldBe GL.GL_ZERO
      binding.GL_ONE shouldBe GL.GL_ONE
      binding.GL_ONE_MINUS_SRC_ALPHA shouldBe GL.GL_ONE_MINUS_SRC_ALPHA
      binding.GL_ONE_MINUS_SRC_COLOR shouldBe GL.GL_ONE_MINUS_SRC_COLOR
      binding.GL_DST_COLOR shouldBe GL.GL_DST_COLOR
      binding.GL_TEXTURE1 shouldBe GL.GL_TEXTURE1
      binding.GL_TEXTURE0 shouldBe GL.GL_TEXTURE0
      binding.GL_FLOAT shouldBe GL.GL_FLOAT
      binding.GL_ARRAY_BUFFER_BINDING shouldBe GL.GL_ARRAY_BUFFER_BINDING
      binding.GL_ELEMENT_ARRAY_BUFFER_BINDING shouldBe GL.GL_ELEMENT_ARRAY_BUFFER_BINDING
      binding.GL_CURRENT_PROGRAM shouldBe GL2ES2.GL_CURRENT_PROGRAM
      binding.GL_ACTIVE_TEXTURE shouldBe GL.GL_ACTIVE_TEXTURE
      binding.GL_TEXTURE_BINDING_2D shouldBe GL.GL_TEXTURE_BINDING_2D
      binding.GL_VERTEX_ATTRIB_ARRAY_ENABLED shouldBe GL2ES2.GL_VERTEX_ATTRIB_ARRAY_ENABLED
      binding.GL_SCISSOR_TEST shouldBe GL.GL_SCISSOR_TEST
      binding.GL_STENCIL_TEST shouldBe GL.GL_STENCIL_TEST
      binding.GL_DEPTH_TEST shouldBe GL.GL_DEPTH_TEST
      binding.GL_CULL_FACE shouldBe GL.GL_CULL_FACE
      binding.GL_BLEND shouldBe GL.GL_BLEND
      binding.GL_FRONT_FACE shouldBe GL.GL_FRONT_FACE
      binding.GL_COLOR_WRITEMASK shouldBe GL.GL_COLOR_WRITEMASK
      binding.GL_BLEND_SRC_RGB shouldBe GL.GL_BLEND_SRC_RGB
      binding.GL_BLEND_DST_RGB shouldBe GL.GL_BLEND_DST_RGB
      binding.GL_BLEND_SRC_ALPHA shouldBe GL.GL_BLEND_SRC_ALPHA
      binding.GL_BLEND_DST_ALPHA shouldBe GL.GL_BLEND_DST_ALPHA
      binding.GL_FRAMEBUFFER_BINDING shouldBe GL.GL_FRAMEBUFFER_BINDING
      binding.GL_VIEWPORT shouldBe GL.GL_VIEWPORT
      binding.GL_FALSE shouldBe GL.GL_FALSE
      binding.GL_TRUE shouldBe GL.GL_TRUE
      binding.GL_ARRAY_BUFFER shouldBe GL.GL_ARRAY_BUFFER
      binding.GL_ELEMENT_ARRAY_BUFFER shouldBe GL.GL_ELEMENT_ARRAY_BUFFER
      binding.GL_TEXTURE_WRAP_S shouldBe GL.GL_TEXTURE_WRAP_S
      binding.GL_CLAMP_TO_EDGE shouldBe GL.GL_CLAMP_TO_EDGE
      binding.GL_TEXTURE_WRAP_T shouldBe GL.GL_TEXTURE_WRAP_T
      binding.GL_FRAMEBUFFER shouldBe GL.GL_FRAMEBUFFER
      binding.GL_COLOR_ATTACHMENT0 shouldBe GL.GL_COLOR_ATTACHMENT0
      binding.GL_COLOR_BUFFER_BIT shouldBe GL.GL_COLOR_BUFFER_BIT
      binding.GL_CCW shouldBe GL.GL_CCW
      binding.GL_TRIANGLES shouldBe GL.GL_TRIANGLES
      binding.GL_UNSIGNED_SHORT shouldBe GL.GL_UNSIGNED_SHORT
      binding.GL_SRC_ALPHA shouldBe GL.GL_SRC_ALPHA
      binding.GL_DEPTH_BUFFER_BIT shouldBe GL.GL_DEPTH_BUFFER_BIT
      binding.GL_TRIANGLE_FAN shouldBe GL.GL_TRIANGLE_FAN
    }
  }

  Feature("Delegate OpenGL method to underlay OpenGL binding") {
    Scenario("glGenTextures") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glGenTextures")
      val array = Array(0, 1, 2)
      binding.glGenTextures(0, array)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glGenTextures: (Int, Array[Int], Int) => Unit).verify(0, array, 0).once()
    }

    Scenario("glBindTexture") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glBindTexture")
      binding.glBindTexture(0, 1)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glBindTexture _).verify(0, 1).once()
    }

    Scenario("glTexImage2D") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glTexImage2D")
      val byteBuffer = ByteBuffer.allocate(1)
      binding.glTexImage2D(0, 1, 2, 3, 4, 5, 6, 7, byteBuffer)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glTexImage2D: (Int, Int, Int, Int, Int, Int, Int, Int, Buffer) => Unit)
        .verify(0, 1, 2, 3, 4, 5, 6, 7, byteBuffer)
        .once()
    }

    Scenario("glGenerateMipmap") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glGenerateMipmap")
      binding.glGenerateMipmap(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glGenerateMipmap _).verify(0).once()
    }

    Scenario("glTexParameteri") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glTexParameteri")
      binding.glTexParameteri(0, 1, 2)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glTexParameteri _).verify(0, 1, 2).once()
    }

    Scenario("glViewport") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glViewport")
      binding.glViewport(0, 1, 2, 3)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glViewport _).verify(0, 1, 2, 3).once()
    }

    Scenario("glActiveTexture") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glActiveTexture")
      binding.glActiveTexture(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glActiveTexture _).verify(0).once()
    }

    Scenario("glBlendFuncSeparate") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glBlendFuncSeparate")
      binding.glBlendFuncSeparate(0, 1, 2, 3)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glBlendFuncSeparate _).verify(0, 1, 2, 3).once()
    }

    Scenario("glIsEnabled") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glIsEnabled")
      binding.glIsEnabled(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glIsEnabled _).verify(0).once()
    }

    Scenario("glGetBooleanv") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glGetBooleanv")
      val data = Array[Byte](0)
      binding.glGetBooleanv(0, data)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glGetBooleanv: (Int, Array[Byte], Int) => Unit).verify(0, data, 0).once()
    }

    Scenario("glEnable") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glEnable")
      binding.glEnable(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glEnable _).verify(0).once()
    }

    Scenario("glDisable") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glDisable")
      binding.glDisable(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glDisable _).verify(0).once()
    }

    Scenario("glFrontFace") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glFrontFace")
      binding.glFrontFace(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glFrontFace _).verify(0).once()
    }

    Scenario("glColorMask") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glColorMask")
      binding.glColorMask(red = true, green = false, blue = true, alpha = false)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glColorMask _).verify(true, false, true, false).once()
    }

    Scenario("glBindBuffer") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glBindBuffer")
      binding.glBindBuffer(0, 1)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glBindBuffer _).verify(0, 1).once()
    }

    Scenario("glGetIntegerv") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glGetIntegerv")
      val params = Array[Int](0)
      binding.glGetIntegerv(0, params)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glGetIntegerv: (Int, Array[Int], Int) => Unit).verify(0, params, 0).once()
    }

    Scenario("glGenFramebuffers") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glGenFramebuffers")
      val params = Array[Int](0)
      binding.glGenFramebuffers(0, params)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glGenFramebuffers: (Int, Array[Int], Int) => Unit).verify(0, params, 0).once()
    }

    Scenario("glBindFramebuffer") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glBindFramebuffer")
      binding.glBindFramebuffer(0, 1)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glBindFramebuffer _).verify(0, 1).once()
    }

    Scenario("glFramebufferTexture2D") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glFramebufferTexture2D")
      binding.glFramebufferTexture2D(0, 1, 2, 3, 4)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glFramebufferTexture2D _).verify(0, 1, 2, 3, 4).once()
    }

    Scenario("glClearColor") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glClearColor")
      binding.glClearColor(0.1f, 0.2f, 0.3f, 0.4f)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glClearColor _).verify(0.1f, 0.2f, 0.3f, 0.4f).once()
    }

    Scenario("glClear") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glClear")
      binding.glClear(0)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glClear _).verify(0).once()
    }

    Scenario("glDeleteTextures") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glDeleteTextures")
      val textures = Array[Int](0)
      binding.glDeleteTextures(0, textures)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glDeleteTextures: (Int, Array[Int], Int) => Unit)
        .verify(0, textures, 0)
        .once()
    }

    Scenario("glDeleteFramebuffers") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glDeleteFramebuffers")
      val textures = Array[Int](0)
      binding.glDeleteFramebuffers(0, textures)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glDeleteFramebuffers: (Int, Array[Int], Int) => Unit)
        .verify(0, textures, 0)
        .once()
    }

    Scenario("glBlendFunc") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glBlendFunc")
      binding.glBlendFunc(0, 1)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glBlendFunc _).verify(0, 1).once()
    }

    Scenario("glDrawArrays") {
      Given("a mocked GL object")
      val stubbedGL = stub[GL]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(stubbedGL, null, null, null, null)

      When("call glDrawArrays")
      binding.glDrawArrays(0, 1, 2)

      Then("t1he stubbed GL object should receive the delegated call")
      (stubbedGL.glDrawArrays _).verify(0, 1, 2).once()
    }

    Scenario("glDrawElements") {
      Given("a mocked GL2ES1 object")
      val stubbedGL2ES1 = stub[GL2ES1]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, stubbedGL2ES1, null, null)

      When("call glDrawArrays")
      val buffer = ByteBuffer.allocate(1)
      binding.glDrawElements(0, 1, 2, buffer)

      Then("t1he stubbed GL2ES1 object should receive the delegated call")
      (stubbedGL2ES1.glDrawElements: (Int, Int, Int, Buffer) => Unit).verify(0, 1, 2, buffer).once()
    }

    Scenario("glUseProgram") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glUseProgram")
      binding.glUseProgram(0)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glUseProgram _).verify(0).once()
    }

    Scenario("glCompileShader") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glCompileShader")
      binding.glCompileShader(0)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glCompileShader _).verify(0).once()
    }

    Scenario("glDeleteProgram") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glDeleteProgram")
      binding.glDeleteProgram(0)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDeleteProgram _).verify(0).once()
    }

    Scenario("glCreateProgram") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glDeleteProgram")
      binding.glCreateProgram()

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glCreateProgram _).verify().once()
    }

    Scenario("glAttachShader") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glAttachShader")
      binding.glAttachShader(0, 1)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glAttachShader _).verify(0, 1).once()
    }

    Scenario("glDetachShader") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glDetachShader")
      binding.glDetachShader(0, 1)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDetachShader _).verify(0, 1).once()
    }

    Scenario("glDeleteShader") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glDeleteShader")
      binding.glDeleteShader(0)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glDeleteShader _).verify(0).once()
    }

    Scenario("glLinkProgram") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glLinkProgram")
      binding.glLinkProgram(0)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glLinkProgram _).verify(0).once()
    }

    Scenario("glCreateShader") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glCreateShader")
      binding.glCreateShader(0)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glCreateShader _).verify(0).once()
    }

    Scenario("glShaderSource") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glShaderSource")
      val source = Array[String]("sourceCode")
      binding.glShaderSource(0, 1, source)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glShaderSource: (Int, Int, Array[String], Array[Int], Int) => Unit)
        .verify(0, 1, source, null, 0)
        .once()
    }

    Scenario("glGetProgramiv") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glGetProgramiv")
      val params = Array[Int](0)
      binding.glGetProgramiv(0, 1, params)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetProgramiv: (Int, Int, Array[Int], Int) => Unit)
        .verify(0, 1, params, 0)
        .once()
    }

    Scenario("glGetProgramInfoLog") {
      Given("a mocked GL object")
      val stubbedGL2ES2 = stub[GL2ES2]

      And("a JavaOpenGL binding")
      val binding = new JavaOpenGLBinding(null, null, null, stubbedGL2ES2, null)

      When("call glGetProgramInfoLog")
      val buffer = ByteBuffer.allocate(1)
      binding.glGetProgramInfoLog(0, 1, buffer)

      Then("t1he stubbed GL2ES2 object should receive the delegated call")
      (stubbedGL2ES2.glGetProgramInfoLog: (Int, Int, IntBuffer, ByteBuffer) => Unit)
        .verify(0, 1, *, buffer)
        .once()
    }

  }

}
