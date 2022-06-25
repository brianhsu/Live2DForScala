package moe.brianhsu.live2d.usecase.renderer.opengl

import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.{Drawable, VertexInfo}
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureManager
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingManager

class ClippingRenderer(model: Live2DModel, textureManager: TextureManager, shaderRenderer: ShaderRenderer,
                       initClippingManager: Option[ClippingManager], val offscreenFrameHolder: Option[OffscreenFrame])
                      (implicit gl: OpenGLBinding, wrapper: OpenGLBinding => RichOpenGLBinding) {

  import gl.constants._

  private var clippingManagerHolder: Option[ClippingManager] = initClippingManager

  def this(model: Live2DModel, textureManager: TextureManager, shaderRenderer: ShaderRenderer)
          (implicit gl: OpenGLBinding, wrapper: OpenGLBinding => RichOpenGLBinding = RichOpenGLBinding.wrapOpenGLBinding) = {

    this(
      model, textureManager, shaderRenderer,
      ClippingManager.fromLive2DModel(model),
      ClippingManager.fromLive2DModel(model).map { _ =>
        OffscreenFrame.getInstance(
          ClippingManager.MaskBufferSize,
          ClippingManager.MaskBufferSize
        )
      }
    )
  }

  def this(model: Live2DModel, textureManager: TextureManager,
           shaderRenderer: ShaderRenderer, initClippingManager: Option[ClippingManager])
          (implicit gl: OpenGLBinding, wrapper: OpenGLBinding => RichOpenGLBinding) = {

    this(
      model, textureManager, shaderRenderer,
      initClippingManager,
      initClippingManager.map { _ =>
        OffscreenFrame.getInstance(
          ClippingManager.MaskBufferSize,
          ClippingManager.MaskBufferSize
        )
      }
    )
  }

  def clippingContextBufferForDraw(drawable: Drawable): Option[ClippingContext] = {
    clippingManagerHolder.flatMap (_.getClippingContextByDrawable(drawable))
  }

  def draw(profile: Profile): Unit = {
    clippingManagerHolder = clippingManagerHolder.map(_.updateContextListForMask())
    clippingManagerHolder
      .filter(_.usingClipCount > 0)
      .foreach(manager => drawClipping(manager.contextListForMask, profile))
  }

  private def drawClipping(contextListForMask: List[ClippingContext], profile: Profile): Unit = {
    gl.glViewport(0, 0, ClippingManager.MaskBufferSize, ClippingManager.MaskBufferSize)
    gl.preDraw()

    this.offscreenFrameHolder.foreach(_.beginDraw(profile.lastFrameBufferBinding))

    for (clipContext <- contextListForMask) {
      for (maskDrawable <- clipContext.vertexPositionChangedMaskDrawable) {
        val textureFile = model.textureFiles(maskDrawable.textureIndex)
        val textureInfo = textureManager.loadTexture(textureFile)
        this.drawClippingMesh(clipContext, textureInfo.textureId, maskDrawable.isCulling, maskDrawable.vertexInfo)
      }
    }

    this.offscreenFrameHolder.foreach(_.endDraw())
    gl.viewPort = profile.lastViewPort
  }

  private def drawClippingMesh(clippingContextBufferForMask: ClippingContext,
                               textureId: Int, isCulling: Boolean,
                               vertexInfo: VertexInfo): Unit ={

    gl.setCapabilityEnabled(GL_CULL_FACE, isCulling)
    gl.glFrontFace(GL_CCW)

    shaderRenderer.renderMask(
      clippingContextBufferForMask, textureId,
      vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer
    )

    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)
    gl.glUseProgram(0)
  }

}
