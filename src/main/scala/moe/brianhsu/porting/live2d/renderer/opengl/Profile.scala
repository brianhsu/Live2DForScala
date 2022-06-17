package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.RichOpenGLBinding._

object Profile {
  private var profile: Map[OpenGLBinding, Profile] = Map.empty

  def getInstance(implicit gl: OpenGLBinding): Profile = {
    profile.get(gl) match {
      case Some(profile) => profile
      case None =>
        this.profile += (gl -> new Profile())
        this.profile(gl)
    }
  }

}

class Profile private (implicit gl: OpenGLBinding) {

  import gl.constants._

  private var lastArrayBufferBinding: Option[Int] = None
  private var lastElementArrayBufferBinding: Option[Int] = None
  private var lastProgram: Option[Int] = None
  private var lastTexture0Binding2D: Option[Int] = None
  private var lastTexture1Binding2D: Option[Int] = None
  private var lastVertexAttributes: Array[Boolean] = new Array[Boolean](4)
  private var lastActiveTexture: Option[Int] = None
  private var lastScissorTest: Boolean = false
  private var lastStencilTest: Boolean = false
  private var lastDepthTest: Boolean = false
  private var lastCullFace: Boolean = false
  private var lastBlend: Boolean = false
  private var lastFrontFace: Option[Int] = None
  private var lastColorWriteMask: Option[ColorWriteMask] = None
  private var lastBlending: Option[BlendFunction] = None

  var lastFrameBufferBinding: Option[Int] = None
  var lastViewPort: Option[ViewPort] = None

  def save(): Unit = {
    lastArrayBufferBinding = Option(gl.openGLParameter(GL_ARRAY_BUFFER_BINDING))
    lastElementArrayBufferBinding = Option(gl.openGLParameter(GL_ELEMENT_ARRAY_BUFFER_BINDING))
    lastProgram = Option(gl.openGLParameter(GL_CURRENT_PROGRAM))
    lastActiveTexture = Option(gl.openGLParameter(GL_ACTIVE_TEXTURE))
    lastTexture0Binding2D = Option(gl.textureBinding2D(GL_TEXTURE0))
    lastTexture1Binding2D = Option(gl.textureBinding2D(GL_TEXTURE1))
    lastVertexAttributes = gl.vertexAttributes

    lastScissorTest = gl.glIsEnabled(GL_SCISSOR_TEST)
    lastStencilTest = gl.glIsEnabled(GL_STENCIL_TEST)
    lastDepthTest = gl.glIsEnabled(GL_DEPTH_TEST)
    lastCullFace = gl.glIsEnabled(GL_CULL_FACE)
    lastBlend = gl.glIsEnabled(GL_BLEND)
    lastFrontFace = Option(gl.openGLParameter(GL_FRONT_FACE))
    lastColorWriteMask = Option(gl.colorWriteMask)
    lastBlending = Option(gl.blendFunction)
    lastFrameBufferBinding = Option(gl.openGLParameter(GL_FRAMEBUFFER_BINDING))
    lastViewPort = Option(gl.viewPort)
  }

  def restore(): Unit = {
    gl.glUseProgram(lastProgram.get)
    gl.setVertexAttributes(lastVertexAttributes)

    gl.setCapabilityEnabled(GL_SCISSOR_TEST, lastScissorTest)
    gl.setCapabilityEnabled(GL_STENCIL_TEST, lastStencilTest)
    gl.setCapabilityEnabled(GL_DEPTH_TEST, lastDepthTest)
    gl.setCapabilityEnabled(GL_CULL_FACE, lastCullFace)
    gl.setCapabilityEnabled(GL_BLEND, lastBlend)

    gl.glFrontFace(lastFrontFace.get)
    gl.colorWriteMask = lastColorWriteMask.get

    gl.glBindBuffer(GL_ARRAY_BUFFER, lastArrayBufferBinding.get)
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lastElementArrayBufferBinding.get)

    gl.activeAndBinding2DTexture(GL_TEXTURE1, lastTexture1Binding2D.get)
    gl.activeAndBinding2DTexture(GL_TEXTURE0, lastTexture0Binding2D.get)

    gl.glActiveTexture(lastActiveTexture.get)
    gl.updateBlendFunc(lastBlending.get)
  }

}
