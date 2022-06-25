package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureInfo
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SpriteShader
import moe.brianhsu.live2d.usecase.renderer.opengl.sprite.Sprite

class BackgroundSprite(drawCanvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo,
                       shader: SpriteShader) extends Sprite(drawCanvasInfo, textureInfo, shader) {

  override protected def calculatePositionAndSize(): Rectangle = {

    val windowWidth = drawCanvasInfo.currentCanvasWidth
    val windowHeight = drawCanvasInfo.currentCanvasHeight
    Rectangle(0, 0, windowWidth.toFloat, windowHeight.toFloat)
  }

}
