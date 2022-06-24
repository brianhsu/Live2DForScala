package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.BlendMode
import moe.brianhsu.live2d.enitiy.model.drawable.VertexInfo
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.{TextureColor, TextureManager}
import moe.brianhsu.live2d.usecase.renderer.opengl.{OffscreenFrame, Profile}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingManager

class Renderer(var model: Live2DModel)(implicit gl: OpenGLBinding) {
  import gl.constants._

  private implicit val wrapper: OpenGLBinding => RichOpenGLBinding = RichOpenGLBinding.wrapOpenGLBinding

  private val textureManager = TextureManager.getInstance
  private val shaderRenderer = ShaderRenderer.getInstance
  private val profile = Profile.getInstance

  private var projection: Option[ProjectionMatrix] = None
  private var clippingManagerHolder: Option[ClippingManager] = ClippingManager.fromLive2DModel(model)
  private val offscreenBufferHolder: Option[OffscreenFrame] = clippingManagerHolder.map(_ => OffscreenFrame.getInstance(ClippingManager.MaskBufferSize, ClippingManager.MaskBufferSize))

  def draw(avatar: Avatar, projection: ProjectionMatrix): Unit = {
    this.projection = Some(avatar.model.modelMatrix * projection)
    this.profile.save()
    this.drawModel()
    this.profile.restore()
  }

  def drawMesh(clippingContextBufferForDraw: Option[ClippingContext], drawTextureId: Int, isCulling: Boolean, vertexInfo: VertexInfo,
               opacity: Float, colorBlendMode: BlendMode, invertedMask: Boolean): Unit ={

    gl.setCapabilityEnabled(GL_CULL_FACE, isCulling)
    gl.glFrontFace(GL_CCW)

    val modelColorRGBA = TextureColor(1.0f, 1.0f, 1.0f, opacity)

    shaderRenderer.renderDrawable(
      clippingContextBufferForDraw, offscreenBufferHolder, drawTextureId,
      vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer,
      colorBlendMode, modelColorRGBA, projection.getOrElse(new ProjectionMatrix),
      invertedMask
    )

    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)
    gl.glUseProgram(0)
  }

  def drawClippingMesh(clippingContextBufferForMask: ClippingContext,
                       drawTextureId: Int, isCulling: Boolean, vertexInfo: VertexInfo): Unit ={

    gl.setCapabilityEnabled(GL_CULL_FACE, isCulling)
    gl.glFrontFace(GL_CCW)

    shaderRenderer.renderMask(
      clippingContextBufferForMask, drawTextureId,
      vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer
    )

    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)
    gl.glUseProgram(0)
  }

  def drawModel(): Unit = {
    clippingManagerHolder = clippingManagerHolder.map(_.updateContextListForMask())
    clippingManagerHolder
      .filter(_.usingClipCount > 0)
      .foreach(manager => drawClipping(manager.contextListForMask))

    gl.preDraw()

    val sortedDrawable = model.sortedDrawables
    for (drawable <- sortedDrawable.filter(_.dynamicFlags.isVisible)) {
      val clippingContextBufferForDraw = clippingManagerHolder.flatMap(_.getClippingContextByDrawable(drawable))
      val textureFile = model.textureFiles(drawable.textureIndex)
      val textureInfo = textureManager.loadTexture(textureFile)

      drawMesh(
        clippingContextBufferForDraw,
        textureInfo.textureId,
        drawable.isCulling,
        drawable.vertexInfo,
        drawable.opacity,
        drawable.constantFlags.blendMode,
        drawable.constantFlags.isInvertedMask
      )
    }
  }

  private def drawClipping(contextListForMask: List[ClippingContext]): Unit = {
    gl.glViewport(0, 0, ClippingManager.MaskBufferSize, ClippingManager.MaskBufferSize)
    gl.preDraw()

    this.offscreenBufferHolder.foreach(_.beginDraw(profile.lastFrameBufferBinding))

    for (clipContext <- contextListForMask) {

      for (maskDrawable <- clipContext.vertexPositionChangedMaskDrawable) {
        val textureFile = model.textureFiles(maskDrawable.textureIndex)
        val textureInfo = textureManager.loadTexture(textureFile)
        this.drawClippingMesh(clipContext, textureInfo.textureId, maskDrawable.isCulling, maskDrawable.vertexInfo)
      }
    }

    this.offscreenBufferHolder.foreach(_.endDraw())
    gl.viewPort = profile.lastViewPort
  }
}
