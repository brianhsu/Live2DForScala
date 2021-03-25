package moe.brianhsu.live2d.demo

import com.jogamp.opengl.{GLAutoDrawable, GLEventListener}
import com.jogamp.opengl.util.Animator

import java.awt.event.{MouseAdapter, MouseEvent}


class GLMain extends MouseAdapter with GLEventListener{

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

  override def mouseClicked(e: MouseEvent): Unit = {
    this.view.foreach(_.onMouseClick(e.getX, e.getY))
  }
}
