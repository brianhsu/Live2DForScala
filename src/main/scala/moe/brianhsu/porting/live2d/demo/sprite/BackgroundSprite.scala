package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureInfo

class BackgroundSprite(drawCanvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo, shader: SpriteShader)
                      (implicit private val gl: OpenGLBinding) extends LAppSprite(drawCanvasInfo, textureInfo, shader) {

  override protected def calculatePosition(): Position = {

    val windowWidth = drawCanvasInfo.currentCanvasWidth
    val windowHeight = drawCanvasInfo.currentCanvasHeight
    Position(0, 0, windowWidth.toFloat, windowHeight.toFloat)
  }

}
