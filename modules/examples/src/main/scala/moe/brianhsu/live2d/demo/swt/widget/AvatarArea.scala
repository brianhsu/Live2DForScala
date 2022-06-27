package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.adapter.gateway.opengl.lwjgl.{LWJGLBinding, SWTOpenGLCanvasInfoReader}
import moe.brianhsu.live2d.demo.app.DemoApp
import org.eclipse.swt.SWT
import org.eclipse.swt.events.{KeyEvent, KeyListener, MouseEvent, MouseListener}
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.opengl.{GLCanvas, GLData}
import org.eclipse.swt.widgets._
import org.lwjgl.opengl.GL

class AvatarArea(display: Display, parent: Composite) extends Composite(parent, SWT.NONE) {
  private var lastX: Option[Int] = None
  private var lastY: Option[Int] = None

  private val canvas = createGLCanvas()
  private implicit val openGLBinding = new LWJGLBinding
  private val canvasInfo = new SWTOpenGLCanvasInfoReader(canvas)
  private val demoApp = new DemoApp(canvasInfo, runOnOpenGLThread) {
    override def onAvatarLoaded(live2DView: DemoApp): Unit = {}
    override def onStatusUpdated(status: String): Unit = {}
  }

  {
    this.setLayout(new FillLayout)
    this.canvas.addListener(SWT.Resize, _ => {
      this.demoApp.resize()
      this.demoApp.display()
    })
    this.canvas.addPaintListener(_ => updater.run())
    this.canvas.addKeyListener(new KeyListener {
      override def keyPressed(e: KeyEvent): Unit = {}
      override def keyReleased(e: KeyEvent): Unit = {
        demoApp.keyReleased(e.character)
      }
    })

    this.canvas.addMouseWheelListener { e: MouseEvent =>
      demoApp.zoom(e.count * 0.01f)
    }
    this.canvas.addMouseMoveListener( mouseEvent => {
      if ((mouseEvent.stateMask & SWT.BUTTON1) != 0) {
        demoApp.onMouseDragged(mouseEvent.x, mouseEvent.y)
      }

      if ((mouseEvent.stateMask & SWT.BUTTON3) != 0) {
        val offsetX = this.lastX.map(mouseEvent.x - _).getOrElse(0).toFloat * 0.0020f
        val offsetY = this.lastY.map(_ - mouseEvent.y).getOrElse(0).toFloat * 0.0020f

        demoApp.move(offsetX, offsetY)

        this.lastX = Some(mouseEvent.x)
        this.lastY = Some(mouseEvent.y)
      }
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

  val updater = new Runnable() {
    override def run(): Unit = {
      if (!canvas.isDisposed) {
        demoApp.display()
        canvas.swapBuffers()
        display.asyncExec(this)
      }
    }
  }
}
