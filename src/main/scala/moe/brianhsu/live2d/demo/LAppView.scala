package moe.brianhsu.live2d.demo

import com.jogamp.opengl.{GL2, GLAutoDrawable}
import com.jogamp.opengl.GL._
import moe.brianhsu.live2d.demo.LAppSprite.{BackgroundSprite, GearSprite, PowerSprite}
import moe.brianhsu.live2d.renderer.opengl.{Renderer, TextureManager}

class LAppView(openGLDrawable: GLAutoDrawable) {
  private implicit val gl: GL2 = openGLDrawable.getGL.getGL2
  private val spriteShader: SpriteShader = new SpriteShader().useProgram()
  private val manager = new TextureManager()
  private lazy val backgroundTexture = manager.loadTexture("src/main/resources/texture/back_class_normal.png")
  private lazy val powerTexture = manager.loadTexture("src/main/resources/texture/close.png")
  private lazy val gearTexture = manager.loadTexture("src/main/resources/texture/icon_gear.png")

  private val model = LAppLive2DManager.avatarHolder.get.modelHolder.get
  private val backgroundSprite: LAppSprite = new BackgroundSprite(openGLDrawable, backgroundTexture, spriteShader)
  private val powerSprite: LAppSprite = new PowerSprite(openGLDrawable, powerTexture, spriteShader)
  private val gearSprite: LAppSprite = new GearSprite(openGLDrawable, gearTexture, spriteShader)
  private val renderer = new Renderer(model)(gl)
  private val viewPortMatrixCalculator = new ViewPortMatrixCalculator

  init()

  def init(): Unit = {
    viewPortMatrixCalculator.updateViewPort(
      openGLDrawable.getSurfaceWidth,
      openGLDrawable.getSurfaceHeight
    )

    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    gl.glEnable(GL_BLEND)
    gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
  }

  def display(): Unit = {
    clearScreen()

    this.backgroundSprite.render()
    this.powerSprite.render()
    this.gearSprite.render()

    LAppLive2DManager.avatarHolder.foreach { avatar =>
      val projection = viewPortMatrixCalculator.getProjection(
        openGLDrawable.getSurfaceWidth,
        openGLDrawable.getSurfaceHeight,
        avatar.modelHolder.map(_.canvasInfo.width).get,
        avatar.modelHolder.map(_.modelMatrix).get
      )

      avatar.update()
      renderer.draw(avatar, projection)
    }

    FrameTime.updateFrameTime(System.currentTimeMillis())
  }

  private def clearScreen(): Unit = {
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    gl.glClearDepth(1.0)
  }

  def resize() = {
    viewPortMatrixCalculator.updateViewPort(
      openGLDrawable.getSurfaceWidth,
      openGLDrawable.getSurfaceHeight
    )

    backgroundSprite.resize()
    powerSprite.resize()
    gearSprite.resize()
  }

  def onMouseClick(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.getDeviceToScreen.transformX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.getDeviceToScreen.transformY(y.toFloat)

    println("===> transformedX:" + transformedX)
    println("===> transformedY:" + transformedY)
  }
}
