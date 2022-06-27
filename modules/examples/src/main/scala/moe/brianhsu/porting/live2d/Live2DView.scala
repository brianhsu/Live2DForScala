package moe.brianhsu.porting.live2d

import moe.brianhsu.live2d.adapter.gateway.avatar.AvatarFileReader
import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPhysicsReader, AvatarPoseReader, FaceDirectionByMouse}
import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.sprite.Sprite
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureManager
import moe.brianhsu.live2d.enitiy.updater.SystemNanoTimeBasedFrameInfo
import moe.brianhsu.live2d.usecase.renderer.opengl.AvatarRenderer
import moe.brianhsu.live2d.usecase.renderer.opengl.shader.SpriteShader
import moe.brianhsu.live2d.usecase.renderer.opengl.sprite.SpriteRenderer
import moe.brianhsu.live2d.usecase.renderer.viewport.{ProjectionMatrixCalculator, ViewOrientation, ViewPortMatrixCalculator}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy
import moe.brianhsu.porting.live2d.Live2DView.{ClickAndDrag, FaceDirectionMode, FollowMouse, OnOpenGLThread}
import moe.brianhsu.porting.live2d.demo.sprite.{BackgroundSprite, GearSprite, PowerSprite}

import java.awt.Color
import scala.annotation.unused
import scala.util.Try

object Live2DView {
  type OnOpenGLThread = (=> Any) => Unit

  sealed trait FaceDirectionMode
  case object FollowMouse extends FaceDirectionMode
  case object ClickAndDrag extends FaceDirectionMode
}

