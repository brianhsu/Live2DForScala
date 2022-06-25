package moe.brianhsu.live2d.usecase.renderer.opengl.shader

import moe.brianhsu.live2d.enitiy.model.drawable.ConstantFlags.BlendMode
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureColor
import moe.brianhsu.live2d.enitiy.opengl.{BlendFunction, OpenGLBinding, RichOpenGLBinding}
import moe.brianhsu.live2d.usecase.renderer.opengl.OffscreenFrame
import moe.brianhsu.live2d.usecase.renderer.opengl.clipping.ClippingContext
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.ShaderFactory.DefaultShaderFactory
import moe.brianhsu.live2d.usecase.renderer.viewport.matrix.ProjectionMatrix

import java.nio.ByteBuffer

object ShaderRenderer {
  private var shaderRendererHolder: Map[OpenGLBinding, ShaderRenderer] = Map.empty

  def getInstance(implicit gl: OpenGLBinding): ShaderRenderer = {

    val shaderFactory = new DefaultShaderFactory

    shaderRendererHolder.get(gl) match {
      case Some(renderer) => renderer
      case None =>
        this.shaderRendererHolder += (gl -> new ShaderRenderer(shaderFactory)(gl, {x: OpenGLBinding => new RichOpenGLBinding(x)}))
        this.shaderRendererHolder(gl)
    }
  }
}

class ShaderRenderer (shaderFactory: ShaderFactory)(implicit gl: OpenGLBinding, richOpenGLWrapper: OpenGLBinding => RichOpenGLBinding) {

  import gl.constants._

  private val setupMaskShader = shaderFactory.setupMaskShader
  private val normalShader = shaderFactory.normalShader
  private val maskedShader = shaderFactory.maskedShader
  private val invertedMaskedShader = shaderFactory.invertedMaskedShader

  def renderDrawable(clippingContextBufferForDraw: Option[ClippingContext], offscreenFrameHolder: Option[OffscreenFrame], textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer, colorBlendMode: BlendMode, baseColor: TextureColor, projection: ProjectionMatrix, invertedMask: Boolean): Unit = {
    val isMasked = clippingContextBufferForDraw.isDefined
    val currentShader = isMasked match {
      case true if invertedMask => invertedMaskedShader
      case true => maskedShader
      case false => normalShader
    }

    gl.glUseProgram(currentShader.programId)
    gl.updateVertexInfo(vertexArray, uvArray, currentShader.attributePositionLocation, currentShader.attributeTexCoordLocation)

    for {
      context <- clippingContextBufferForDraw
      offscreenFrame <- offscreenFrameHolder
      frameBufferId = offscreenFrame.frameBufferId
    } {
      gl.activeAndUpdateTextureVariable(GL_TEXTURE1, frameBufferId, currentShader.samplerTexture1Location, 1)
      gl.glUniformMatrix4fv(currentShader.uniformClipMatrixLocation, 1, transpose = false, context.matrixForDraw.elements)
      gl.setColorChannel(context.layout.channelColor, currentShader.uniformChannelFlagLocation)
    }

    gl.activeAndUpdateTextureVariable(GL_TEXTURE0, textureId, currentShader.samplerTexture0Location, 0)

    // Coordinate transform
    gl.glUniformMatrix4fv(currentShader.uniformMatrixLocation, 1, transpose = false, projection.elements)
    gl.glUniform4f(currentShader.uniformBaseColorLocation, baseColor.red, baseColor.green, baseColor.blue, baseColor.alpha)
    gl.blendFunction = BlendFunction(colorBlendMode)
  }

  def renderMask(context: ClippingContext, textureId: Int, vertexArray: ByteBuffer, uvArray: ByteBuffer): Unit = {

    gl.glUseProgram(setupMaskShader.programId)

    gl.activeAndUpdateTextureVariable(GL_TEXTURE0, textureId, setupMaskShader.samplerTexture0Location, 0)
    gl.updateVertexInfo(vertexArray, uvArray, setupMaskShader.attributePositionLocation, setupMaskShader.attributeTexCoordLocation)
    gl.setColorChannel(context.layout.channelColor, setupMaskShader.uniformChannelFlagLocation)
    gl.glUniformMatrix4fv(setupMaskShader.uniformClipMatrixLocation, 1, transpose = false, context.matrixForMask.elements)

    gl.glUniform4f(
      setupMaskShader.uniformBaseColorLocation,
      context.layout.bounds.leftX * 2.0f - 1.0f,
      context.layout.bounds.bottomY * 2.0f - 1.0f,
      context.layout.bounds.rightX * 2.0f - 1.0f,
      context.layout.bounds.topY * 2.0f - 1.0f
    )

    gl.blendFunction = BlendFunction(GL_ZERO, GL_ONE_MINUS_SRC_COLOR, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA)
  }

}
