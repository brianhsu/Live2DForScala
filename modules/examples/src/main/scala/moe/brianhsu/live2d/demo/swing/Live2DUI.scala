package moe.brianhsu.live2d.demo.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import moe.brianhsu.live2d.adapter.gateway.opengl.jogl.JavaOpenGLBinding
import moe.brianhsu.live2d.adapter.gateway.renderer.jogl.JOGLCanvasInfoReader
import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.swing.widget.{SwingEffectSelector, SwingExpressionSelector, SwingMotionSelector, SwingStatusBar, SwingToolbar}

import java.awt.event._
import javax.swing.SwingUtilities


class Live2DUI(val canvas: GLCanvas) extends MouseAdapter with GLEventListener with KeyListener {

  val expressionSelector = new SwingExpressionSelector(this)
  val motionSelector = new SwingMotionSelector(this)
  val effectSelector = new SwingEffectSelector(this)
  val toolbar = new SwingToolbar(this)
  val statusBar = new SwingStatusBar()

  private var animator: Option[FixedFPSAnimator] = None
  private val canvasInfo = new JOGLCanvasInfoReader(canvas)
  private var mDemoAppHolder: Option[DemoApp] = None
  private var lastMouseX: Option[Int] = None
  private var lastMouseY: Option[Int] = None

  def demoAppHolder: Option[DemoApp] = mDemoAppHolder

  private def runOnOpenGLThread(callback: => Any): Any = {
    canvas.invoke(true, (_: GLAutoDrawable) => {
      callback
      true
    })
  }

  override def init(drawable: GLAutoDrawable): Unit = {
    this.mDemoAppHolder = Option(createLive2DDemoApp(drawable))
    this.animator = Option(new FixedFPSAnimator(60, drawable))
    this.animator.foreach { x => x.start() }
  }

  private def createLive2DDemoApp(drawable: GLAutoDrawable): DemoApp = {
    implicit val openGL: JavaOpenGLBinding = new JavaOpenGLBinding(drawable.getGL.getGL2)

    new DemoApp(canvasInfo, runOnOpenGLThread) {
      override def onStatusUpdated(status: String): Unit = statusBar.setText(status)
      override def onAvatarLoaded(live2DView: DemoApp): Unit = {
        live2DView.avatarHolder.foreach { avatar =>
          expressionSelector.updateExpressionList(avatar)
          motionSelector.updateMotionTree(avatar)
        }
        live2DView.basicUpdateStrategyHolder.foreach { strategy =>
          effectSelector.syncWithStrategy(strategy)
        }
      }
    }
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {
    this.animator.foreach(_.stop())
  }

  override def display(drawable: GLAutoDrawable): Unit = {
    this.mDemoAppHolder.foreach(_.display())
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
    this.mDemoAppHolder.foreach(_.resize())
  }

  override def mouseDragged(e: MouseEvent): Unit = {
    if (SwingUtilities.isLeftMouseButton(e)) {
      this.mDemoAppHolder.foreach(_.onMouseDragged(e.getX, e.getY))
    }
    if (SwingUtilities.isRightMouseButton(e)) {
      val offsetX = this.lastMouseX.map(e.getX - _).getOrElse(0).toFloat * 0.002f
      val offsetY = this.lastMouseY.map(_ - e.getY).getOrElse(0).toFloat * 0.002f

      this.mDemoAppHolder.foreach(_.move(offsetX, offsetY))

      this.lastMouseX = Some(e.getX)
      this.lastMouseY = Some(e.getY)
    }
  }
  override def mouseMoved(mouseEvent: MouseEvent): Unit = {
    this.mDemoAppHolder.foreach(_.onMouseMoved(mouseEvent.getX, mouseEvent.getY))
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    this.mDemoAppHolder.foreach(_.onMouseReleased(mouseEvent.getX, mouseEvent.getY))
    this.lastMouseX = None
    this.lastMouseY = None
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}
  override def keyReleased(keyEvent: KeyEvent): Unit = {
    this.mDemoAppHolder.foreach(_.keyReleased(keyEvent.getKeyChar))
  }
  override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
    this.mDemoAppHolder.foreach(_.zoom(e.getScrollAmount * -e.getWheelRotation * 0.01f))
  }

}
