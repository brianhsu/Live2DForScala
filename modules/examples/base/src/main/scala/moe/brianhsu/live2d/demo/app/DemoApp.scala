package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.adapter.gateway.avatar.AvatarFileReader
import moe.brianhsu.live2d.adapter.gateway.avatar.effect.{AvatarPhysicsReader, AvatarPoseReader}
import moe.brianhsu.live2d.adapter.gateway.core.JnaNativeCubismAPILoader
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.demo.app.DemoApp.OnOpenGLThread
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, LipSyncFromMotionSound}
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.updater.SystemNanoTimeBasedFrameInfo
import moe.brianhsu.live2d.usecase.renderer.opengl.AvatarRenderer
import moe.brianhsu.live2d.usecase.renderer.viewport.{ProjectionMatrixCalculator, ViewOrientation, ViewPortMatrixCalculator}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy

import scala.annotation.unused
import scala.util.Try

object DemoApp {
  type OnOpenGLThread = (=> Any) => Unit

  sealed trait FaceDirectionMode
  case object FollowMouse extends FaceDirectionMode
  case object ClickAndDrag extends FaceDirectionMode
}

abstract class DemoApp(drawCanvasInfo: DrawCanvasInfoReader, onOpenGLThread: OnOpenGLThread)
                      (override protected implicit val openGL: OpenGLBinding) extends OpenGLBase(drawCanvasInfo, onOpenGLThread) with SpriteControl with EffectControl {

  import openGL.constants._

  protected lazy val viewPortMatrixCalculator = new ViewPortMatrixCalculator
  protected lazy val projectionMatrixCalculator = new ProjectionMatrixCalculator(drawCanvasInfo)
  protected var mAvatarHolder: Option[Avatar] = None
  protected var modelHolder: Option[Live2DModel] = mAvatarHolder.map(_.model)
  protected var rendererHolder: Option[AvatarRenderer] = modelHolder.map(model => AvatarRenderer(model))
  protected var mUpdateStrategyHolder: Option[BasicUpdateStrategy] = mAvatarHolder.map(a => {
    a.updateStrategyHolder = Some(new BasicUpdateStrategy(a.avatarSettings, a.model, motionListener = Some(this)))
    a.updateStrategyHolder.get.asInstanceOf[BasicUpdateStrategy]
  })

  private implicit val cubismCore: JnaNativeCubismAPILoader = new JnaNativeCubismAPILoader()
  private val frameTimeCalculator = new SystemNanoTimeBasedFrameInfo

  private var zoom: Float = 2.0f
  private var offsetX: Float = 0.0f
  private var offsetY: Float = 0.0f

  {
    setupAvatarEffects()
    initOpenGL()
  }

  def avatarHolder: Option[Avatar] = mAvatarHolder
  def basicUpdateStrategyHolder: Option[BasicUpdateStrategy] = mUpdateStrategyHolder

  def resetModel(): Unit = {
    modelHolder.foreach(_.reset())
  }

  override def display(isForceUpdate: Boolean = false): Unit = {
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

  override def onMouseReleased(x: Int, y: Int): Unit = {
    super.onMouseReleased(x, y)

    if (powerSprite.isHit(x.toFloat, y.toFloat)) {
      onStatusUpdated("Clicked on Power sprite.")
    } else if (gearSprite.isHit(x.toFloat, y.toFloat)) {
      onStatusUpdated("Clicked on Gear sprite.")
    } else {
      onStatusUpdated("Clicked nothing.")
    }

    for {
      _ <- mAvatarHolder
      model <- modelHolder
    } {
      val transformedX = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedX(x.toFloat)
      val transformedY = viewPortMatrixCalculator.drawCanvasToModelMatrix.transformedY(y.toFloat)

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
        poseHolder,
        Some(new LipSyncFromMotionSound(avatar.avatarSettings, 100))
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

    this.disableMicLipSync()
    this.disableLipSyncFromMotionSound()

    val newAvatarHolder = new AvatarFileReader(directoryPath).loadAvatar()

    this.mAvatarHolder = newAvatarHolder.toOption.orElse(this.mAvatarHolder)
    this.modelHolder = mAvatarHolder.map(_.model)
    this.mUpdateStrategyHolder = mAvatarHolder.map(a => {
      onStatusUpdated(s"$directoryPath loaded.")
      a.updateStrategyHolder = Some(new BasicUpdateStrategy(a.avatarSettings, a.model, motionListener = Some(this)))
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
    this.zoom = (level + zoom).max(0.5f)
    this.display(true)
  }

  def keyReleased(key: Char): Unit = {
    key match {
      case 'z' => switchAvatar("src/main/resources/Haru")
      case 'x' => switchAvatar("src/main/resources/Mark")
      case 'c' => switchAvatar("src/main/resources/Rice")
      case 'v' => switchAvatar("src/main/resources/Natori")
      case 'b' => switchAvatar("src/main/resources/Hiyori")
      case _   =>
    }
  }

  def onAvatarLoaded(live2DView: DemoApp): Unit
  def onStatusUpdated(status: String): Unit
}
