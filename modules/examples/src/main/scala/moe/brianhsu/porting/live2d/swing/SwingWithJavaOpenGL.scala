package moe.brianhsu.porting.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLProfile}
import moe.brianhsu.porting.live2d.swing.Live2DWidget

import java.awt.{BorderLayout, Color}
import javax.swing.{JButton, JFrame}

object Main {

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")

    val frame = new JFrame("Live 2D Scala Demo")

    frame.setSize(1080, 720)
    frame.setVisible(true)
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.getContentPane.add(BorderLayout.NORTH, new JButton("Hello"))
    frame.getContentPane.add(BorderLayout.CENTER, createGLCanvas())

    frame.setVisible(true)

  }

  private def createGLCanvas(): GLCanvas = {
    val profile = GLProfile.get(GLProfile.GL2)
    val capabilities = new GLCapabilities(profile)

    val canvas = new GLCanvas(capabilities)
    val glMain = new Live2DWidget(canvas)

    canvas.addGLEventListener(glMain)
    canvas.addMouseListener(glMain)
    canvas.addMouseMotionListener(glMain)
    canvas.addMouseWheelListener(glMain)
    canvas.addKeyListener(glMain)
    canvas.setBackground(new Color(1, 0, 0, 0))
    canvas
  }
}
