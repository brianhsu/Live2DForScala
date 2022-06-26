package moe.brianhsu.porting.live2d.renderer.opengl

import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.BlendMode
import moe.brianhsu.live2d.enitiy.model.drawable.VertexInfo
import moe.brianhsu.live2d.enitiy.opengl.texture.{TextureColor, TextureManager}
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderRenderer
import moe.brianhsu.live2d.usecase.renderer.opengl.{ClippingRenderer, Profile}
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix

class AvatarRenderer(model: Live2DModel, textureManager: TextureManager, shaderRenderer: ShaderRenderer,
                     profile: Profile, clippingRenderer: ClippingRenderer)
                    (implicit gl: OpenGLBinding, wrapper: OpenGLBinding => RichOpenGLBinding) {

  import gl.constants._

  def this(model: Live2DModel)(implicit gl: OpenGLBinding, wrapper: OpenGLBinding => RichOpenGLBinding = RichOpenGLBinding.wrapOpenGLBinding) = {
    this(
      model, TextureManager.getInstance, ShaderRenderer.getInstance, Profile.getInstance,
      new ClippingRenderer(model, TextureManager.getInstance, ShaderRenderer.getInstance)
    )
  }

  def draw(avatar: Avatar, projection: ProjectionMatrix): Unit = {
    this.profile.save()
    this.drawModel(avatar.model.modelMatrix * projection)
    this.profile.restore()
  }

  private def drawModel(projection: ProjectionMatrix): Unit = {
    clippingRenderer.draw(profile)

    gl.preDraw()

    val sortedDrawable = model.sortedDrawables
    for (drawable <- sortedDrawable.filter(_.dynamicFlags.isVisible)) {
      val clippingContextBufferForDraw = clippingRenderer.clippingContextBufferForDraw(drawable)
      val textureFile = model.textureFiles(drawable.textureIndex)
      val textureInfo = textureManager.loadTexture(textureFile)

      drawMesh(
        clippingContextBufferForDraw,
        textureInfo.textureId,
        drawable.isCulling,
        drawable.vertexInfo,
        drawable.opacity,
        drawable.constantFlags.blendMode,
        drawable.constantFlags.isInvertedMask,
        projection
      )
    }
  }

  private def drawMesh(clippingContextBufferForDraw: Option[ClippingContext],
                       drawTextureId: Int, isCulling: Boolean, vertexInfo: VertexInfo,
                       opacity: Float, colorBlendMode: BlendMode,
                       invertedMask: Boolean, projection: ProjectionMatrix): Unit ={

    gl.setCapabilityEnabled(GL_CULL_FACE, isCulling)
    gl.glFrontFace(GL_CCW)

    val modelColorRGBA = TextureColor(1.0f, 1.0f, 1.0f, opacity)

    shaderRenderer.renderDrawable(
      clippingContextBufferForDraw,
      clippingRenderer.offscreenFrameHolder,
      drawTextureId,
      vertexInfo.vertexArrayDirectBuffer, vertexInfo.uvArrayDirectBuffer,
      colorBlendMode, modelColorRGBA, projection,
      invertedMask
    )

    gl.glDrawElements(GL_TRIANGLES, vertexInfo.numberOfTriangleIndex, GL_UNSIGNED_SHORT, vertexInfo.indexArrayDirectBuffer)
    gl.glUseProgram(0)
  }

}
