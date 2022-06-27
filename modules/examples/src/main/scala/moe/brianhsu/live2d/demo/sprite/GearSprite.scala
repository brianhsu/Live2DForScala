package moe.brianhsu.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureInfo
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite

class GearSprite(canvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo)
                extends Sprite(canvasInfo, textureInfo) {

  override protected def calculatePositionAndSize(): Rectangle = {
    val windowWidth = canvasInfo.currentCanvasWidth
    val windowHeight = canvasInfo.currentCanvasHeight

    Rectangle(
      windowWidth - textureInfo.width.toFloat,
      windowHeight - textureInfo.height.toFloat,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )

  }
}