abstract class Live2DView(drawCanvasInfo: DrawCanvasInfoReader, onOpenGLThread: OnOpenGLThread)
                         (private implicit val openGL: OpenGLBinding) {

  import openGL.constants._

  private implicit val cubismCore: JnaNativeCubismAPILoader = new JnaNativeCubismAPILoader()

  private val defaultBackgroundTexture = "/texture/back_class_normal.png"

  private var zoom: Float = 2.0f
  private var offsetX: Float = 0.0f
  private var offsetY: Float = 0.0f

  private val frameTimeCalculator = new SystemNanoTimeBasedFrameInfo
  private val textureManager = TextureManager.getInstance

  private val backgroundTexture = textureManager.loadTexture(defaultBackgroundTexture)
  private val backgroundSprite: Sprite = new BackgroundSprite(drawCanvasInfo, backgroundTexture)
  private val powerTexture = textureManager.loadTexture("/texture/close.png")
  private val powerSprite: Sprite = new PowerSprite(drawCanvasInfo, powerTexture)
  private val gearTexture = textureManager.loadTexture("/texture/icon_gear.png")
  private val gearSprite: Sprite = new GearSprite(drawCanvasInfo, gearTexture)
  private var sprites = this.backgroundSprite :: this.gearSprite :: this.powerSprite :: Nil

  private var mAvatarHolder: Option[Avatar] = None
  private var modelHolder: Option[Live2DModel] = mAvatarHolder.map(_.model)
  private var rendererHolder: Option[AvatarRenderer] = modelHolder.map(model => AvatarRenderer(model))
  private val spriteRenderer = new SpriteRenderer(new SpriteShader)
  private var mUpdateStrategyHolder: Option[BasicUpdateStrategy] = mAvatarHolder.map(a => {
    a.updateStrategyHolder = Some(new BasicUpdateStrategy(a.avatarSettings, a.model))
    a.updateStrategyHolder.get.asInstanceOf[BasicUpdateStrategy]
  })

  private val targetPointCalculator = new FaceDirectionByMouse(60)
  private val faceDirection = new FaceDirection(targetPointCalculator)

  private lazy val viewPortMatrixCalculator = new ViewPortMatrixCalculator
  private lazy val projectionMatrixCalculator = new ProjectionMatrixCalculator(drawCanvasInfo)

  var faceDirectionMode: FaceDirectionMode = ClickAndDrag
  private var backgroundColor = new Color(0, 255, 0)

  {
    setupAvatarEffects()
    initOpenGL()
  }

  def avatarHolder: Option[Avatar] = mAvatarHolder
  def basicUpdateStrategyHolder: Option[BasicUpdateStrategy] = mUpdateStrategyHolder

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

  private def createBackgroundSprite(textureFile: String): BackgroundSprite = {
    new BackgroundSprite(
      drawCanvasInfo,
      textureManager.loadTexture(textureFile)
    )
  }

  def resetModel(): Unit = {
    modelHolder.foreach(_.reset())
  }

  def disableBreath(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[Breath])
    }
  }

  def enableBreath(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= new Breath
    }
  }

  def disableFaceDirection(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[FaceDirection])
    }
  }

  def resetFaceDirection(): Unit = {
    targetPointCalculator.updateFaceTargetCoordinate(0, 0)
  }

  def enableFaceDirection(): Unit = {
    for {
      _ <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= faceDirection
    }
  }

  def disableEyeBlink(): Unit = {
    for (strategy <- this.mUpdateStrategyHolder) {
      strategy.effects = strategy.effects.filterNot(_.isInstanceOf[EyeBlink])
    }
  }

  def enableEyeBlink(): Unit = {
    for {
      avatar <- this.avatarHolder
      strategy <- this.mUpdateStrategyHolder
    } {
      strategy.effects ::= new EyeBlink(avatar.avatarSettings)
    }
  }

  def display(isForceUpdate: Boolean = false): Unit = {
    clearScreen()

    sprites.foreach(spriteRenderer.draw)

    this.frameTimeCalculator.updateFrameTime()

    for {
      avatar <- mAvatarHolder
      model <- modelHolder
      renderer <- rendererHolder
    } {

      val projection = projectionMatrixCalculator.calculate(
        viewPortMatrixCalculator.viewPortMatrix,
        isForceUpdate,
        updateModelMatrix(model)
      )

      avatar.update(this.frameTimeCalculator)
      renderer.draw(projection)
    }

    def updateModelMatrix(model: Live2DModel)(@unused viewOrientation: ViewOrientation): Unit = {
      model.modelMatrix = model.modelMatrix
        .scaleToHeight(zoom)
        .left(offsetX)
        .top(offsetY)
    }

  }

  def resize(): Unit = {

    viewPortMatrixCalculator.updateViewPort(
      drawCanvasInfo.currentCanvasWidth,
      drawCanvasInfo.currentCanvasHeight
    )

    openGL.glViewport(0, 0, drawCanvasInfo.currentSurfaceWidth, drawCanvasInfo.currentSurfaceHeight)

    sprites.foreach(_.resize())

    this.display()
  }

  def onMouseMoved(x: Int, y: Int): Unit = {
    if (faceDirectionMode == FollowMouse) {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
      val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
      val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
      targetPointCalculator.updateFaceTargetCoordinate(viewX, viewY)
    }
  }

  def onMouseReleased(x: Int, y: Int): Unit = {
    val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
    val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)

    if (faceDirectionMode == ClickAndDrag) {
      resetFaceDirection()
    }

    for {
      _ <- mAvatarHolder
      model <- modelHolder
    } {

      if (powerSprite.isHit(x.toFloat, y.toFloat)) {
        onStatusUpdated("Clicked on Power sprite.")
      } else if (gearSprite.isHit(x.toFloat, y.toFloat)) {
        onStatusUpdated("Clicked on Power sprite.")
      } else {
        val hitAreaHolder = for {
          avatar <- mAvatarHolder
          hitArea <- avatar.avatarSettings.hitArea.find(area => model.isHit(area.id, transformedX, transformedY))
        } yield {
          hitArea.name
        }
        hitAreaHolder match {
          case Some(area) => onStatusUpdated(s"Clicked on Avatar's $area.")
          case None => onStatusUpdated("Clicked nothing.")
        }
      }
    }
  }

  def onMouseDragged(x: Int, y: Int): Unit = {
    if (faceDirectionMode == ClickAndDrag) {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)
      val viewX = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedX(transformedX)
      val viewY = viewPortMatrixCalculator.viewPortMatrix.invertedTransformedY(transformedY)
      targetPointCalculator.updateFaceTargetCoordinate(viewX, viewY)
    }
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
      avatar <- mAvatarHolder
      updateStrategy <- mUpdateStrategyHolder
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

  def startMotion(group: String, i: Int, isLoop: Boolean): Unit = {
    mUpdateStrategyHolder.foreach { updateStrategy =>
      updateStrategy.startMotion(group, i, isLoop)
    }
  }

  def startExpression(name: String): Unit = {
    mUpdateStrategyHolder.foreach { updateStrategy =>
      updateStrategy.startExpression(name)
    }
  }

  private def clearScreen(): Unit = {
    openGL.glClearColor(
      backgroundColor.getRed / 255.0f,
      backgroundColor.getGreen / 255.0f,
      backgroundColor.getBlue / 255.0f,
      1.0f
    )
    openGL.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    openGL.glClearDepth(1.0)
  }

  def switchAvatar(directoryPath: String): Try[Avatar] = {
    onStatusUpdated(s"Loading $directoryPath...")

    val newAvatarHolder = new AvatarFileReader(directoryPath).loadAvatar()

    this.mAvatarHolder = newAvatarHolder.toOption.orElse(this.mAvatarHolder)
    this.modelHolder = mAvatarHolder.map(_.model)
    this.mUpdateStrategyHolder = mAvatarHolder.map(a => {
      onStatusUpdated(s"$directoryPath loaded.")
      a.updateStrategyHolder = Some(new BasicUpdateStrategy(a.avatarSettings, a.model))
      a.updateStrategyHolder.get.asInstanceOf[BasicUpdateStrategy]
    })
    setupAvatarEffects()
    newAvatarHolder.foreach(_ => onAvatarLoaded(this))

    onOpenGLThread {
      this.rendererHolder = modelHolder.map(model => AvatarRenderer(model))
      initOpenGL()
    }
    newAvatarHolder
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
      case 'z' => switchAvatar("src/main/resources/Haru")
      case 'x' => switchAvatar("src/main/resources/Mark")
      case 'c' => switchAvatar("src/main/resources/Rice")
      case 'v' => switchAvatar("src/main/resources/Natori")
      case 'b' => switchAvatar("src/main/resources/Hiyori")
      case _   => println("Unknow key")
    }
  }

  def onAvatarLoaded(live2DView: Live2DView): Unit
  def onStatusUpdated(status: String): Unit
}
