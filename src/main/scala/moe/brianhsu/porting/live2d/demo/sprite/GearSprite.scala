package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.porting.live2d.renderer.opengl.TextureManager.TextureInfo
import moe.brianhsu.porting.live2d.adapter.{DrawCanvasInfo, OpenGL}

class GearSprite(canvasInfo: DrawCanvasInfo, textureInfo: TextureInfo, shader: SpriteShader)
                (implicit private val gl: OpenGL) extends LAppSprite(canvasInfo, textureInfo, shader) {
  override protected def calculatePosition(): Position = {
    val windowWidth = canvasInfo.currentSurfaceWidth
    val windowHeight = canvasInfo.currentSurfaceHeight

    Position(
      windowWidth - textureInfo.width * 0.5f,
      windowHeight - textureInfo.height * 0.5f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )

  }
}
