package moe.brianhsu.porting.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.BlendMode
import moe.brianhsu.live2d.enitiy.opengl.{OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.enitiy.opengl.shader.Blending
import moe.brianhsu.live2d.usecase.renderer.opengl.OffscreenFrame
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader._
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureColor
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix

import java.nio.ByteBuffer

object ShaderRenderer {
  private var shaderRendererHolder: Map[OpenGLBinding, ShaderRenderer] = Map.empty

  def getInstance(implicit gl: OpenGLBinding): ShaderRenderer = {
    shaderRendererHolder.get(gl) match {
      case Some(renderer) => renderer
      case None =>
        this.shaderRendererHolder += (gl -> new ShaderRenderer()(gl, {x: OpenGLBinding => new RichOpenGLBinding(x)}))
        this.shaderRendererHolder(gl)
    }
  }
}

class ShaderRenderer private (implicit gl: OpenGLBinding, richOpenGLWrapper: OpenGLBinding => RichOpenGLBinding) {

  import gl.constants._

  private val setupMaskShader = new SetupMaskShader
  private val normalShader = new NormalShader
  private val maskedShader = new MaskedShader
  private val invertedMaskedShader = new InvertedMaskedShader


  def renderDrawable(clippingContextBufferForDraw: Option[ClippingContext], offscreenFrameHolder: Option[OffscreenFrame], textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer, colorBlendMode: BlendMode, baseColor: TextureColor, projection: ProjectionMatrix, invertedMask: Boolean): Unit = {
    val drawClippingContextHolder = clippingContextBufferForDraw
    val masked = drawClippingContextHolder.isDefined // この描画オブジェクトはマスク対象か
    val shader = masked match {
      case true if invertedMask => invertedMaskedShader
      case true => maskedShader
      case false => normalShader
    }

    shader.useProgram()
    gl.setGlVertexInfo(vertexArray, uvArray, shader.attributePositionLocation, shader.attributeTexCoordLocation)

    for (context <- drawClippingContextHolder) {
      offscreenFrameHolder.foreach { buffer =>
        gl.setGlTexture(GL_TEXTURE1, buffer.frameBufferId, shader.samplerTexture1Location, 1)
        gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, transpose = false, context.matrixForDraw.elements)
        gl.setGlColorChannel(context.layout.channelColor, shader.uniformChannelFlagLocation)
      }
    }

    //テクスチャ設定
    gl.setGlTexture(GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)

    //座標変換
    gl.glUniformMatrix4fv(shader.uniformMatrixLocation, 1, transpose = false, projection.elements)
    gl.glUniform4f(shader.uniformBaseColorLocation, baseColor.red, baseColor.green, baseColor.blue, baseColor.alpha)
    gl.setGlBlend(Blending(colorBlendMode))
  }

  def renderMask(context: ClippingContext, textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer): Unit = {
    val shader = setupMaskShader

    shader.useProgram()

    gl.setGlTexture(GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)
    gl.setGlVertexInfo(vertexArray, uvArray, shader.attributePositionLocation, shader.attributeTexCoordLocation)
    gl.setGlColorChannel(context.layout.channelColor, shader.uniformChannelFlagLocation)
    gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, transpose = false, context.matrixForMask.elements)

    val rect = context.layout.bounds

    gl.glUniform4f(
      shader.uniformBaseColorLocation,
      rect.leftX * 2.0f - 1.0f,
      rect.bottomY * 2.0f - 1.0f,
      rect.rightX * 2.0f - 1.0f,
      rect.topY * 2.0f - 1.0f
    )

    gl.setGlBlend(Blending(GL_ZERO, GL_ONE_MINUS_SRC_COLOR, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA))
  }

}
