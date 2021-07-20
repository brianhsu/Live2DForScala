package moe.brianhsu.live2d.demo

import moe.brianhsu.live2d.adapter.{DrawCanvasInfo, OpenGL}
import moe.brianhsu.live2d.demo.sprite.{BackgroundSprite, GearSprite, PowerSprite}
import moe.brianhsu.live2d.demo.sprite.{LAppSprite, SpriteShader}
import moe.brianhsu.live2d.framework.{Cubism, CubismExpressionMotion, CubismMotion}
import moe.brianhsu.live2d.framework.effect.impl.{Breath, EyeBlink, FaceDirection}
import moe.brianhsu.live2d.framework.math.ViewPortMatrixCalculator
import moe.brianhsu.live2d.framework.model.{Avatar, Live2DModel}
import moe.brianhsu.live2d.renderer.opengl.{Renderer, TextureManager}

import java.awt.event.KeyEvent
import scala.util.Try



class LAppView(drawCanvasInfo: DrawCanvasInfo)(private implicit val openGL: OpenGL) {


  import openGL._

  private val spriteShader: SpriteShader = new SpriteShader().useProgram()
  private val manager = new TextureManager()

  private lazy val backgroundTexture = manager.loadTexture("src/main/resources/texture/back_class_normal.png")
  private lazy val powerTexture = manager.loadTexture("src/main/resources/texture/close.png")
  private lazy val gearTexture = manager.loadTexture("src/main/resources/texture/icon_gear.png")
  private lazy val viewPortMatrixCalculator = new ViewPortMatrixCalculator

  private val frameTimeCalculator = new FrameTimeCalculator
  private val avatarHolder: Try[Avatar] = Cubism.loadAvatar("src/main/resources/Haru")
  private val modelHolder: Try[Live2DModel] = avatarHolder.flatMap(_.modelHolder)
  private val backgroundSprite: LAppSprite = new BackgroundSprite(drawCanvasInfo, backgroundTexture, spriteShader)
  private val powerSprite: LAppSprite = new PowerSprite(drawCanvasInfo, powerTexture, spriteShader)
  private val gearSprite: LAppSprite = new GearSprite(drawCanvasInfo, gearTexture, spriteShader)
  private val rendererHolder: Try[Renderer] = modelHolder.map(model => new Renderer(model))


  private val faceDirection = new FaceDirection(30)

  {
    setupAvatarEffects()
    initOpenGL()
    for {
      avatar <- avatarHolder
      settings = avatar.getAvatarSettings
      (key, motions) <- settings.motions
      motion <- motions
    } {
      val m = CubismMotion(motion, e => println(e), settings.eyeBlinkParameterIds, Nil)
      println(m)
    }
  }

  def resetModel(): Unit = {
    modelHolder.foreach(_.reset())
  }

  def display(): Unit = {
    clearScreen()

    this.backgroundSprite.render()
    this.powerSprite.render()
    this.gearSprite.render()
    this.frameTimeCalculator.updateFrameTime()

    for {
      avatar <- avatarHolder
      model <- modelHolder
      renderer <- rendererHolder
    } {
      // TODO:
      // There should be a better way to get width / height
      val projection = viewPortMatrixCalculator.getProjection(
        drawCanvasInfo.currentCanvasWidth,
        drawCanvasInfo.currentCanvasHeight,
        model.canvasInfo.width,
        model.modelMatrix
      )

      avatar.update(this.frameTimeCalculator.getDeltaTimeInSeconds)
      renderer.draw(avatar, projection)
    }

  }

  def resize(): Unit = {

    // TODO:
    // 1. There should be better way to do this.
    viewPortMatrixCalculator.updateViewPort(
      drawCanvasInfo.currentCanvasWidth,
      drawCanvasInfo.currentCanvasHeight
    )

    backgroundSprite.resize()
    powerSprite.resize()
    gearSprite.resize()
    this.display()
  }

  def onMouseReleased(): Unit = {
    faceDirection.setFaceTargetCoordinate(0.0f, 0.0f)
  }

  def onMouseDragged(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.getDeviceToScreen.transformX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.getDeviceToScreen.transformY(y.toFloat)
    val viewX = viewPortMatrixCalculator.getViewMatrix.invertTransformX(transformedX)
    val viewY = viewPortMatrixCalculator.getViewMatrix.invertTransformY(transformedY)
    faceDirection.setFaceTargetCoordinate(viewX, viewY)
  }

  private def initOpenGL(): Unit = {

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
      drawCanvasInfo.currentCanvasWidth,
      drawCanvasInfo.currentCanvasHeight
    )

    openGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    openGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    openGL.glEnable(GL_BLEND)
    openGL.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
  }

  private def setupAvatarEffects(): Unit = {
    avatarHolder.foreach { avatar =>
      avatar.setEffects(
        new Breath() ::
        new EyeBlink(avatar.getAvatarSettings) ::
        faceDirection ::
        Nil
      )
    }
  }

  private def startExpression(name: String): Unit = {
    avatarHolder.foreach { avatar =>
      avatar.setExpression(name)
    }
  }

  private def clearScreen(): Unit = {
    openGL.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    openGL.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    openGL.glClearDepth(1.0)
  }
  def keyReleased(keyEvent: KeyEvent): Unit = {
    keyEvent.getKeyChar match {
      case '0' => startExpression("f00")
      case '1' => startExpression("f01")
      case '2' => startExpression("f02")
      case '3' => startExpression("f03")
      case '4' => startExpression("f04")
      case '5' => startExpression("f05")
      case '6' => startExpression("f06")
      case '7' => startExpression("f07")
      case _   => println("Unknown expression")
    }
  }


}
