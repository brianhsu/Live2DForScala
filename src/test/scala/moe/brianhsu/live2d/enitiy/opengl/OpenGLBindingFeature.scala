package moe.brianhsu.live2d.enitiy.opengl

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

import java.nio.{ByteBuffer, FloatBuffer}

class OpenGLBindingFeature extends AnyFeatureSpec with Matchers with GivenWhenThen {
  Feature("Read shader program compile error log") {
    Scenario("Cannot get log length") {
      Given("an OpenGL binding that has shader error log length = 0")
      val binding = new DummyGLBinding {
        override val GL_INFO_LOG_LENGTH = 1
        override def glGetShaderiv(shader: Int, pname: Int, params: Array[Int]): Unit = {
          if (pname == GL_INFO_LOG_LENGTH) {
            params(0) = 0
          } else {
            throw new UnsupportedOperationException()
          }
        }
      }

      When("read compile error log from OpenGL binding")
      val programId = 123
      val errorLogHolder = binding.readShaderCompileError(programId)

      Then("it should be None")
      errorLogHolder shouldBe None
    }

    Scenario("There is no error") {
      pending
    }

    Scenario("There is an error") {
      pending
    }

  }
  Feature("Read shader program link error log") {
    Scenario("Cannot get log length") {
      pending
    }

    Scenario("There is no error") {
      pending
    }

    Scenario("There is an error") {
      pending
    }

  }

