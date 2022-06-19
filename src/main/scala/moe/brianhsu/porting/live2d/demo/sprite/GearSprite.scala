package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureInfo

class GearSprite(canvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo, shader: SpriteShader)
                (implicit private val gl: OpenGLBinding) extends LAppSprite(canvasInfo, textureInfo, shader) {
  override protected def calculatePosition(): Position = {
    val windowWidth = canvasInfo.currentCanvasWidth
    val windowHeight = canvasInfo.currentCanvasHeight

    Position(
      windowWidth - textureInfo.width.toFloat,
      windowHeight - textureInfo.height.toFloat,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )

  }
}
