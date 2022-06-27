package moe.brianhsu.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureInfo
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite

class PowerSprite(drawCanvasInfo: DrawCanvasInfoReader,
                  textureInfo: TextureInfo) extends Sprite(drawCanvasInfo, textureInfo) {

  override protected def calculatePositionAndSize(): Rectangle = {
    val windowWidth = drawCanvasInfo.currentCanvasWidth
    Rectangle(
      windowWidth - textureInfo.width.toFloat,
      textureInfo.height * 0.25f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )
  }
}