  class DummyGLBinding extends OpenGLBinding {
    override def glGenTextures(n: Int, textures: Array[Int]): Unit = ???
    override def glBindTexture(target: Int, texture: Int): Unit = ???
    override def glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: ByteBuffer): Unit = ???
    override def glGenerateMipmap(target: Int): Unit = ???
    override def glTexParameteri(target: Int, pname: Int, param: Int): Unit = ???
    override def glUseProgram(program: Int): Unit = ???
    override def glCompileShader(shader: Int): Unit = ???
    override def glDeleteProgram(program: Int): Unit = ???
    override def glCreateProgram(): Int = ???
    override def glAttachShader(program: Int, shader: Int): Unit = ???
    override def glDetachShader(program: Int, shader: Int): Unit = ???
    override def glDeleteShader(shader: Int): Unit = ???
    override def glLinkProgram(program: Int): Unit = ???
    override def glCreateShader(`type`: Int): Int = ???
    override def glShaderSource(shader: Int, count: Int, string: Array[String]): Unit = ???
    override def glGetProgramiv(program: Int, pname: Int, params: Array[Int]): Unit = ???
    override def glGetProgramInfoLog(program: Int, bufSize: Int, infoLog: ByteBuffer): Unit = ???
    override def glGetShaderiv(shader: Int, pname: Int, params: Array[Int]): Unit = ???
    override def glGetShaderInfoLog(shader: Int, bufSize: Int, infoLog: ByteBuffer): Unit = ???
    override def glGetAttribLocation(program: Int, name: String): Int = ???
    override def glGetUniformLocation(program: Int, name: String): Int = ???
    override def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: Array[Float]): Unit = ???
    override def glUniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float): Unit = ???
    override def glActiveTexture(texture: Int): Unit = ???
    override def glUniform1i(location: Int, v0: Int): Unit = ???
    override def glEnableVertexAttribArray(index: Int): Unit = ???
    override def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: ByteBuffer): Unit = ???
    override def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: FloatBuffer): Unit = ???
    override def glBlendFuncSeparate(sfactorRGB: Int, dfactorRGB: Int, sfactorAlpha: Int, dfactorAlpha: Int): Unit = ???
    override def glGetIntegerv(pname: Int, params: Array[Int], params_offset: Int): Unit = ???
    override def glGetVertexAttribiv(index: Int, pname: Int, params: Array[Int], params_offset: Int): Unit = ???
    override def glIsEnabled(cap: Int): Boolean = ???
    override def glGetBooleanv(pname: Int, data: Array[Byte]): Unit = ???
    override def glDisableVertexAttribArray(index: Int): Unit = ???
    override def glEnable(cap: Int): Unit = ???
    override def glDisable(cap: Int): Unit = ???
    override def glFrontFace(mode: Int): Unit = ???
    override def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean): Unit = ???
    override def glBindBuffer(target: Int, buffer: Int): Unit = ???
    override def glGetIntegerv(pname: Int, params: Array[Int]): Unit = ???
    override def glGenFramebuffers(n: Int, framebuffers: Array[Int]): Unit = ???
    override def glBindFramebuffer(target: Int, framebuffer: Int): Unit = ???
    override def glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int): Unit = ???
    override def glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = ???
    override def glClear(mask: Int): Unit = ???
    override def glDeleteTextures(n: Int, textures: Array[Int]): Unit = ???
    override def glDeleteFramebuffers(n: Int, framebuffers: Array[Int]): Unit = ???
    override def glDrawElements(mode: Int, count: Int, `type`: Int, indices: ByteBuffer): Unit = ???
    override def glClearDepth(depth: Double): Unit = ???
    override def glBlendFunc(sfactor: Int, dfactor: Int): Unit = ???
    override def glDrawArrays(mode: Int, first: Int, count: Int): Unit = ???
    override def newDirectFloatBuffer(float: Array[Float]): FloatBuffer = ???
    override def glViewport(x: Int, y: Int, w: Int, h: Int): Unit = ???

    override def GL_TEXTURE_2D: Int = ???
    override def GL_RGBA: Int = ???
    override def GL_UNSIGNED_BYTE: Int = ???
    override def GL_TEXTURE_MIN_FILTER: Int = ???
    override def GL_LINEAR_MIPMAP_LINEAR: Int = ???
    override def GL_TEXTURE_MAG_FILTER: Int = ???
    override def GL_LINEAR: Int = ???
    override def GL_VERTEX_SHADER: Int = ???
    override def GL_FRAGMENT_SHADER: Int = ???
    override def GL_INFO_LOG_LENGTH: Int = ???
    override def GL_ZERO: Int = ???
    override def GL_ONE: Int = ???
    override def GL_ONE_MINUS_SRC_ALPHA: Int = ???
    override def GL_ONE_MINUS_SRC_COLOR: Int = ???
    override def GL_DST_COLOR: Int = ???
    override def GL_TEXTURE1: Int = ???
    override def GL_TEXTURE0: Int = ???
    override def GL_FLOAT: Int = ???
    override def GL_ARRAY_BUFFER_BINDING: Int = ???
    override def GL_ELEMENT_ARRAY_BUFFER_BINDING: Int = ???
    override def GL_CURRENT_PROGRAM: Int = ???
    override def GL_ACTIVE_TEXTURE: Int = ???
    override def GL_TEXTURE_BINDING_2D: Int = ???
    override def GL_VERTEX_ATTRIB_ARRAY_ENABLED: Int = ???
    override def GL_SCISSOR_TEST: Int = ???
    override def GL_STENCIL_TEST: Int = ???
    override def GL_DEPTH_TEST: Int = ???
    override def GL_CULL_FACE: Int = ???
    override def GL_BLEND: Int = ???
    override def GL_FRONT_FACE: Int = ???
    override def GL_COLOR_WRITEMASK: Int = ???
    override def GL_BLEND_SRC_RGB: Int = ???
    override def GL_BLEND_DST_RGB: Int = ???
    override def GL_BLEND_SRC_ALPHA: Int = ???
    override def GL_BLEND_DST_ALPHA: Int = ???
    override def GL_FRAMEBUFFER_BINDING: Int = ???
    override def GL_VIEWPORT: Int = ???
    override def GL_FALSE: Int = ???
    override def GL_TRUE: Int = ???
    override def GL_ARRAY_BUFFER: Int = ???
    override def GL_ELEMENT_ARRAY_BUFFER: Int = ???
    override def GL_TEXTURE_WRAP_S: Int = ???
    override def GL_CLAMP_TO_EDGE: Int = ???
    override def GL_TEXTURE_WRAP_T: Int = ???
    override def GL_FRAMEBUFFER: Int = ???
    override def GL_COLOR_ATTACHMENT0: Int = ???
    override def GL_COLOR_BUFFER_BIT: Int = ???
    override def GL_CCW: Int = ???
    override def GL_TRIANGLES: Int = ???
    override def GL_UNSIGNED_SHORT: Int = ???
    override def GL_SRC_ALPHA: Int = ???
    override def GL_DEPTH_BUFFER_BIT: Int = ???
    override def GL_TRIANGLE_FAN: Int = ???
  }

}
