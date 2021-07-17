package moe.brianhsu.live2d.demo

import com.jogamp.opengl.GLAutoDrawable
import moe.brianhsu.live2d.adapter.JavaOpenGL
import moe.brianhsu.live2d.demo.sprite.{BackgroundSprite, GearSprite, PowerSprite}
import moe.brianhsu.live2d.demo.sprite.{LAppSprite, SpriteShader}
import moe.brianhsu.live2d.framework.Cubism
import moe.brianhsu.live2d.framework.math.ViewPortMatrixCalculator
import moe.brianhsu.live2d.framework.model.{Avatar, Live2DModel}
import moe.brianhsu.live2d.renderer.opengl.{Renderer, TextureManager}

import scala.util.Try

class LAppView(openGLDrawable: GLAutoDrawable) {


  private implicit val openGL: JavaOpenGL = new JavaOpenGL(openGLDrawable.getGL.getGL2)

  import openGL._

  private val spriteShader: SpriteShader = new SpriteShader().useProgram()
  private val manager = new TextureManager()

  private lazy val backgroundTexture = manager.loadTexture("src/main/resources/texture/back_class_normal.png")
  private lazy val powerTexture = manager.loadTexture("src/main/resources/texture/close.png")
  private lazy val gearTexture = manager.loadTexture("src/main/resources/texture/icon_gear.png")
  private lazy val viewPortMatrixCalculator = new ViewPortMatrixCalculator

  private val avatarHolder: Try[Avatar] = Cubism.loadAvatar("src/main/resources/Haru")
  private val modelHolder: Try[Live2DModel] = avatarHolder.flatMap(_.modelHolder)
  private val backgroundSprite: LAppSprite = new BackgroundSprite(openGLDrawable, backgroundTexture, spriteShader)
  private val powerSprite: LAppSprite = new PowerSprite(openGLDrawable, powerTexture, spriteShader)
  private val gearSprite: LAppSprite = new GearSprite(openGLDrawable, gearTexture, spriteShader)
  private val rendererHolder: Try[Renderer] = modelHolder.map(model => new Renderer(model))


  init()
  def resetMode(): Unit = {
    for {
      avatar <- avatarHolder
      model <- modelHolder
    } {
      model.reset()
    }
  }

  def init(): Unit = {

    // TODO:
    // 1. Check if Linux's openGLDrawable.getSurfaceWidth / getSurfaceHeight also return wrong value
    // 2. There should be a better way to do this.
    /*
    viewPortMatrixCalculator.updateViewPort(
      openGLDrawable.getSurfaceWidth,
      openGLDrawable.getSurfaceHeight
    )
    */
    viewPortMatrixCalculator.updateViewPort(
      Main.frame.getWidth,
      Main.frame.getHeight
    )

    openGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    openGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    openGL.glEnable(GL_BLEND)
    openGL.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
  }

  def display(): Unit = {
    clearScreen()

    this.backgroundSprite.render()
    this.powerSprite.render()
    this.gearSprite.render()
    FrameTime.updateFrameTime()

    for {
      avatar <- avatarHolder
      model <- modelHolder
      renderer <- rendererHolder
    } {
      // TODO:
      // There should be a better way to get width / height
      val projection = viewPortMatrixCalculator.getProjection(
        Main.frame.getWidth,
        Main.frame.getHeight,
        model.canvasInfo.width,
        model.modelMatrix
      )

      avatar.update()
      renderer.draw(avatar, projection)
    }

  }

  private def clearScreen(): Unit = {
    openGL.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    openGL.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    openGL.glClearDepth(1.0)
  }

  def resize(): Unit = {

    // TODO:
    // 1. There should be better way to do this.
    viewPortMatrixCalculator.updateViewPort(
      Main.frame.getWidth,
      Main.frame.getHeight
    )

    backgroundSprite.resize()
    powerSprite.resize()
    gearSprite.resize()
    this.display()
  }

  def onMouseClick(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.getDeviceToScreen.transformX(939.toFloat)
    val transformedY = viewPortMatrixCalculator.getDeviceToScreen.transformY(851.toFloat)
    val viewX = viewPortMatrixCalculator.getViewMatrix.invertTransformX(transformedX)
    val viewY = viewPortMatrixCalculator.getViewMatrix.invertTransformY(transformedY)
    //println(s"==> viewX, viewY = $viewX, $viewY")

  }

  private var lastX = 0
  private var lastY = 0
  def onMouseDragged(x: Int, y: Int): Unit = {
    if (x == 0 && y == 0) {
      FaceDirection.set(0.0f, 0.0f)
    } else {
      val transformedX = viewPortMatrixCalculator.getDeviceToScreen.transformX(lastX.toFloat)
      val transformedY = viewPortMatrixCalculator.getDeviceToScreen.transformY(lastY.toFloat)
      val viewX = viewPortMatrixCalculator.getViewMatrix.invertTransformX(transformedX)
      val viewY = viewPortMatrixCalculator.getViewMatrix.invertTransformY(transformedY)
      FaceDirection.set(viewX, viewY)
    }
    lastX = x
    lastY = y
  }

}
