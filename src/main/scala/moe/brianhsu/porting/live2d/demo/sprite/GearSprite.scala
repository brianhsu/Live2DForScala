package moe.brianhsu.porting.live2d.demo.sprite

import moe.brianhsu.live2d.adapter.gateway.renderer.OpenGLBinding
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfo
import moe.brianhsu.porting.live2d.renderer.opengl.TextureManager.TextureInfo

class GearSprite(canvasInfo: DrawCanvasInfo, textureInfo: TextureInfo, shader: SpriteShader)
                (implicit private val gl: OpenGLBinding) extends LAppSprite(canvasInfo, textureInfo, shader) {
  override protected def calculatePosition(): Position = {
    val windowWidth = canvasInfo.currentSurfaceWidth
    val windowHeight = canvasInfo.currentSurfaceHeight

    Position(
      windowWidth - textureInfo.width * 0.5f,
      windowHeight - textureInfo.height * 0.5f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )

  }
}
