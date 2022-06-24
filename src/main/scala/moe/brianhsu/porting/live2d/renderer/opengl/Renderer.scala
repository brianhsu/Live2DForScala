package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.{BlendMode, Normal}
import moe.brianhsu.live2d.enitiy.model.drawable.VertexInfo
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.live2d.usecase.renderer.opengl.{OffscreenFrame, Profile}
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.{TextureColor, TextureManager}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingManager

class Renderer(var model: Live2DModel)(implicit gl: OpenGLBinding) {
  import gl.constants._

  private implicit val wrapper: OpenGLBinding => RichOpenGLBinding = RichOpenGLBinding.wrapOpenGLBinding

  private var projection: Option[ProjectionMatrix] = None
  private val textureManager = TextureManager.getInstance
  private val shaderRenderer = ShaderRenderer.getInstance
  private val profile = Profile.getInstance
  private var clippingContextBufferForMask: Option[ClippingContext] = None
  private var clippingContextBufferForDraw: Option[ClippingContext] = None
  private var clippingManagerHolder: Option[ClippingManager] = ClippingManager.fromLive2DModel(model)
  private val offscreenBufferHolder: Option[OffscreenFrame] = clippingManagerHolder.map(_ => OffscreenFrame.getInstance(ClippingManager.MaskBufferSize, ClippingManager.MaskBufferSize))

  private def setProjection(projection: ProjectionMatrix): Unit = {
    this.projection = Some(projection)
  }

  def draw(avatar: Avatar, projection: ProjectionMatrix): Unit = {
    this.setProjection(avatar.model.modelMatrix * projection)
    this.profile.save()
    this.drawModel()
    this.profile.restore()
  }

  def beginDrawOffscreenFrame(lastFrameBufferBinding: Int): Unit = {
    this.offscreenBufferHolder.foreach(_.beginDraw(lastFrameBufferBinding))
  }

  def endDrawOffscreenFrame(): Unit = {
    this.offscreenBufferHolder.foreach(_.endDraw())
  }

  def preDraw(): Unit = {
    gl.glDisable(GL_SCISSOR_TEST)
    gl.glDisable(GL_STENCIL_TEST)
    gl.glDisable(GL_DEPTH_TEST)
    gl.glEnable(GL_BLEND)
    gl.glColorMask(red = true, green = true, blue = true, alpha = true)
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    gl.glBindBuffer(GL_ARRAY_BUFFER, 0) //前にバッファがバインドされていたら破棄する必要がある
  }

  def drawMesh(drawTextureId: Int, isCulling: Boolean, vertexInfo: VertexInfo,
               opacity: Float, colorBlendMode: BlendMode, invertedMask: Boolean): Unit ={

    if (isCulling) {
      gl.glEnable(GL_CULL_FACE)
    } else {
      gl.glDisable(GL_CULL_FACE)
    }

    gl.glFrontFace(GL_CCW)

    val modelColorRGBA = clippingContextBufferForMask match {
      case None => TextureColor(1.0f, 1.0f, 1.0f, opacity)
      case _    => TextureColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    this.clippingContextBufferForMask match {
      case Some(context) =>
        shaderRenderer.renderMask(
          context, drawTextureId,
          vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer
        )
      case None =>
        shaderRenderer.renderDrawable(
          clippingContextBufferForDraw, offscreenBufferHolder, drawTextureId,
          vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer,
          colorBlendMode, modelColorRGBA, projection.getOrElse(new ProjectionMatrix), invertedMask
        )
    }


    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)

    gl.glUseProgram(0)
    this.clippingContextBufferForDraw = None
    this.clippingContextBufferForMask = None
  }
  def drawClippingMesh(clippingContextBufferForMask: ClippingContext,
                       drawTextureId: Int, isCulling: Boolean, vertexInfo: VertexInfo): Unit ={

    if (isCulling) {
      gl.glEnable(GL_CULL_FACE)
    } else {
      gl.glDisable(GL_CULL_FACE)
    }

    gl.glFrontFace(GL_CCW)

    shaderRenderer.renderMask(
      clippingContextBufferForMask, drawTextureId,
      vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer
    )

    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)

    gl.glUseProgram(0)
    this.clippingContextBufferForDraw = None
  }

  def drawModel(): Unit = {
    clippingManagerHolder = clippingManagerHolder.map(_.updateContextListForMask())
    clippingManagerHolder
      .filter(_.usingClipCount > 0)
      .foreach(manager => drawClipping(manager.contextListForMask))

    preDraw()

    val sortedDrawable = model.sortedDrawables
    for (drawable <- sortedDrawable.filter(_.dynamicFlags.isVisible)) {
      this.clippingContextBufferForDraw = clippingManagerHolder.flatMap(_.getClippingContextByDrawable(drawable))
      val textureFile = model.textureFiles(drawable.textureIndex)
      val textureInfo = textureManager.loadTexture(textureFile)

      drawMesh(
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

    this.preDraw()
    this.beginDrawOffscreenFrame(profile.lastFrameBufferBinding)

    for (clipContext <- contextListForMask) {

      for (maskDrawable <- clipContext.vertexPositionChangedMaskDrawable) {
        val textureFile = model.textureFiles(maskDrawable.textureIndex)
        val textureInfo = textureManager.loadTexture(textureFile)
        this.drawClippingMesh(clipContext, textureInfo.textureId, maskDrawable.isCulling, maskDrawable.vertexInfo)
      }
    }

    // --- 後処理 ---
    this.endDrawOffscreenFrame()
    gl.viewPort = profile.lastViewPort
  }
}
