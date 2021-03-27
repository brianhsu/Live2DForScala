package moe.brianhsu.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLDrawableFactory, GLProfile}

import java.awt.Color
import java.awt.event.{MouseEvent, MouseMotionListener}
import javax.swing.JFrame

object Main {

  def main(args: Array[String]): Unit = {
    System.setProperty("sun.awt.noerasebackground", "true")

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
    canvas.addMouseMotionListener(glMain)
    canvas.addKeyListener(glMain)
    canvas.setBackground(new Color(1, 0, 0, 0))
    canvas
  }
}
