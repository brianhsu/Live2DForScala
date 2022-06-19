package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.BlendMode
import moe.brianhsu.live2d.enitiy.model.drawable.VertexInfo
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.Profile
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.{TextureColor, TextureManager}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.{ClippingContext, ClippingManager}
import moe.brianhsu.porting.live2d.renderer.opengl.shader.ShaderRenderer

class Renderer(var model: Live2DModel)(implicit gl: OpenGLBinding) {
  import gl.constants._

  private var projection: Option[ProjectionMatrix] = None
  private val textureManager = TextureManager.getInstance
  private val shaderRenderer = ShaderRenderer.getInstance
  private val profile = Profile.getInstance
  private var isCulling: Boolean = false
  private var clippingContextBufferForMask: Option[ClippingContext] = None
  private var clippingContextBufferForDraw: Option[ClippingContext] = None
  private val clippingManagerHolder: Option[ClippingManager] = if (model.containMaskedDrawables) {
    Some(new ClippingManager(model, textureManager))
  } else {
    None
  }

  var offscreenBufferHolder: Option[OffscreenFrame] = clippingManagerHolder.map(manager => OffscreenFrame.getInstance(manager.clippingMaskBufferSize, manager.clippingMaskBufferSize))

  def getProjection: Option[GeneralMatrix] = projection
  def getClippingContextBufferForDraw: Option[ClippingContext] = clippingContextBufferForDraw
  def getClippingContextBufferForMask: Option[ClippingContext] = clippingContextBufferForMask
  def setClippingContextBufferForMask(clip: Option[ClippingContext]): Unit = {
    clippingContextBufferForMask = clip
  }

  def setClippingContextBufferForDraw(context: Option[ClippingContext]): Unit = {
    clippingContextBufferForDraw = context
  }

  def setIsCulling(isCulling: Boolean): Unit = {
    this.isCulling = isCulling
  }

  private def setProjection(projection: ProjectionMatrix): Unit = {
    this.projection = Some(projection)
  }

  def draw(avatar: Avatar, projection: ProjectionMatrix): Unit = {
    this.setProjection(avatar.model.modelMatrix * projection)
    this.profile.save()
    this.drawModel()
    this.profile.restore()
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

  def postDraw(): Unit = {}

  def drawMesh(drawTextureId: Int, vertexInfo: VertexInfo,
               opacity: Float, colorBlendMode: BlendMode, invertedMask: Boolean): Unit ={

    if (isCulling) {
      gl.glEnable(GL_CULL_FACE)
    } else {
      gl.glDisable(GL_CULL_FACE)
    }

    gl.glFrontFace(GL_CCW)

    val modelColorRGBA = getClippingContextBufferForMask match {
      case None => TextureColor(1.0f, 1.0f, 1.0f, opacity)
      case _    => TextureColor()
    }

    shaderRenderer.render(
      this, drawTextureId,
      vertexInfo.vertexArrayDirectBuffer,
      vertexInfo.uvArrayDirectBuffer,
      colorBlendMode,
      modelColorRGBA,
      projection.getOrElse(new ProjectionMatrix),
      invertedMask
    )

    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)

    gl.glUseProgram(0)
    setClippingContextBufferForDraw(None)
    setClippingContextBufferForMask(None)
  }

  def drawModel(): Unit = {
    clippingManagerHolder.foreach { manager =>
      preDraw()
      manager.setupClippingContext(this, profile.lastFrameBufferBinding, profile.lastViewPort)
    }

    preDraw()

    val sortedDrawable = model.sortedDrawables
    for (drawable <- sortedDrawable.filter(_.dynamicFlags.isVisible)) {

      val clipContext: Option[ClippingContext] = clippingManagerHolder.flatMap(_.getClippingContextByDrawable(drawable))
      setClippingContextBufferForDraw(clipContext)

      setIsCulling(drawable.isCulling)
      val textureFile = model.textureFiles(drawable.textureIndex)
      val textureInfo = textureManager.loadTexture(textureFile)

      drawMesh(
        textureInfo.textureId,
        drawable.vertexInfo,
        drawable.opacity,
        drawable.constantFlags.blendMode,
        drawable.constantFlags.isInvertedMask
      )
    }
    postDraw()
  }

  def destroy(): Unit = {
    offscreenBufferHolder.foreach(_.destroy())
  }

}
