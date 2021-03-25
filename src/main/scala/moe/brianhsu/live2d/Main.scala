package moe.brianhsu.live2d

import com.jogamp.opengl.{GL, GL2, GLAutoDrawable, GLCapabilities, GLEventListener, GLProfile}
import com.jogamp.opengl.awt.GLCanvas
import moe.brianhsu.live2d.demo.{FrameTime, GLMain, LAppSprite}
import com.jogamp.opengl.GL._
import com.jogamp.opengl.util.Animator

import java.awt.event.{MouseAdapter, MouseEvent, MouseListener}
import javax.swing.JFrame


object Main {

  def main(args: Array[String]): Unit = {
    val frame = new JFrame("Sample")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(1840, 1000)
    frame.getContentPane.add(createGLCanvas())
    frame.setVisible(true)
  }

  private def createGLCanvas(): GLCanvas = {
    val profile = GLProfile.get(GLProfile.GL2)
    val capabilities = new GLCapabilities(profile)

    val canvas = new GLCanvas(capabilities)
    val glMain = new GLMain
    canvas.addGLEventListener(glMain)
    canvas.addMouseListener(glMain)
    canvas
  }
}
