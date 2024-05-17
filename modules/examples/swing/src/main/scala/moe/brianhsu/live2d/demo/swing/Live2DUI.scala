package moe.brianhsu.live2d.demo.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import moe.brianhsu.live2d.adapter.gateway.opengl.jogl.JavaOpenGLBinding
import moe.brianhsu.live2d.adapter.gateway.renderer.jogl.JOGLCanvasInfoReader
import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.swing.widget.{SwingEffectSelector, SwingExpressionSelector, SwingFaceTrackingPane, SwingMotionSelector, SwingStatusBar, SwingToolbar}

import java.awt.event._
import javax.swing.SwingUtilities
import scala.util.Try
import java.util.concurrent.atomic.AtomicReference

class Live2DUI(val canvas: GLCanvas) extends MouseAdapter with GLEventListener with KeyListener {
  private val animatorRef = new AtomicReference[Option[FixedFPSAnimator]](None)
  private val canvasInfo = new JOGLCanvasInfoReader(canvas)
  private val mDemoAppHolderRef = new AtomicReference[Option[DemoApp]](None)
  private val lastMouseX = new AtomicReference[Option[Int]](None)
  private val lastMouseY = new AtomicReference[Option[Int]](None)

  val expressionSelector = new SwingExpressionSelector(this)
  val motionSelector = new SwingMotionSelector(this)
  val effectSelector = new SwingEffectSelector(this)
  val toolbar = new SwingToolbar(this)
  val statusBar = new SwingStatusBar()
  val faceTrackingPane = new SwingFaceTrackingPane(this)


  def demoAppHolder: Option[DemoApp] = mDemoAppHolderRef.get()

  private def runOnOpenGLThread(callback: => Any): Unit = {
    canvas.invoke(true, (_: GLAutoDrawable) => {
      Try(callback)
      true
    })
  }

  override def init(drawable: GLAutoDrawable): Unit = {
    runOnOpenGLThread {
      Try {
        val demoApp = createLive2DDemoApp(drawable)
        mDemoAppHolderRef.set(Option(demoApp))
        animatorRef.set(Option(new FixedFPSAnimator(60, drawable)))
        animatorRef.get().foreach(_.start())
      }
    }
  }

  private def createLive2DDemoApp(drawable: GLAutoDrawable): DemoApp = {
    implicit val openGL: JavaOpenGLBinding = new JavaOpenGLBinding(drawable.getGL.getGL2)

    new DemoApp(canvasInfo, runOnOpenGLThread) {
      override def onStatusUpdated(status: String): Unit = statusBar.setText(status)
      override def onAvatarLoaded(live2DView: DemoApp): Unit = {
        faceTrackingPane.enableStartButton()
        live2DView.avatarHolder.foreach { avatar =>
          expressionSelector.updateExpressionList(avatar)
          motionSelector.updateMotionTree(avatar)
        }
        live2DView.strategyHolder.foreach { strategy =>
          effectSelector.syncWithStrategy(strategy)
          motionSelector.syncWithStrategy(strategy)
        }
      }
    }
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {
    runOnOpenGLThread {
      animatorRef.get().foreach(_.stop())
    }
  }

  override def display(drawable: GLAutoDrawable): Unit = {
    runOnOpenGLThread {
      mDemoAppHolderRef.get().foreach(_.display())
    }
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
    runOnOpenGLThread {
      mDemoAppHolderRef.get().foreach(_.resize())
    }
  }

  // Optimized event handlers
  override def mouseDragged(e: MouseEvent): Unit = {
    runOnOpenGLThread {
      if (SwingUtilities.isLeftMouseButton(e)) {
        mDemoAppHolderRef.get().foreach(_.onMouseDragged(e.getX, e.getY))
      }
      if (SwingUtilities.isRightMouseButton(e)) {
        val offsetX = lastMouseX.get.map(e.getX - _).getOrElse(0).toFloat * 0.002f
        val offsetY = lastMouseY.get.map(_ - e.getY).getOrElse(0).toFloat * 0.002f

        mDemoAppHolderRef.get().foreach(_.move(offsetX, offsetY))

        lastMouseX.set(Some(e.getX))
        lastMouseY.set(Some(e.getY))
      }
    }
  }
  override def mouseMoved(mouseEvent: MouseEvent): Unit = {
    runOnOpenGLThread {
      mDemoAppHolderRef.get().foreach(_.onMouseMoved(mouseEvent.getX, mouseEvent.getY))
    }
  }

  override def mouseReleased(mouseEvent: MouseEvent): Unit = {
    runOnOpenGLThread {
      mDemoAppHolderRef.get().foreach(_.onMouseReleased(mouseEvent.getX, mouseEvent.getY))
      lastMouseX.set(None)
      lastMouseY.set(None)
    }
  }

  override def keyTyped(keyEvent: KeyEvent): Unit = {}
  override def keyPressed(keyEvent: KeyEvent): Unit = {}
  override def keyReleased(keyEvent: KeyEvent): Unit = {
    runOnOpenGLThread {
      mDemoAppHolderRef.get().foreach(_.keyReleased(keyEvent.getKeyChar))
    }
  }
  override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
    runOnOpenGLThread {
      mDemoAppHolderRef.get().foreach(_.zoom(e.getScrollAmount * -e.getWheelRotation * 0.01f))
    }
  }

}
