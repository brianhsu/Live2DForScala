package moe.brianhsu.utils.mock

import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, OpenGLConstants, RichOpenGLBinding}
import moe.brianhsu.utils.mock.OpenGLMock.Constants
import org.scalamock.scalatest.MockFactory
import scala.reflect.runtime.universe._

object OpenGLMock {
  object Constants extends OpenGLConstants {
    override val GL_TEXTURE_2D: Int = 1
    override val GL_RGBA: Int = 2
    override val GL_UNSIGNED_BYTE: Int = 3
    override val GL_TEXTURE_MIN_FILTER: Int = 4
    override val GL_LINEAR_MIPMAP_LINEAR: Int = 5
    override val GL_TEXTURE_MAG_FILTER: Int = 6
    override val GL_LINEAR: Int = 7
    override val GL_VERTEX_SHADER: Int = 8
    override val GL_FRAGMENT_SHADER: Int = 9
    override val GL_INFO_LOG_LENGTH: Int = 10
    override val GL_ZERO: Int = 11
    override val GL_ONE: Int = 12
    override val GL_ONE_MINUS_SRC_ALPHA: Int = 13
    override val GL_ONE_MINUS_SRC_COLOR: Int = 14
    override val GL_DST_COLOR: Int = 15
    override val GL_TEXTURE1: Int = 16
    override val GL_TEXTURE0: Int = 17
    override val GL_FLOAT: Int = 18
    override val GL_ARRAY_BUFFER_BINDING: Int = 19
    override val GL_ELEMENT_ARRAY_BUFFER_BINDING: Int = 20
    override val GL_CURRENT_PROGRAM: Int = 21
    override val GL_ACTIVE_TEXTURE: Int = 22
    override val GL_TEXTURE_BINDING_2D: Int = 23
    override val GL_VERTEX_ATTRIB_ARRAY_ENABLED: Int = 24
    override val GL_SCISSOR_TEST: Int = 25
    override val GL_STENCIL_TEST: Int = 26
    override val GL_DEPTH_TEST: Int = 27
    override val GL_CULL_FACE: Int = 28
    override val GL_BLEND: Int = 29
    override val GL_FRONT_FACE: Int = 30
    override val GL_COLOR_WRITEMASK: Int = 31
    override val GL_BLEND_SRC_RGB: Int = 32
    override val GL_BLEND_DST_RGB: Int = 33
    override val GL_BLEND_SRC_ALPHA: Int = 34
    override val GL_BLEND_DST_ALPHA: Int = 35
    override val GL_FRAMEBUFFER_BINDING: Int = 36
    override val GL_VIEWPORT: Int = 37
    override val GL_FALSE: Int = 38
    override val GL_TRUE: Int = 39
    override val GL_ARRAY_BUFFER: Int = 40
    override val GL_ELEMENT_ARRAY_BUFFER: Int = 41
    override val GL_TEXTURE_WRAP_S: Int = 42
    override val GL_CLAMP_TO_EDGE: Int = 43
    override val GL_TEXTURE_WRAP_T: Int = 44
    override val GL_FRAMEBUFFER: Int = 45
    override val GL_COLOR_ATTACHMENT0: Int = 46
    override val GL_COLOR_BUFFER_BIT: Int = 47
    override val GL_CCW: Int = 48
    override val GL_TRIANGLES: Int = 49
    override val GL_UNSIGNED_SHORT: Int = 50
    override val GL_SRC_ALPHA: Int = 51
    override val GL_DEPTH_BUFFER_BIT: Int = 52
    override val GL_TRIANGLE_FAN: Int = 53
  }

}
trait OpenGLMock {
  this: MockFactory =>

  def createOpenGLStub(): OpenGLBinding = stub[DummyOpenGLBinding]
  def createOpenGLMock(): OpenGLBinding = mock[DummyOpenGLBinding]

  abstract class DummyOpenGLBinding extends OpenGLBinding {
    override val constants: OpenGLConstants = Constants
  }

  def addDummyIntOpenGLParameter(richOpenGLBinding: RichOpenGLBinding, pname: Int, value: Int): Unit = {
    (richOpenGLBinding.openGLParameters(_: Int)(_: TypeTag[Int]))
      .when(pname, typeTag[Int])
      .returning(value)

  }

}
