package moe.brianhsu.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureInfo
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite

class BackgroundSprite(drawCanvasInfo: DrawCanvasInfoReader,
                       textureInfo: TextureInfo) extends Sprite(drawCanvasInfo, textureInfo) {

  override protected def calculatePositionAndSize(): Rectangle = {

    val windowWidth = drawCanvasInfo.currentCanvasWidth
    val windowHeight = drawCanvasInfo.currentCanvasHeight
    Rectangle(0, 0, windowWidth.toFloat, windowHeight.toFloat)
  }

}
