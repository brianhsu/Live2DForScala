package moe.brianhsu.porting.live2d.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.math.matrix.GeneralMatrix
import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}
import moe.brianhsu.porting.live2d.adapter.OpenGL
import moe.brianhsu.porting.live2d.renderer.opengl.{Renderer, TextureColor}
import moe.brianhsu.porting.live2d.renderer.opengl.clipping.ClippingContext

import java.nio.{ByteBuffer, FloatBuffer}

class ShaderRenderer(implicit gl: OpenGL) {

  import gl._

  private val setupMaskShader = new SetupMask
  private val normalShader = new Normal
  private val maskedShader = new Masked
  private val invertedMaskedShader = new InvertedMasked

  case class Blending(srcColor: Int, dstColor: Int, srcAlpha: Int, dstAlpha: Int)

  def render(renderer: Renderer, textureId: Int,
             vertexArray: ByteBuffer, uvArray: ByteBuffer, colorBlendMode: BlendMode,
             baseColor: TextureColor, projection: GeneralMatrix,
             invertedMask: Boolean): Unit = {

    renderer.getClippingContextBufferForMask match {
      case Some(context) => renderMask(context, textureId, vertexArray, uvArray)
      case None => renderDrawable(renderer, textureId, vertexArray, uvArray, colorBlendMode, baseColor, projection, invertedMask)
    }
  }

  private def renderDrawable(renderer: Renderer, textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer, colorBlendMode: BlendMode, baseColor: TextureColor, projection: GeneralMatrix, invertedMask: Boolean): Unit = {
    val drawClippingContextHolder = renderer.getClippingContextBufferForDraw
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

    gl.glUseProgram(shader.shaderProgram)

    setGlVertexInfo(vertexArray, uvArray, shader)

    for (context <- drawClippingContextHolder) {
      val textureIdHolder = renderer.offscreenBufferHolder.map(_.getColorBuffer)
      textureIdHolder.foreach { textureId =>
        setGlTexture(GL_TEXTURE1, textureId, shader.samplerTexture1Location, 1)
        gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, transpose = false, FloatBuffer.wrap(context.getMatrixForDraw.elements))
        setGlColorChannel(context, shader)
      }
    }

    //テクスチャ設定
    setGlTexture(GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)

    //座標変換
    gl.glUniformMatrix4fv(shader.uniformMatrixLocation, 1, transpose = false, FloatBuffer.wrap(projection.elements))
    gl.glUniform4f(shader.uniformBaseColorLocation, baseColor.r, baseColor.g, baseColor.b, baseColor.a)
    setGlBlend(blending)
  }

  private def renderMask(context: ClippingContext, textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer): Unit = {
    val shader = setupMaskShader

    gl.glUseProgram(shader.shaderProgram)

    setGlTexture(GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)
    setGlVertexInfo(vertexArray, uvArray, shader)
    setGlColorChannel(context, shader)

    gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, transpose = false, FloatBuffer.wrap(context.getMatrixForMask.elements))

    val rect = context.getLayoutBounds

    gl.glUniform4f(
      shader.uniformBaseColorLocation,
      rect.leftX * 2.0f - 1.0f,
      rect.topY * 2.0f - 1.0f,
      rect.rightX * 2.0f - 1.0f,
      rect.bottomY * 2.0f - 1.0f
    )

    setGlBlend(Blending(GL_ZERO, GL_ONE_MINUS_SRC_COLOR, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA))
  }

  def setGlColorChannel(context: ClippingContext, shader: AvatarShader): Unit = {
    val colorChannel = context.getChannelColor
    gl.glUniform4f(shader.uniformChannelFlagLocation, colorChannel.r, colorChannel.g, colorChannel.b, colorChannel.a)

  }

  def setGlTexture(textureUnit: Int, textureId: Int, variable: Int, variableValue: Int): Unit = {
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
