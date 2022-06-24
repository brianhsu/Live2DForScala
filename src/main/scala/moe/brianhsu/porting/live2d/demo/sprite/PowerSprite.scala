package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureInfo

class PowerSprite(drawCanvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo,
                  shader: SpriteShader) extends Sprite(drawCanvasInfo, textureInfo, shader) {

  override protected def calculatePosition(): Rectangle = {
    val windowWidth = drawCanvasInfo.currentCanvasWidth
    Rectangle(
      windowWidth - textureInfo.width.toFloat,
      textureInfo.height * 0.25f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )
  }
}
