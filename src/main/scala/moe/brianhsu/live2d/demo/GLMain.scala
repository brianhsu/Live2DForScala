package moe.brianhsu.live2d.demo

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
    println("===> period:" + calculateExecutionPeriod)
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

class GLMain extends MouseAdapter with GLEventListener with KeyListener {

  private var animator: Option[FixedFPSAnimator] = None
  private var view: Option[LAppView] = None


  override def init(drawable: GLAutoDrawable): Unit = {
    this.view = Option(new LAppView(drawable))
    this.animator = Option(new FixedFPSAnimator(30, drawable))
    this.animator.foreach { x => x.start() }
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {
    this.animator.foreach(_.stop())
  }

  private var lastTimestamp = System.currentTimeMillis() / 1000
  private var fpsCount = 0

  override def display(drawable: GLAutoDrawable): Unit = {
    this.view.foreach(_.display())
    val currentSeconds = System.currentTimeMillis() / 1000
    if (lastTimestamp != currentSeconds) {
      this.lastTimestamp = currentSeconds
      println("FPS:" + fpsCount)
      fpsCount = 0
    } else {
      fpsCount += 1
    }
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
    this.view.foreach(_.resize())
  }

  override def mouseWheelMoved(var1: MouseWheelEvent): Unit = {
  }

  override def mouseDragged(e: MouseEvent): Unit = {
    this.view.foreach(_.onMouseDragged(e.getX, e.getY))
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    this.view.foreach(_.onMouseDragged(0, 0))
  }

  override def mouseClicked(e: MouseEvent): Unit = {
    this.view.foreach(_.onMouseClick(e.getX, e.getY))
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}

  override def keyReleased(keyEvent: KeyEvent): Unit = {
    if (keyEvent.getKeyCode == KeyEvent.VK_SPACE) {
      println("===> Space hitted")
      this.view.foreach(_.resetMode())
    }
  }
}
