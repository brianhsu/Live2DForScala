package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.adapter.gateway.renderer.OpenGLBinding
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfo
import moe.brianhsu.porting.live2d.renderer.opengl.TextureManager.TextureInfo

class BackgroundSprite(drawCanvasInfo: DrawCanvasInfo, textureInfo: TextureInfo, shader: SpriteShader)
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
