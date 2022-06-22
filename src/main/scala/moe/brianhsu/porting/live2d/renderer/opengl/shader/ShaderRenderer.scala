package moe.brianhsu.porting.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.OffscreenFrame
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.{AvatarShader, InvertedMaskedShader, MaskedShader, NormalShader, SetupMaskShader}
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureColor
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix

import java.nio.ByteBuffer

object ShaderRenderer {
  private var shaderRendererHolder: Map[OpenGLBinding, ShaderRenderer] = Map.empty

  def getInstance(implicit gl: OpenGLBinding): ShaderRenderer = {
    shaderRendererHolder.get(gl) match {
      case Some(renderer) => renderer
      case None =>
        this.shaderRendererHolder += (gl -> new ShaderRenderer())
        this.shaderRendererHolder(gl)
    }
  }
}

class ShaderRenderer private (implicit gl: OpenGLBinding) {

  import gl.constants._

  private val setupMaskShader = new SetupMaskShader
  private val normalShader = new NormalShader
  private val maskedShader = new MaskedShader
  private val invertedMaskedShader = new InvertedMaskedShader

  case class Blending(srcColor: Int, dstColor: Int, srcAlpha: Int, dstAlpha: Int)

  def renderDrawable(clippingContextBufferForDraw: Option[ClippingContext], offscreenFrameHolder: Option[OffscreenFrame], textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer, colorBlendMode: BlendMode, baseColor: TextureColor, projection: ProjectionMatrix, invertedMask: Boolean): Unit = {
    val drawClippingContextHolder = clippingContextBufferForDraw
    val masked = drawClippingContextHolder.isDefined // この描画オブジェクトはマスク対象か
    val shader = masked match {
      case true if invertedMask => invertedMaskedShader
      case true => maskedShader
      case false => normalShader
    }

    val blending = colorBlendMode match {
      case Normal => Blending(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
      case AdditiveBlend => Blending(GL_ONE, GL_ONE, GL_ZERO, GL_ONE)
      case MultiplicativeBlend => Blending(GL_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE)
    }

    shader.useProgram()

    setGlVertexInfo(vertexArray, uvArray, shader)

    for (context <- drawClippingContextHolder) {
      offscreenFrameHolder.foreach { buffer =>
        setGlTexture(GL_TEXTURE1, buffer.frameBufferId, shader.samplerTexture1Location, 1)
        gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, transpose = false, context.matrixForDraw.elements)
        setGlColorChannel(context, shader)
      }
    }

    //テクスチャ設定
    setGlTexture(GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)

    //座標変換
    gl.glUniformMatrix4fv(shader.uniformMatrixLocation, 1, transpose = false, projection.elements)
    gl.glUniform4f(shader.uniformBaseColorLocation, baseColor.red, baseColor.green, baseColor.blue, baseColor.alpha)
    setGlBlend(blending)
  }

  def renderMask(context: ClippingContext, textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer): Unit = {
    val shader = setupMaskShader

    shader.useProgram()

    setGlTexture(GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)
    setGlVertexInfo(vertexArray, uvArray, shader)
    setGlColorChannel(context, shader)

    gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, transpose = false, context.matrixForMask.elements)

    val rect = context.layout.bounds

    gl.glUniform4f(
      shader.uniformBaseColorLocation,
      rect.leftX * 2.0f - 1.0f,
      rect.bottomY * 2.0f - 1.0f,
      rect.rightX * 2.0f - 1.0f,
      rect.topY * 2.0f - 1.0f
    )

    setGlBlend(Blending(GL_ZERO, GL_ONE_MINUS_SRC_COLOR, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA))
  }

  private def setGlColorChannel(context: ClippingContext, shader: AvatarShader): Unit = {
    val colorChannel = context.layout.channelColor
    gl.glUniform4f(shader.uniformChannelFlagLocation, colorChannel.red, colorChannel.green, colorChannel.blue, colorChannel.alpha)
  }

  private def setGlTexture(textureUnit: Int, textureId: Int, variable: Int, variableValue: Int): Unit = {
    gl.glActiveTexture(textureUnit)
    gl.glBindTexture(GL_TEXTURE_2D, textureId)
    gl.glUniform1i(variable, variableValue)
  }

  private def setGlVertexInfo(vertexArray: ByteBuffer, uvArray: ByteBuffer, shader: AvatarShader): Unit = {
    // 頂点配列の設定
    gl.glEnableVertexAttribArray(shader.attributePositionLocation)
    gl.glVertexAttribPointer(shader.attributePositionLocation, 2, GL_FLOAT, normalized = false, 4 * 2, vertexArray)
    // テクスチャ頂点の設定
    gl.glEnableVertexAttribArray(shader.attributeTexCoordLocation)
    gl.glVertexAttribPointer(shader.attributeTexCoordLocation, 2, GL_FLOAT, normalized = false, 4 * 2, uvArray)
  }

  private def setGlBlend(blending: Blending): Unit = {
    gl.glBlendFuncSeparate(blending.srcColor, blending.dstColor, blending.srcAlpha, blending.dstAlpha)
  }

}
