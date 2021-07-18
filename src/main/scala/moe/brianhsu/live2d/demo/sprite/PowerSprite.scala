package moe.brianhsu.live2d.demo.sprite

import com.jogamp.opengl.GLAutoDrawable
import moe.brianhsu.live2d.demo.DrawCanvasInfo
import moe.brianhsu.live2d.renderer.opengl.TextureManager.TextureInfo

class PowerSprite(canvasInfo: DrawCanvasInfo, drawable: GLAutoDrawable, textureInfo: TextureInfo,
                  shader: SpriteShader) extends LAppSprite(drawable, textureInfo, shader) {
  override protected def calculatePosition(): Position = {
    val windowWidth = canvasInfo.currentSurfaceWidth
    Position(
      windowWidth - textureInfo.width * 0.5f,
      textureInfo.height * 0.5f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )
  }
}
