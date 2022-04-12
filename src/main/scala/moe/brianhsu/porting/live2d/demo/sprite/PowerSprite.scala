package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.texture.TextureInfo

class PowerSprite(drawCanvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo, shader: SpriteShader)
                 (implicit private val gl: OpenGLBinding) extends LAppSprite(drawCanvasInfo, textureInfo, shader) {

  override protected def calculatePosition(): Position = {
    val windowWidth = drawCanvasInfo.currentCanvasWidth
    Position(
      windowWidth - textureInfo.width.toFloat,
      textureInfo.height * 0.25f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )
  }
}
