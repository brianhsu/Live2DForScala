package moe.brianhsu.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}

import java.awt.event.{KeyEvent, KeyListener, MouseAdapter, MouseEvent, MouseWheelEvent}
import java.util.concurrent.{ScheduledFuture, ScheduledThreadPoolExecutor, TimeUnit}

class FixedFPSAnimator(fps: Int, drawable: GLAutoDrawable) {
  private val scheduledThreadPool = new ScheduledThreadPoolExecutor(1)
  private val updateOpenGLCanvas = new Runnable {
    override def run(): Unit = drawable.display()
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
  private val canvasInfo = new OpenGLCanvasInfo(canvas)

  override def init(drawable: GLAutoDrawable): Unit = {
    this.view = Option(new LAppView(canvasInfo, drawable))
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
    println(s"width: ${canvas.getWidth}, height: ${canvas.getHeight}")
    this.view.foreach(_.resize())
  }

  override def mouseWheelMoved(var1: MouseWheelEvent): Unit = {
  }

  override def mouseDragged(e: MouseEvent): Unit = {
    this.view.foreach(_.onMouseDragged(e.getX, e.getY))
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    this.view.foreach(_.onMouseReleased())
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}

  override def keyReleased(keyEvent: KeyEvent): Unit = {
    if (keyEvent.getKeyCode == KeyEvent.VK_SPACE) {
      println("===> Space hitted")
      this.view.foreach(_.resetModel())
    }
  }
}
