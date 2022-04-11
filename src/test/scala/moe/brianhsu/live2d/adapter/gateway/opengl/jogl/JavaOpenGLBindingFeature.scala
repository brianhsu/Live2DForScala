package moe.brianhsu.live2d.adapter.gateway.opengl.jogl

import com.jogamp.opengl.{DebugGL4bc, GL, GL2, GL2ES2, GL4bc}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

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

  }

}
