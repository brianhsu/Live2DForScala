package moe.brianhsu.porting.live2d.demo

import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPoseReader, FaceDirectionByMouse}
import moe.brianhsu.live2d.adapter.gateway.core.JnaCubismCore
import moe.brianhsu.live2d.adapter.gateway.reader.AvatarFileReader
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection, Pose}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.porting.live2d.adapter.{DrawCanvasInfo, OpenGL}
import moe.brianhsu.porting.live2d.demo.sprite._
import moe.brianhsu.porting.live2d.framework.math.ProjectionMatrixCalculator.{Horizontal, Vertical, ViewOrientation}
import moe.brianhsu.porting.live2d.framework.math.{ProjectionMatrixCalculator, ViewPortMatrixCalculator}
import moe.brianhsu.porting.live2d.framework.model.{Avatar, DefaultStrategy}
import moe.brianhsu.porting.live2d.renderer.opengl.{Renderer, TextureManager}

import scala.util.Try

class LAppView(drawCanvasInfo: DrawCanvasInfo)(private implicit val openGL: OpenGL) {

  import openGL._

  private var zoom: Float = 2.0f
  private var offsetX: Float = 0.0f
  private var offsetY: Float = 0.0f
  private val spriteShader: SpriteShader = new SpriteShader().useProgram()
  private val manager = TextureManager.getInstance

  private lazy val backgroundTexture = manager.loadTexture("src/main/resources/texture/back_class_normal.png")
  private lazy val powerTexture = manager.loadTexture("src/main/resources/texture/close.png")
  private lazy val gearTexture = manager.loadTexture("src/main/resources/texture/icon_gear.png")
  private lazy val viewPortMatrixCalculator = new ViewPortMatrixCalculator
  private lazy val projectionMatrixCalculator = new ProjectionMatrixCalculator

  private val frameTimeCalculator = new FrameTimeCalculator
  private implicit val cubismCore: JnaCubismCore = new JnaCubismCore()

  private var avatarHolder: Try[Avatar] = new AvatarFileReader("src/main/resources/Haru").loadAvatar()
  private var modelHolder: Try[Live2DModel] = avatarHolder.map(_.model)
  private var rendererHolder: Try[Renderer] = modelHolder.map(model => new Renderer(model))
  private var updateStrategyHolder: Try[DefaultStrategy] = avatarHolder.map(a => {
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

  def display(isForceUpdate: Boolean = false): Unit = {
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
        updateModelMatrix(model),
        isForceUpdate
      )

      avatar.update(this.frameTimeCalculator)
      renderer.draw(avatar, projection)
    }

    def updateModelMatrix(model: Live2DModel)(viewOrientation: ViewOrientation): Unit = {
      val updatedMatrix = viewOrientation match {
        case Horizontal => model.modelMatrix.scaleToHeight(zoom).left(offsetX).top(offsetY)
        case Vertical => model.modelMatrix.scaleToWidth(zoom).left(offsetX).top(offsetY)
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

    openGL.glViewport(0, 0, drawCanvasInfo.currentSurfaceWidth, drawCanvasInfo.currentSurfaceHeight)
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
      updateStrategy.setEffects(
        //new Breath() ::
        //new EyeBlink(avatar.avatarSettings) ::
        faceDirection ::
        pose ::
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

  private def switchModel(filename: String): Unit = {
    this.avatarHolder = new AvatarFileReader(filename).loadAvatar()
    this.modelHolder = avatarHolder.map(_.model)
    this.updateStrategyHolder = avatarHolder.map(a => {
      println("Create new update strategy")
      a.updateStrategyHolder = Some(new DefaultStrategy(a.avatarSettings, a.model))
      a.updateStrategyHolder.get.asInstanceOf[DefaultStrategy]
    })
    this.rendererHolder = modelHolder.map(model => new Renderer(model))
    setupAvatarEffects()
    initOpenGL()
  }

  def move(offsetX: Float, offsetY: Float): Unit = {
    this.offsetX += offsetX
    this.offsetY += offsetY
    this.display(true)
  }

  def zoom(level: Float): Unit = {
    this.zoom += level
    this.display(true)
  }

  def keyReleased(key: Char): Unit = {
    key match {
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
      case 'z' => {
        println("Z is pressed...")
        DefaultStrategy.enablePhy = true
      }
      case 'x' => switchModel("src/test/resources/models/Mark")
      case 'c' => switchModel("src/test/resources/models/Rice")
      case 'v' => switchModel("src/test/resources/models/Natori")
      case 'b' => switchModel("src/test/resources/models/Hiyori")
      case _   => println("Unknow key")
    }
  }


}
