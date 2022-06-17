package moe.brianhsu.live2d.adapter.gateway.opengl.lwjgl

import org.lwjgl.opengl.{GL11, GL12, GL13, GL14, GL15, GL20, GL30}
import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class LWJGLBindingFeature extends AnyFeatureSpec with Matchers with GivenWhenThen with MockFactory {
  Feature("OpenGL constant") {
    Scenario("Validate OpenGL constant") {
      Given("A LWJGL OpenGL binding")
      val binding = new LWJGLBinding()

      Then("it should contains correct constant values")
      binding.constants.GL_TEXTURE_2D shouldBe GL11.GL_TEXTURE_2D
      binding.constants.GL_RGBA shouldBe GL11.GL_RGBA
      binding.constants.GL_UNSIGNED_BYTE shouldBe GL11.GL_UNSIGNED_BYTE
      binding.constants.GL_TEXTURE_MIN_FILTER shouldBe GL11.GL_TEXTURE_MIN_FILTER
      binding.constants.GL_LINEAR_MIPMAP_LINEAR shouldBe GL11.GL_LINEAR_MIPMAP_LINEAR
      binding.constants.GL_TEXTURE_MAG_FILTER shouldBe GL11.GL_TEXTURE_MAG_FILTER
      binding.constants.GL_LINEAR shouldBe GL11.GL_LINEAR
      binding.constants.GL_VERTEX_SHADER shouldBe GL20.GL_VERTEX_SHADER
      binding.constants.GL_FRAGMENT_SHADER shouldBe GL20.GL_FRAGMENT_SHADER
      binding.constants.GL_INFO_LOG_LENGTH shouldBe GL20.GL_INFO_LOG_LENGTH
      binding.constants.GL_ZERO shouldBe GL11.GL_ZERO
      binding.constants.GL_ONE shouldBe GL11.GL_ONE
      binding.constants.GL_ONE_MINUS_SRC_ALPHA shouldBe GL11.GL_ONE_MINUS_SRC_ALPHA
      binding.constants.GL_ONE_MINUS_SRC_COLOR shouldBe GL11.GL_ONE_MINUS_SRC_COLOR
      binding.constants.GL_DST_COLOR shouldBe GL11.GL_DST_COLOR
      binding.constants.GL_TEXTURE1 shouldBe GL13.GL_TEXTURE1
      binding.constants.GL_TEXTURE0 shouldBe GL13.GL_TEXTURE0
      binding.constants.GL_FLOAT shouldBe GL11.GL_FLOAT
      binding.constants.GL_ARRAY_BUFFER_BINDING shouldBe GL15.GL_ARRAY_BUFFER_BINDING
      binding.constants.GL_ELEMENT_ARRAY_BUFFER_BINDING shouldBe GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING
      binding.constants.GL_CURRENT_PROGRAM shouldBe GL20.GL_CURRENT_PROGRAM
      binding.constants.GL_ACTIVE_TEXTURE shouldBe GL13.GL_ACTIVE_TEXTURE
      binding.constants.GL_TEXTURE_BINDING_2D shouldBe GL11.GL_TEXTURE_BINDING_2D
      binding.constants.GL_VERTEX_ATTRIB_ARRAY_ENABLED shouldBe GL20.GL_VERTEX_ATTRIB_ARRAY_ENABLED
      binding.constants.GL_SCISSOR_TEST shouldBe GL11.GL_SCISSOR_TEST
      binding.constants.GL_STENCIL_TEST shouldBe GL11.GL_STENCIL_TEST
      binding.constants.GL_DEPTH_TEST shouldBe GL11.GL_DEPTH_TEST
      binding.constants.GL_CULL_FACE shouldBe GL11.GL_CULL_FACE
      binding.constants.GL_BLEND shouldBe GL11.GL_BLEND
      binding.constants.GL_FRONT_FACE shouldBe GL11.GL_FRONT_FACE
      binding.constants.GL_COLOR_WRITEMASK shouldBe GL11.GL_COLOR_WRITEMASK
      binding.constants.GL_BLEND_SRC_RGB shouldBe GL14.GL_BLEND_SRC_RGB
      binding.constants.GL_BLEND_DST_RGB shouldBe GL14.GL_BLEND_DST_RGB
      binding.constants.GL_BLEND_SRC_ALPHA shouldBe GL14.GL_BLEND_SRC_ALPHA
      binding.constants.GL_BLEND_DST_ALPHA shouldBe GL14.GL_BLEND_DST_ALPHA
      binding.constants.GL_FRAMEBUFFER_BINDING shouldBe GL30.GL_FRAMEBUFFER_BINDING
      binding.constants.GL_VIEWPORT shouldBe GL11.GL_VIEWPORT
      binding.constants.GL_FALSE shouldBe GL11.GL_FALSE
      binding.constants.GL_TRUE shouldBe GL11.GL_TRUE
      binding.constants.GL_ARRAY_BUFFER shouldBe GL15.GL_ARRAY_BUFFER
      binding.constants.GL_ELEMENT_ARRAY_BUFFER shouldBe GL15.GL_ELEMENT_ARRAY_BUFFER
      binding.constants.GL_TEXTURE_WRAP_S shouldBe GL11.GL_TEXTURE_WRAP_S
      binding.constants.GL_CLAMP_TO_EDGE shouldBe GL12.GL_CLAMP_TO_EDGE
      binding.constants.GL_TEXTURE_WRAP_T shouldBe GL11.GL_TEXTURE_WRAP_T
      binding.constants.GL_FRAMEBUFFER shouldBe GL30.GL_FRAMEBUFFER
      binding.constants.GL_COLOR_ATTACHMENT0 shouldBe GL30.GL_COLOR_ATTACHMENT0
      binding.constants.GL_COLOR_BUFFER_BIT shouldBe GL11.GL_COLOR_BUFFER_BIT
      binding.constants.GL_CCW shouldBe GL11.GL_CCW
      binding.constants.GL_TRIANGLES shouldBe GL11.GL_TRIANGLES
      binding.constants.GL_UNSIGNED_SHORT shouldBe GL11.GL_UNSIGNED_SHORT
      binding.constants.GL_SRC_ALPHA shouldBe GL11.GL_SRC_ALPHA
      binding.constants.GL_DEPTH_BUFFER_BIT shouldBe GL11.GL_DEPTH_BUFFER_BIT
      binding.constants.GL_TRIANGLE_FAN shouldBe GL11.GL_TRIANGLE_FAN

    }
  }

  Feature("Create direct float buffer") {
    Scenario("Create from Array[Float]") {
      Given("A LWJGL OpenGL binding")
      val binding = new LWJGLBinding()

      When("create a buffer from that an Array[Float]")
      val floats = Array[Float](0.1f, 0.2f, 0.3f, 0.4f)
      val buffer = binding.newDirectFloatBuffer(floats)

      Then("The buffer should be a direct buffer")
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
}

