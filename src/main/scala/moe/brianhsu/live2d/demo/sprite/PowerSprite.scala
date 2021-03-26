package moe.brianhsu.live2d.demo.sprite

import com.jogamp.opengl.GLAutoDrawable
import moe.brianhsu.live2d.renderer.opengl.TextureManager.TextureInfo

class PowerSprite(drawable: GLAutoDrawable, textureInfo: TextureInfo,
                  shader: SpriteShader) extends LAppSprite(drawable, textureInfo, shader) {
  override protected def calculatePosition(): Position = {
    val windowWidth = drawable.getSurfaceWidth
    Position(
      windowWidth - textureInfo.width * 0.5f,
      textureInfo.height * 0.5f,
      textureInfo.width.toFloat,
      textureInfo.height.toFloat
    )
  }
}
