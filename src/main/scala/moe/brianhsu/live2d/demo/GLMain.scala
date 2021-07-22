package moe.brianhsu.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import moe.brianhsu.live2d.adapter.jogl.{JavaOpenGL, JavaOpenGLCanvasInfo}

import java.awt.event.{KeyEvent, KeyListener, MouseAdapter, MouseEvent}
import java.util.concurrent.{ScheduledFuture, ScheduledThreadPoolExecutor, TimeUnit}

class FixedFPSAnimator(fps: Int, drawable: GLAutoDrawable) {
  private val scheduledThreadPool = new ScheduledThreadPoolExecutor(1)
  private val updateOpenGLCanvas = new Runnable {
    override def run(): Unit = {
      try {
        drawable.display()
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }

  private var scheduler: Option[ScheduledFuture[_]] = None

  def start(): Unit = {
    scheduler = Some(createScheduledFuture())
  }

  def stop(): Unit = {
    scheduledThreadPool.shutdown()
  }

  private def createScheduledFuture() = {
    scheduledThreadPool.scheduleAtFixedRate(
      updateOpenGLCanvas, 0, calculateExecutionPeriod,
      TimeUnit.MILLISECONDS
    )
  }

  private def calculateExecutionPeriod: Int = {
    val updateIntervalInSeconds = 1 / fps.toDouble
    val updateIntervalInMilliSeconds = updateIntervalInSeconds * 1000
    updateIntervalInMilliSeconds.toInt
  }

}

class GLMain(canvas: GLCanvas) extends MouseAdapter with GLEventListener with KeyListener {

  private var animator: Option[FixedFPSAnimator] = None
  private var view: Option[LAppView] = None
  private val canvasInfo = new JavaOpenGLCanvasInfo(canvas)

  override def init(drawable: GLAutoDrawable): Unit = {
    implicit val openGL: JavaOpenGL = new JavaOpenGL(drawable.getGL.getGL2)
    this.view = Option(new LAppView(canvasInfo))
    this.animator = Option(new FixedFPSAnimator(30, drawable))
    this.animator.foreach { x => x.start() }
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {
    this.animator.foreach(_.stop())
  }

  override def display(drawable: GLAutoDrawable): Unit = {
    this.view.foreach(_.display())
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
    this.view.foreach(_.resize())
  }

  override def mouseDragged(e: MouseEvent): Unit = {
    this.view.foreach(_.onMouseDragged(e.getX, e.getY))
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    this.view.foreach(_.onMouseReleased(mouseEvent.getX, mouseEvent.getY))
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}
  override def keyReleased(keyEvent: KeyEvent): Unit = {
    this.view.foreach(_.keyReleased(keyEvent))

  }
}
