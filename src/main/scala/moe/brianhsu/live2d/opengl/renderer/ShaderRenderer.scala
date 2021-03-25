package moe.brianhsu.live2d.opengl.renderer

import com.jogamp.opengl.{GL, GL2}
import moe.brianhsu.live2d.framework.model.drawable.ConstantFlags.{AdditiveBlend, BlendMode, MultiplicativeBlend, Normal}
import moe.brianhsu.live2d.math.Matrix4x4
import moe.brianhsu.live2d.opengl.TextureColor
import moe.brianhsu.live2d.opengl.renderer.clipping.ClippingContext
import moe.brianhsu.live2d.opengl.shader._

import java.nio.{Buffer, FloatBuffer}

class ShaderRenderer(implicit gl: GL2) {

  private val setupMaskShader = new SetupMask
  private val normalShader = new Normal
  private val maskedShader = new Masked
  private val invertedMaskedShader = new InvertedMasked

  case class Blending(srcColor: Int, dstColor: Int, srcAlpha: Int, dstAlpha: Int)

  def render(renderer: Renderer, textureId: Int,
             vertexArray: Buffer, uvArray: Buffer, colorBlendMode: BlendMode,
             baseColor: TextureColor, projection: Matrix4x4,
             invertedMask: Boolean): Unit = {

    renderer.getClippingContextBufferForMask match {
      case Some(context) => renderMask(context, textureId, vertexArray, uvArray)
      case None => renderDrawable(renderer, textureId, vertexArray, uvArray, colorBlendMode, baseColor, projection, invertedMask)
    }
  }

  private def renderDrawable(renderer: Renderer, textureId: Int, vertexArray: Buffer, uvArray: Buffer, colorBlendMode: BlendMode, baseColor: TextureColor, projection: Matrix4x4, invertedMask: Boolean): Unit = {
    val drawClippingContextHolder = renderer.getClippingContextBufferForDraw
    val masked = drawClippingContextHolder.isDefined // この描画オブジェクトはマスク対象か
    val shader = masked match {
      case true if invertedMask => invertedMaskedShader
      case true => maskedShader
      case false => normalShader
    }

    val blending = colorBlendMode match {
      case Normal => Blending(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA, GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA)
      case AdditiveBlend => Blending(GL.GL_ONE, GL.GL_ONE, GL.GL_ZERO, GL.GL_ONE)
      case MultiplicativeBlend => Blending(GL.GL_DST_COLOR, GL.GL_ONE_MINUS_SRC_ALPHA, GL.GL_ZERO, GL.GL_ONE)
    }

    gl.glUseProgram(shader.shaderProgram)

    setGlVertexInfo(vertexArray, uvArray, shader)

    for (context <- drawClippingContextHolder) {
      val textureIdHolder = renderer.offscreenBufferHolder.map(_.getColorBuffer)
      textureIdHolder.foreach { textureId =>
        setGlTexture(GL.GL_TEXTURE1, textureId, shader.samplerTexture1Location, 1)
        gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, false, FloatBuffer.wrap(context.getMatrixForDraw.getArray()))
        setGlColorChannel(context, shader)
      }
    }

    //テクスチャ設定
    setGlTexture(GL.GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)

    //座標変換
    gl.glUniformMatrix4fv(shader.uniformMatrixLocation, 1, false, FloatBuffer.wrap(projection.getArray()))
    gl.glUniform4f(shader.uniformBaseColorLocation, baseColor.r, baseColor.g, baseColor.b, baseColor.a)
    setGlBlend(blending)
  }

  private def renderMask(context: ClippingContext, textureId: Int, vertexArray: Buffer, uvArray: Buffer): Unit = {
    val shader = setupMaskShader

    gl.glUseProgram(shader.shaderProgram)

    setGlTexture(GL.GL_TEXTURE0, textureId, shader.samplerTexture0Location, 0)
    setGlVertexInfo(vertexArray, uvArray, shader)
    setGlColorChannel(context, shader)

    gl.glUniformMatrix4fv(shader.uniformClipMatrixLocation, 1, false, FloatBuffer.wrap(context.getMatrixForMask.getArray()))

    val rect = context.getLayoutBounds

    gl.glUniform4f(
      shader.uniformBaseColorLocation,
      rect.leftX * 2.0f - 1.0f,
      rect.topY * 2.0f - 1.0f,
      rect.rightX * 2.0f - 1.0f,
      rect.bottomY * 2.0f - 1.0f
    )

    setGlBlend(Blending(GL.GL_ZERO, GL.GL_ONE_MINUS_SRC_COLOR, GL.GL_ZERO, GL.GL_ONE_MINUS_SRC_ALPHA))
  }

  def setGlColorChannel(context: ClippingContext, shader: AvatarShader): Unit = {
    val colorChannel = context.getChannelColor
    gl.glUniform4f(shader.uniformChannelFlagLocation, colorChannel.r, colorChannel.g, colorChannel.b, colorChannel.a)

  }

  def setGlTexture(textureUnit: Int, textureId: Int, variable: Int, variableValue: Int): Unit = {
    gl.glActiveTexture(textureUnit)
    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId)
    gl.glUniform1i(variable, variableValue)
  }

  private def setGlVertexInfo(vertexArray: Buffer, uvArray: Buffer, shader: AvatarShader): Unit = {
    // 頂点配列の設定
    gl.glEnableVertexAttribArray(shader.attributePositionLocation)
    gl.glVertexAttribPointer(shader.attributePositionLocation, 2, GL.GL_FLOAT, false, 4 * 2, vertexArray)
    // テクスチャ頂点の設定
    gl.glEnableVertexAttribArray(shader.attributeTexCoordLocation)
    gl.glVertexAttribPointer(shader.attributeTexCoordLocation, 2, GL.GL_FLOAT, false, 4 * 2, uvArray)
  }

  private def setGlBlend(blending: Blending): Unit = {
    gl.glBlendFuncSeparate(blending.srcColor, blending.dstColor, blending.srcAlpha, blending.dstAlpha)
  }

}
