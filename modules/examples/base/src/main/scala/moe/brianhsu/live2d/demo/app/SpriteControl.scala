package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.demo.sprite.{BackgroundSprite, GearSprite, PowerSprite}
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureInfo
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SpriteShader
import moe.brianhsu.live2d.usecase.renderer.opengl.sprite.SpriteRenderer

import java.awt.Color
import scala.util.Try

trait SpriteControl {
  this: OpenGLBase =>

  private val defaultBackgroundTexture = "/texture/back_class_normal.png"
  protected var backgroundColor = new Color(0, 255, 0)

  protected val backgroundTexture: TextureInfo = textureManager.loadTexture(defaultBackgroundTexture)
  protected val backgroundSprite: Sprite = new BackgroundSprite(drawCanvasInfo, backgroundTexture)
  protected val powerTexture: TextureInfo = textureManager.loadTexture("/texture/close.png")
  protected val powerSprite: Sprite = new PowerSprite(drawCanvasInfo, powerTexture)
  protected val gearTexture: TextureInfo = textureManager.loadTexture("/texture/icon_gear.png")
  protected val gearSprite: Sprite = new GearSprite(drawCanvasInfo, gearTexture)
  protected var sprites: List[Sprite] = this.backgroundSprite :: this.gearSprite :: this.powerSprite :: Nil

  protected val spriteRenderer = new SpriteRenderer(new SpriteShader)

  def switchToDefaultBackground(): Unit = {
    this.sprites =
      createBackgroundSprite(defaultBackgroundTexture) ::
        this.sprites.filterNot(_.isInstanceOf[BackgroundSprite])
  }

  def changeBackground(filePath: String): Try[Unit] = Try {
    onOpenGLThread {
      this.sprites =
        createBackgroundSprite(filePath) ::
          this.sprites.filterNot(_.isInstanceOf[BackgroundSprite])
    }
  }

  def switchToPureColorBackground(color: Color): Unit = {
    this.backgroundColor = color
    this.sprites = this.sprites.filterNot(_.isInstanceOf[BackgroundSprite])
    this.display(true)
  }

  protected def createBackgroundSprite(textureFile: String): BackgroundSprite = {
    new BackgroundSprite(
      drawCanvasInfo,
      textureManager.loadTexture(textureFile)
    )
  }

}
