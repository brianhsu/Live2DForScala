package moe.brianhsu.porting.live2d.demo

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPoseReader, FaceDirectionByMouse}
import moe.brianhsu.live2d.adapter.gateway.core.JnaCubismCore
import moe.brianhsu.live2d.adapter.gateway.reader.AvatarFileReader
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{FaceDirection, Pose}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.adapter.{DrawCanvasInfo, OpenGL}
import moe.brianhsu.porting.live2d.demo.sprite._
import moe.brianhsu.porting.live2d.framework.math.ProjectionMatrixCalculator.{Horizontal, Vertical, ViewOrientation}
import moe.brianhsu.porting.live2d.framework.math.{ProjectionMatrixCalculator, ViewPortMatrixCalculator}
import moe.brianhsu.porting.live2d.framework.model.{Avatar, DefaultStrategy}
import moe.brianhsu.porting.live2d.renderer.opengl.{Renderer, TextureManager}

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
  private lazy val projectionMatrixCalculator = new ProjectionMatrixCalculator

  private val frameTimeCalculator = new FrameTimeCalculator
  private implicit val cubismCore: JnaCubismCore = new JnaCubismCore()

  private val avatarHolder: Try[Avatar] = new AvatarFileReader("/home/brianhsu/WorkRoom/CubismSdkForNative-4-r.4/Samples/Resources/Mark").loadAvatar()
  private val modelHolder: Try[Live2DModel] = avatarHolder.map(_.model)
  private val rendererHolder: Try[Renderer] = modelHolder.map(model => new Renderer(model))
  private val updateStrategyHolder: Try[DefaultStrategy] = avatarHolder.map(a => {
    a.updateStrategyHolder = Some(new DefaultStrategy(a.avatarSettings, a.model))
    a.updateStrategyHolder.get.asInstanceOf[DefaultStrategy]
  })
  private val backgroundSprite: LAppSprite = new BackgroundSprite(drawCanvasInfo, backgroundTexture, spriteShader)
  private val powerSprite: LAppSprite = new PowerSprite(drawCanvasInfo, powerTexture, spriteShader)
  private val gearSprite: LAppSprite = new GearSprite(drawCanvasInfo, gearTexture, spriteShader)

  private val targetPointCalculator = new FaceDirectionByMouse(30)

  private val faceDirection = new FaceDirection(targetPointCalculator)

  {
    setupAvatarEffects()
    initOpenGL()
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
      val projection = projectionMatrixCalculator.calculateProjection(
        model.canvasInfo,
        drawCanvasInfo.currentCanvasWidth, drawCanvasInfo.currentCanvasHeight,
        viewPortMatrixCalculator.getViewMatrix,
        updateModelMatrix(model)
      )

      avatar.update(this.frameTimeCalculator)
      renderer.draw(avatar, projection)
    }

    def updateModelMatrix(model: Live2DModel)(viewOrientation: ViewOrientation): Unit = {
      val updatedMatrix = viewOrientation match {
        case Horizontal => model.modelMatrix.scaleToHeight(2.0f)
        case Vertical => model.modelMatrix.scaleToWidth(2.0f)
      }
      model.modelMatrix = updatedMatrix
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

  def onMouseReleased(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.getDeviceToScreen.transformedX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.getDeviceToScreen.transformedY(y.toFloat)
    val viewX = viewPortMatrixCalculator.getViewMatrix.invertedTransformedX(transformedX)
    val viewY = viewPortMatrixCalculator.getViewMatrix.invertedTransformedY(transformedY)

    targetPointCalculator.setFaceTargetCoordinate(0.0f, 0.0f)
    for {
      _ <- avatarHolder
      model <- modelHolder
    } {
      val isHead = model.isHit("HitArea", viewX, viewY)
      val isBody = model.isHit("HitArea2", viewX, viewY)

      println(s"isHead = $isHead, isBody = $isBody")
    }
  }

  def onMouseDragged(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.getDeviceToScreen.transformedX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.getDeviceToScreen.transformedY(y.toFloat)
    val viewX = viewPortMatrixCalculator.getViewMatrix.invertedTransformedX(transformedX)
    val viewY = viewPortMatrixCalculator.getViewMatrix.invertedTransformedY(transformedY)
    targetPointCalculator.setFaceTargetCoordinate(viewX, viewY)
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
    for {
      avatar <- avatarHolder
      updateStrategy <- updateStrategyHolder
    } {
      val pose = new AvatarPoseReader(avatar.avatarSettings).loadPose.getOrElse(new Pose)
      updateStrategy.setFunctionalEffects(
        // new Breath() ::
        //new EyeBlink(avatar.avatarSettings) ::
        faceDirection :: pose ::
        Nil
      )
    }
  }

  private def startMotion(group: String, i: Int): Unit = {
    updateStrategyHolder.foreach { updateStrategy =>
      updateStrategy.startMotion(group, i)
    }

  }
  private def startExpression(name: String): Unit = {
    updateStrategyHolder.foreach { updateStrategy =>
      updateStrategy.setExpression(name)
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
      case 'q' => startMotion("idle", 0)
      case 'w' => startMotion("idle", 1)
      case 'a' => startMotion("tapBody", 0)
      case 's' => startMotion("tapBody", 1)
      case 'd' => startMotion("tapBody", 2)
      case 'f' => startMotion("tapBody", 3)
      case _   => println("Unknow key")
    }
  }


}
