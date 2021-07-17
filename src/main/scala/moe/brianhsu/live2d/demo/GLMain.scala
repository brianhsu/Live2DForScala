package moe.brianhsu.live2d.demo

import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import com.jogamp.opengl.util.Animator

import java.awt.event.{KeyEvent, KeyListener, MouseAdapter, MouseEvent, MouseWheelEvent}


class GLMain extends MouseAdapter with GLEventListener with KeyListener {

  private var animator: Option[Animator] = None
  private var view: Option[LAppView] = None

  override def init(drawable: GLAutoDrawable): Unit = {
    this.view = Option(new LAppView(drawable))
    this.animator = Option(new Animator(drawable))
    this.animator.forall(_.start())
  }

  override def dispose(drawable: GLAutoDrawable): Unit = {
    this.animator.forall(_.stop())
  }

  override def display(drawable: GLAutoDrawable): Unit = {
    this.view.foreach(_.display())
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
