package moe.brianhsu.porting.live2d.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import moe.brianhsu.live2d.adapter.gateway.opengl.jogl.JavaOpenGLBinding
import moe.brianhsu.live2d.adapter.gateway.renderer.jogl.JOGLCanvasInfoReader
import moe.brianhsu.live2d.enitiy.avatar.Avatar
import moe.brianhsu.porting.live2d.Live2DView
import moe.brianhsu.porting.live2d.demo.FixedFPSAnimator
import moe.brianhsu.porting.live2d.swing.widget.{ExpressionSelector, MotionSelector}

import java.awt.event._
import javax.swing.SwingUtilities


class Live2DWidget(val canvas: GLCanvas, afterInit: Live2DWidget => Any) extends MouseAdapter with GLEventListener with KeyListener {

  private var animator: Option[FixedFPSAnimator] = None
  private var mLive2DViewHolder: Option[Live2DView] = None
  private val canvasInfo = new JOGLCanvasInfoReader(canvas)
  val expressionSelector = new ExpressionSelector(this)
  val motionSelector = new MotionSelector(this)

  def live2DView: Option[Live2DView] = mLive2DViewHolder

  def doWithLive2DView(callback: Live2DView => Any): Unit = {
    live2DView.foreach(callback)
  }

  private def runOnOpenGLThread(callback: => Any): Unit = {
    canvas.invoke(true, (_: GLAutoDrawable) => {
      callback
      true
    })
  }

  override def init(drawable: GLAutoDrawable): Unit = {
    implicit val openGL: JavaOpenGLBinding = new JavaOpenGLBinding(drawable.getGL.getGL2)

    val live2DView = new Live2DView(canvasInfo, runOnOpenGLThread) {
      override def onAvatarLoaded(avatar: Avatar): Unit = {
        expressionSelector.updateExpressionList(avatar)
        motionSelector.updateMotionTree(avatar)
      }
    }

    this.mLive2DViewHolder = Option(live2DView)
    this.animator = Option(new FixedFPSAnimator(60, drawable))
    this.animator.foreach { x => x.start() }
    this.afterInit(this)
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {
    this.animator.foreach(_.stop())
  }

  override def display(drawable: GLAutoDrawable): Unit = {
    this.mLive2DViewHolder.foreach(_.display())
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
    this.mLive2DViewHolder.foreach(_.resize())
  }

  private var lastX: Option[Int] = None
  private var lastY: Option[Int] = None
  override def mouseDragged(e: MouseEvent): Unit = {
    if (SwingUtilities.isLeftMouseButton(e)) {
      this.mLive2DViewHolder.foreach(_.onMouseDragged(e.getX, e.getY))
    }
    if (SwingUtilities.isRightMouseButton(e)) {
      val offsetX = this.lastX.map(e.getX - _).getOrElse(0).toFloat * 0.002f
      val offsetY = this.lastY.map(_ - e.getY).getOrElse(0).toFloat * 0.002f

      this.mLive2DViewHolder.foreach(_.move(offsetX, offsetY))

      this.lastX = Some(e.getX)
      this.lastY = Some(e.getY)
    }
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    this.mLive2DViewHolder.foreach(_.onMouseReleased(mouseEvent.getX, mouseEvent.getY))
    this.lastX = None
    this.lastY = None
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}
  override def keyReleased(keyEvent: KeyEvent): Unit = {
    this.mLive2DViewHolder.foreach(_.keyReleased(keyEvent.getKeyChar))
  }
  override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
    this.mLive2DViewHolder.foreach(_.zoom(e.getScrollAmount * -e.getWheelRotation * 0.01f))
  }

}
