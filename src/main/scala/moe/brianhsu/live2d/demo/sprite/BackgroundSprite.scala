package moe.brianhsu.live2d.demo.sprite

import com.jogamp.opengl.GLAutoDrawable
import moe.brianhsu.live2d.renderer.opengl.TextureManager.TextureInfo

class BackgroundSprite(drawable: GLAutoDrawable, textureInfo: TextureInfo,
                       shader: SpriteShader) extends LAppSprite(drawable, textureInfo, shader) {

  override protected def calculatePosition(): Position = {

    val windowWidth = drawable.getSurfaceWidth
    val windowHeight = drawable.getSurfaceHeight
    Position(
      windowWidth / 2.0f, windowHeight / 2.0f,
      textureInfo.width * 2.0f, windowHeight * 0.95f
    )
  }

}
