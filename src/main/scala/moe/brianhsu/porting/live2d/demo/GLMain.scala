package moe.brianhsu.porting.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import moe.brianhsu.porting.live2d.adapter.jogl.{JavaOpenGL, JavaOpenGLCanvasInfo}

import java.awt.event.{KeyEvent, KeyListener, MouseAdapter, MouseEvent, MouseWheelEvent}
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import javax.swing.SwingUtilities

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

  def start(): Unit = {
    createScheduledFuture()
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
    this.animator = Option(new FixedFPSAnimator(60, drawable))
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

  private var lastX: Option[Int] = None
  private var lastY: Option[Int] = None
  override def mouseDragged(e: MouseEvent): Unit = {
    if (SwingUtilities.isLeftMouseButton(e)) {
      this.view.foreach(_.onMouseDragged(e.getX, e.getY))
    }
    if (SwingUtilities.isRightMouseButton(e)) {
      val offsetX = this.lastX.map(e.getX - _).getOrElse(0).toFloat * 0.002f
      val offsetY = this.lastY.map(_ - e.getY).getOrElse(0).toFloat * 0.002f

      this.view.foreach(_.move(offsetX, offsetY))

      this.lastX = Some(e.getX)
      this.lastY = Some(e.getY)
    }
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    this.view.foreach(_.onMouseReleased(mouseEvent.getX, mouseEvent.getY))
    this.lastX = None
    this.lastY = None
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}
  override def keyReleased(keyEvent: KeyEvent): Unit = {
    this.view.foreach(_.keyReleased(keyEvent.getKeyChar))
  }
  override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
    this.view.foreach(_.zoom(e.getScrollAmount * -e.getWheelRotation * 0.01f))
  }

}
