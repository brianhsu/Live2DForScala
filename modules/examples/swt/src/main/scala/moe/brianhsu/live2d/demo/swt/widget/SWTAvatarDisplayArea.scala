package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.adapter.gateway.opengl.lwjgl.{LWJGLBinding, SWTOpenGLCanvasInfoReader}
import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.swt.widget.SWTAvatarDisplayArea.AvatarListener
import org.eclipse.swt.SWT
import org.eclipse.swt.events.{KeyEvent, KeyListener, MouseEvent, MouseListener}
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.opengl.{GLCanvas, GLData}
import org.eclipse.swt.widgets._
import org.lwjgl.opengl.GL

object SWTAvatarDisplayArea {
  trait AvatarListener {
    def onAvatarLoaded(live2DView: DemoApp): Unit
    def onStatusUpdated(status: String): Unit
  }
}

class SWTAvatarDisplayArea(parent: Composite) extends Composite(parent, SWT.NONE) {
  private var lastX: Option[Int] = None
  private var lastY: Option[Int] = None
  private var avatarListenerHolder: Option[AvatarListener] = None

  protected val canvas = createGLCanvas()
  private implicit val openGLBinding: LWJGLBinding = new LWJGLBinding
  private val canvasInfo = new SWTOpenGLCanvasInfoReader(canvas)
  private val canvasUpdater: Runnable = new CanvasUpdater

  val demoApp: DemoApp = new DemoApp(canvasInfo, runOnOpenGLThread) {
    override def onAvatarLoaded(live2DView: DemoApp): Unit = {
      avatarListenerHolder.foreach(_.onAvatarLoaded(live2DView))
    }
    override def onStatusUpdated(status: String): Unit = {
      avatarListenerHolder.foreach(_.onStatusUpdated(status))
    }
  }

  {
    this.setLayout(new FillLayout)
    setupResizeListener()
    setupMouseListener()
    setupKeyListener()
    this.canvasUpdater.run()
  }

  def setAvatarListener(listener: AvatarListener): Unit = {
    this.avatarListenerHolder = Some(listener)
  }

  private def setupKeyListener(): Unit = {
    this.canvas.addKeyListener(new KeyListener {
      override def keyPressed(e: KeyEvent): Unit = {}

      override def keyReleased(e: KeyEvent): Unit = {
        demoApp.keyReleased(e.character)
      }
    })
  }

  private def setupMouseListener(): Unit = {
    this.canvas.addMouseWheelListener { e: MouseEvent =>
      demoApp.zoom(e.count * 0.01f)
    }
    this.canvas.addMouseMoveListener(mouseEvent => {
      handleNormalMouseMove(mouseEvent)
      handleLeftDrag(mouseEvent)
      handleRightDrag(mouseEvent)
    })

    this.canvas.addMouseListener(new MouseListener() {
      override def mouseDoubleClick(e: MouseEvent): Unit = {}

      override def mouseDown(e: MouseEvent): Unit = {}

      override def mouseUp(e: MouseEvent): Unit = {
        if ((e.stateMask & SWT.BUTTON1) != 0) {
          demoApp.onMouseReleased(e.x, e.y)
        }
        lastX = None
        lastY = None
      }
    })
  }

  private def handleNormalMouseMove(mouseEvent: MouseEvent) = {
    if (mouseEvent.stateMask == 0) {
      demoApp.onMouseMoved(mouseEvent.x, mouseEvent.y)
    }
  }

  private def setupResizeListener(): Unit = {
    this.canvas.addListener(SWT.Resize, _ => {
      this.demoApp.resize()
      this.demoApp.display()
    })
  }


  private def handleLeftDrag(mouseEvent: MouseEvent): Unit = {
    if ((mouseEvent.stateMask & SWT.BUTTON1) != 0) {
      demoApp.onMouseDragged(mouseEvent.x, mouseEvent.y)
    }
  }

  private def handleRightDrag(mouseEvent: MouseEvent): Unit = {
    if ((mouseEvent.stateMask & SWT.BUTTON3) != 0) {
      val offsetX = this.lastX.map(mouseEvent.x - _).getOrElse(0).toFloat * 0.0020f
      val offsetY = this.lastY.map(_ - mouseEvent.y).getOrElse(0).toFloat * 0.0020f

      demoApp.move(offsetX, offsetY)

      this.lastX = Some(mouseEvent.x)
      this.lastY = Some(mouseEvent.y)
    }
  }

  private def createGLCanvas() = {
    val glCanvas = new GLCanvas(this, SWT.NONE, createDoubleBufferGLData())
    glCanvas.setCurrent()
    GL.createCapabilities()
    glCanvas
  }

  private def createDoubleBufferGLData(): GLData = {
    val glData = new GLData
    glData.doubleBuffer = true
    glData
  }

  private def runOnOpenGLThread(callback: => Any): Any = {
    callback
  }

  private class CanvasUpdater extends Runnable {

    private val FrameRate = System.getProperty("os.name") match {
      case os if os.contains("win") => 144 // I don't know why Windows need a higher value to make it smooth
      case os if os.contains("linux") => 60
      case _ => 60
    }

    private val threshold = ((1 / FrameRate.toFloat) * 1000).toInt

    override def run(): Unit = {
      if (!canvas.isDisposed) {
        demoApp.display()
        canvas.swapBuffers()
        parent.getDisplay.timerExec(threshold, this)
      }
    }
  }

}
