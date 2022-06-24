package moe.brianhsu.porting.live2d.demo

import moe.brianhsu.live2d.adapter.gateway.avatar.AvatarFileReader
import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPhysicsReader, AvatarPoseReader, FaceDirectionByMouse}
import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection}
import moe.brianhsu.live2d.enitiy.avatar.updater.SystemNanoTimeBasedFrameInfo
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.usecase.renderer.opengl.texture.TextureManager
import moe.brianhsu.live2d.usecase.renderer.viewport.{ProjectionMatrixCalculator, ViewOrientation, ViewPortMatrixCalculator}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy
import moe.brianhsu.porting.live2d.demo.sprite._
import moe.brianhsu.porting.live2d.renderer.opengl.Renderer

import scala.annotation.unused
import scala.util.Try

class LAppView(drawCanvasInfo: DrawCanvasInfoReader)(private implicit val openGL: OpenGLBinding) {

  import openGL.constants._

  private var zoom: Float = 2.0f
  private var offsetX: Float = 0.0f
  private var offsetY: Float = 0.0f
  private val spriteShader: SpriteShader = new SpriteShader()//.useProgram()
  println("spriteSharder:" + spriteShader)
  private val manager = TextureManager.getInstance

  private lazy val backgroundTexture = manager.loadTexture("src/main/resources/texture/back_class_normal.png")
  private lazy val powerTexture = manager.loadTexture("src/main/resources/texture/close.png")
  private lazy val gearTexture = manager.loadTexture("src/main/resources/texture/icon_gear.png")
  private lazy val viewPortMatrixCalculator = new ViewPortMatrixCalculator
  private lazy val projectionMatrixCalculator = new ProjectionMatrixCalculator(drawCanvasInfo)

  private val frameTimeCalculator = new SystemNanoTimeBasedFrameInfo
  private implicit val cubismCore: JnaNativeCubismAPILoader = new JnaNativeCubismAPILoader()

  private var avatarHolder: Try[Avatar] = new AvatarFileReader("src/main/resources/Haru").loadAvatar()
  private var modelHolder: Try[Live2DModel] = avatarHolder.map(_.model)
  private var rendererHolder: Try[Renderer] = modelHolder.map(model => new Renderer(model))
  private var updateStrategyHolder: Try[BasicUpdateStrategy] = avatarHolder.map(a => {
    a.updateStrategyHolder = Some(new BasicUpdateStrategy(a.avatarSettings, a.model))
    a.updateStrategyHolder.get.asInstanceOf[BasicUpdateStrategy]
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

      val projection = projectionMatrixCalculator.calculate(
        viewPortMatrixCalculator.viewPortMatrix,
        isForceUpdate,
        updateModelMatrix(model)
      )

      avatar.update(this.frameTimeCalculator)
      renderer.draw(avatar, projection)
    }

    def updateModelMatrix(model: Live2DModel)(@unused viewOrientation: ViewOrientation): Unit = {
      model.modelMatrix = model.modelMatrix
        .scaleToHeight(zoom)
        .left(offsetX)
        .top(offsetY)
    }

  }

  def resize(): Unit = {

    System.err.println(s"cw: ${drawCanvasInfo.currentCanvasWidth}, ch: ${drawCanvasInfo.currentCanvasHeight}, sw: ${drawCanvasInfo.currentSurfaceWidth}, sh: ${drawCanvasInfo.currentSurfaceHeight}")
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
    val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)


    targetPointCalculator.updateFaceTargetCoordinate(0.0f, 0.0f)
    for {
      _ <- avatarHolder
      model <- modelHolder
    } {

      val isHead = model.isHit("HitArea", transformedX, transformedY)
      val isBody = model.isHit("HitArea2", transformedX, transformedY)
      val isPower = powerSprite.isHit(x.toFloat, y.toFloat)
      val isGear = gearSprite.isHit(x.toFloat, y.toFloat)

      println(s"isHead = $isHead, isBody = $isBody, isGear = $isGear, isPower = $isPower")
    }
  }

  def onMouseDragged(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
    val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
    val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
    targetPointCalculator.updateFaceTargetCoordinate(viewX, viewY)
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
      val poseHolder = new AvatarPoseReader(avatar.avatarSettings).loadPose
      val physicsHolder = new AvatarPhysicsReader(avatar.avatarSettings).loadPhysics
      updateStrategy.effects = List(
        Some(new Breath()),
        Some(new EyeBlink(avatar.avatarSettings)),
        Some(faceDirection),
        physicsHolder,
        poseHolder
      ).flatten
    }
  }

  private def startMotion(group: String, i: Int): Unit = {
    updateStrategyHolder.foreach { updateStrategy =>
      updateStrategy.startMotion(group, i)
    }

  }
  private def startExpression(name: String): Unit = {
    updateStrategyHolder.foreach { updateStrategy =>
      updateStrategy.startExpression(name)
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
      a.updateStrategyHolder = Some(new BasicUpdateStrategy(a.avatarSettings, a.model))
      a.updateStrategyHolder.get.asInstanceOf[BasicUpdateStrategy]
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
      case 'z' => switchModel("src/main/resources/Haru")
      case 'x' => switchModel("src/test/resources/models/Mark")
      case 'c' => switchModel("src/test/resources/models/Rice")
      case 'v' => switchModel("src/test/resources/models/Natori")
      case 'b' => switchModel("src/test/resources/models/Hiyori")
      case _   => println("Unknow key")
    }
  }


}
