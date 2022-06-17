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
      binding.openGLConstants.GL_TEXTURE_2D shouldBe GL11.GL_TEXTURE_2D
      binding.openGLConstants.GL_RGBA shouldBe GL11.GL_RGBA
      binding.openGLConstants.GL_UNSIGNED_BYTE shouldBe GL11.GL_UNSIGNED_BYTE
      binding.openGLConstants.GL_TEXTURE_MIN_FILTER shouldBe GL11.GL_TEXTURE_MIN_FILTER
      binding.openGLConstants.GL_LINEAR_MIPMAP_LINEAR shouldBe GL11.GL_LINEAR_MIPMAP_LINEAR
      binding.openGLConstants.GL_TEXTURE_MAG_FILTER shouldBe GL11.GL_TEXTURE_MAG_FILTER
      binding.openGLConstants.GL_LINEAR shouldBe GL11.GL_LINEAR
      binding.openGLConstants.GL_VERTEX_SHADER shouldBe GL20.GL_VERTEX_SHADER
      binding.openGLConstants.GL_FRAGMENT_SHADER shouldBe GL20.GL_FRAGMENT_SHADER
      binding.openGLConstants.GL_INFO_LOG_LENGTH shouldBe GL20.GL_INFO_LOG_LENGTH
      binding.openGLConstants.GL_ZERO shouldBe GL11.GL_ZERO
      binding.openGLConstants.GL_ONE shouldBe GL11.GL_ONE
      binding.openGLConstants.GL_ONE_MINUS_SRC_ALPHA shouldBe GL11.GL_ONE_MINUS_SRC_ALPHA
      binding.openGLConstants.GL_ONE_MINUS_SRC_COLOR shouldBe GL11.GL_ONE_MINUS_SRC_COLOR
      binding.openGLConstants.GL_DST_COLOR shouldBe GL11.GL_DST_COLOR
      binding.openGLConstants.GL_TEXTURE1 shouldBe GL13.GL_TEXTURE1
      binding.openGLConstants.GL_TEXTURE0 shouldBe GL13.GL_TEXTURE0
      binding.openGLConstants.GL_FLOAT shouldBe GL11.GL_FLOAT
      binding.openGLConstants.GL_ARRAY_BUFFER_BINDING shouldBe GL15.GL_ARRAY_BUFFER_BINDING
      binding.openGLConstants.GL_ELEMENT_ARRAY_BUFFER_BINDING shouldBe GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING
      binding.openGLConstants.GL_CURRENT_PROGRAM shouldBe GL20.GL_CURRENT_PROGRAM
      binding.openGLConstants.GL_ACTIVE_TEXTURE shouldBe GL13.GL_ACTIVE_TEXTURE
      binding.openGLConstants.GL_TEXTURE_BINDING_2D shouldBe GL11.GL_TEXTURE_BINDING_2D
      binding.openGLConstants.GL_VERTEX_ATTRIB_ARRAY_ENABLED shouldBe GL20.GL_VERTEX_ATTRIB_ARRAY_ENABLED
      binding.openGLConstants.GL_SCISSOR_TEST shouldBe GL11.GL_SCISSOR_TEST
      binding.openGLConstants.GL_STENCIL_TEST shouldBe GL11.GL_STENCIL_TEST
      binding.openGLConstants.GL_DEPTH_TEST shouldBe GL11.GL_DEPTH_TEST
      binding.openGLConstants.GL_CULL_FACE shouldBe GL11.GL_CULL_FACE
      binding.openGLConstants.GL_BLEND shouldBe GL11.GL_BLEND
      binding.openGLConstants.GL_FRONT_FACE shouldBe GL11.GL_FRONT_FACE
      binding.openGLConstants.GL_COLOR_WRITEMASK shouldBe GL11.GL_COLOR_WRITEMASK
      binding.openGLConstants.GL_BLEND_SRC_RGB shouldBe GL14.GL_BLEND_SRC_RGB
      binding.openGLConstants.GL_BLEND_DST_RGB shouldBe GL14.GL_BLEND_DST_RGB
      binding.openGLConstants.GL_BLEND_SRC_ALPHA shouldBe GL14.GL_BLEND_SRC_ALPHA
      binding.openGLConstants.GL_BLEND_DST_ALPHA shouldBe GL14.GL_BLEND_DST_ALPHA
      binding.openGLConstants.GL_FRAMEBUFFER_BINDING shouldBe GL30.GL_FRAMEBUFFER_BINDING
      binding.openGLConstants.GL_VIEWPORT shouldBe GL11.GL_VIEWPORT
      binding.openGLConstants.GL_FALSE shouldBe GL11.GL_FALSE
      binding.openGLConstants.GL_TRUE shouldBe GL11.GL_TRUE
      binding.openGLConstants.GL_ARRAY_BUFFER shouldBe GL15.GL_ARRAY_BUFFER
      binding.openGLConstants.GL_ELEMENT_ARRAY_BUFFER shouldBe GL15.GL_ELEMENT_ARRAY_BUFFER
      binding.openGLConstants.GL_TEXTURE_WRAP_S shouldBe GL11.GL_TEXTURE_WRAP_S
      binding.openGLConstants.GL_CLAMP_TO_EDGE shouldBe GL12.GL_CLAMP_TO_EDGE
      binding.openGLConstants.GL_TEXTURE_WRAP_T shouldBe GL11.GL_TEXTURE_WRAP_T
      binding.openGLConstants.GL_FRAMEBUFFER shouldBe GL30.GL_FRAMEBUFFER
      binding.openGLConstants.GL_COLOR_ATTACHMENT0 shouldBe GL30.GL_COLOR_ATTACHMENT0
      binding.openGLConstants.GL_COLOR_BUFFER_BIT shouldBe GL11.GL_COLOR_BUFFER_BIT
      binding.openGLConstants.GL_CCW shouldBe GL11.GL_CCW
      binding.openGLConstants.GL_TRIANGLES shouldBe GL11.GL_TRIANGLES
      binding.openGLConstants.GL_UNSIGNED_SHORT shouldBe GL11.GL_UNSIGNED_SHORT
      binding.openGLConstants.GL_SRC_ALPHA shouldBe GL11.GL_SRC_ALPHA
      binding.openGLConstants.GL_DEPTH_BUFFER_BIT shouldBe GL11.GL_DEPTH_BUFFER_BIT
      binding.openGLConstants.GL_TRIANGLE_FAN shouldBe GL11.GL_TRIANGLE_FAN

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

