package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.porting.live2d.renderer.opengl.TextureManager.TextureInfo

class BackgroundSprite(drawCanvasInfo: DrawCanvasInfoReader, textureInfo: TextureInfo, shader: SpriteShader)
                      (implicit private val gl: OpenGLBinding) extends LAppSprite(drawCanvasInfo, textureInfo, shader) {

  override protected def calculatePosition(): Position = {

    val windowWidth = drawCanvasInfo.currentSurfaceWidth
    val windowHeight = drawCanvasInfo.currentSurfaceHeight
    Position(
      windowWidth / 2.0f, windowHeight / 2.0f,
      textureInfo.width * 2.0f, windowHeight * 0.95f
    )
  }

}
