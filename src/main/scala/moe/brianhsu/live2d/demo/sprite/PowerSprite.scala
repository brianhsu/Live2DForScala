package moe.brianhsu.live2d.demo.sprite

import moe.brianhsu.live2d.adapter.{DrawCanvasInfo, OpenGL}
import moe.brianhsu.live2d.renderer.opengl.TextureManager.TextureInfo

class PowerSprite(drawCanvasInfo: DrawCanvasInfo, textureInfo: TextureInfo, shader: SpriteShader)
                 (implicit private val gl: OpenGL) extends LAppSprite(drawCanvasInfo, textureInfo, shader) {

  override protected def calculatePosition(): Position = {
    val windowWidth = drawCanvasInfo.currentSurfaceWidth
    Position(
      windowWidth - textureInfo.width * 0.5f,
      textureInfo.height * 0.5f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )
  }
}
