package moe.brianhsu.live2d.usecase.renderer.opengl.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.math.Rectangle
import moe.brianhsu.live2d.enitiy.opengl.texture.{TextureColor, TextureInfo}
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SpriteShader
import moe.brianhsu.live2d.usecase.renderer.opengl.sprite.Sprite.DefaultTextureColor

object Sprite {
  val DefaultTextureColor: TextureColor = TextureColor(1.0f, 1.0f, 1.0f, 1.0f)
}

abstract class Sprite(val drawCanvasInfoReader: DrawCanvasInfoReader,
                      val textureInfo: TextureInfo,
                      val shader: SpriteShader) {

  def spriteColor: TextureColor = DefaultTextureColor
  def rect: Rectangle = mRect

  private var mRect = calculatePosition()

  protected def calculatePosition(): Rectangle

  def resize(): Unit = {
    this.mRect = calculatePosition()
  }

  def isHit(pointX: Float, pointY: Float): Boolean = {
    val invertedY = drawCanvasInfoReader.currentCanvasHeight - pointY

    pointX >= rect.leftX &&
      pointX <= rect.rightX &&
      invertedY >= rect.bottomY &&
      invertedY <= rect.topY
  }
}
