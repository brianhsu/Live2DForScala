package moe.brianhsu.live2d.demo.sprite

import com.jogamp.opengl.GLAutoDrawable
import moe.brianhsu.live2d.demo.DrawCanvasInfo
import moe.brianhsu.live2d.framework.model.CanvasInfo
import moe.brianhsu.live2d.renderer.opengl.TextureManager.TextureInfo

class GearSprite(canvasInfo: DrawCanvasInfo, drawable: GLAutoDrawable, textureInfo: TextureInfo,
                 shader: SpriteShader) extends LAppSprite(drawable, textureInfo, shader) {
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
