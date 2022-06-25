package moe.brianhsu.live2d.enitiy.opengl.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.{TextureColor, TextureInfo}
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite.DefaultTextureColor

object Sprite {
  val DefaultTextureColor: TextureColor = TextureColor(1.0f, 1.0f, 1.0f, 1.0f)
}

abstract class Sprite(val drawCanvasInfoReader: DrawCanvasInfoReader,
                      val textureInfo: TextureInfo) {

  def spriteColor: TextureColor = DefaultTextureColor
  def positionAndSize: Rectangle = mPositionAndSize

  private var mPositionAndSize = calculatePositionAndSize()

  protected def calculatePositionAndSize(): Rectangle

  def resize(): Unit = {
    this.mPositionAndSize = calculatePositionAndSize()
  }

  def isHit(pointX: Float, pointY: Float): Boolean = {
    val invertedY = drawCanvasInfoReader.currentCanvasHeight - pointY

    pointX >= positionAndSize.leftX &&
      pointX <= positionAndSize.rightX &&
      invertedY >= positionAndSize.bottomY &&
      invertedY <= positionAndSize.topY
  }
}
