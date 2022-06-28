package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding._
import moe.brianhsu.live2d.enitiy.opengl.{BlendFunction, OpenGLBinding, RichOpenGLBinding}

object Profile {
  private var profile: Map[OpenGLBinding, Profile] = Map.empty
  private implicit val richOpenGLWrapper: OpenGLBinding => RichOpenGLBinding = RichOpenGLBinding.wrapOpenGLBinding

  def getInstance(implicit gl: OpenGLBinding): Profile = {

    profile.get(gl) match {
      case Some(profile) => profile
      case None =>
        this.profile += (gl -> new Profile)
        this.profile(gl)
    }
  }

}

/**
 * The Profile state object
 *
 * This is to save / restore various OpenGL parameters.

 * This class should no be created directly by client, client should use `getInstance` method
 * in Profile object.
 *
 * @param gl The OpenGL binding
 * @param richOpenGLWrapper The converter that will wrap OpenGLBinding to a RichOpenGLBinding
 */
class Profile private[opengl] (implicit gl: OpenGLBinding, richOpenGLWrapper: OpenGLBinding => RichOpenGLBinding) {

  import gl.constants._
  private var lastProgram: Int = 0
  private var lastVertexAttributes: Array[Boolean] = new Array[Boolean](4)
  private var lastScissorTest: Boolean = false
  private var lastStencilTest: Boolean = false
  private var lastDepthTest: Boolean = false
  private var lastCullFace: Boolean = false
  private var lastBlend: Boolean = false
  private var lastFrontFace: Int = 0
  private var lastColorWriteMask: ColorWriteMask = _

  private var lastArrayBufferBinding: Int = 0
  private var lastElementArrayBufferBinding: Int = 0
  private var lastTexture0Binding2D: Int = 0
  private var lastTexture1Binding2D: Int = 0
  private var lastActiveTexture: Int = 0
  private var lastBlending: BlendFunction = _
  private var isSaved: Boolean = false

  var lastFrameBufferBinding: Int = 0
  var lastViewPort: ViewPort = _

  def save(): Unit = {
    lastProgram = gl.openGLParameters[Int](GL_CURRENT_PROGRAM)
    lastVertexAttributes = gl.vertexAttributes
    lastScissorTest = gl.glIsEnabled(GL_SCISSOR_TEST)
    lastStencilTest = gl.glIsEnabled(GL_STENCIL_TEST)
    lastDepthTest = gl.glIsEnabled(GL_DEPTH_TEST)
    lastCullFace = gl.glIsEnabled(GL_CULL_FACE)
    lastBlend = gl.glIsEnabled(GL_BLEND)
    lastFrontFace = gl.openGLParameters[Int](GL_FRONT_FACE)
    lastColorWriteMask = gl.colorWriteMask
    lastArrayBufferBinding = gl.openGLParameters[Int](GL_ARRAY_BUFFER_BINDING)
    lastElementArrayBufferBinding = gl.openGLParameters[Int](GL_ELEMENT_ARRAY_BUFFER_BINDING)
    lastTexture1Binding2D = gl.textureBinding2D(GL_TEXTURE1)
    lastTexture0Binding2D = gl.textureBinding2D(GL_TEXTURE0)
    lastActiveTexture = gl.openGLParameters[Int](GL_ACTIVE_TEXTURE)

    lastBlending = gl.blendFunction

    lastFrameBufferBinding = gl.openGLParameters[Int](GL_FRAMEBUFFER_BINDING)
    lastViewPort = gl.viewPort
    isSaved = true
  }

  def restore(): Unit = {
    if (!isSaved) {
      throw new IllegalStateException(s"The profile=($this) state is not saved yet.")
    }

    gl.glUseProgram(lastProgram)
    gl.vertexAttributes = lastVertexAttributes

    gl.setCapabilityEnabled(GL_SCISSOR_TEST, lastScissorTest)
    gl.setCapabilityEnabled(GL_STENCIL_TEST, lastStencilTest)
    gl.setCapabilityEnabled(GL_DEPTH_TEST, lastDepthTest)
    gl.setCapabilityEnabled(GL_CULL_FACE, lastCullFace)
    gl.setCapabilityEnabled(GL_BLEND, lastBlend)

    gl.glFrontFace(lastFrontFace)
    gl.colorWriteMask = lastColorWriteMask

    gl.glBindBuffer(GL_ARRAY_BUFFER, lastArrayBufferBinding)
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lastElementArrayBufferBinding)

    gl.activeAndBinding2DTexture(GL_TEXTURE0, lastTexture0Binding2D)
    gl.activeAndBinding2DTexture(GL_TEXTURE1, lastTexture1Binding2D)
    gl.glActiveTexture(lastActiveTexture)

    gl.blendFunction = lastBlending
  }

}
