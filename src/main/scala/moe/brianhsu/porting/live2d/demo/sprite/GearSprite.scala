package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.porting.live2d.renderer.opengl.TextureManager.TextureInfo

class GearSprite(canvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo, shader: SpriteShader)
                (implicit private val gl: OpenGLBinding) extends LAppSprite(canvasInfo, textureInfo, shader) {
  override protected def calculatePosition(): Position = {
    val windowWidth = canvasInfo.currentCanvasWidth
    val windowHeight = canvasInfo.currentCanvasHeight

    Position(
      windowWidth - textureInfo.width,
      windowHeight - textureInfo.height,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )

  }
}
