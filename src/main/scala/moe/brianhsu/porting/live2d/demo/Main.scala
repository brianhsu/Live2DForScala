package moe.brianhsu.porting.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLProfile}

import java.awt.Color
import javax.swing.JFrame

object Main {

  // TODO:
  // This should be fixed.
  var frame: JFrame = null

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")

    this.frame = new JFrame("Sample")
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(1920, 1080)
    frame.getContentPane.add(createGLCanvas())
    frame.setVisible(true)


  }

  private def createGLCanvas(): GLCanvas = {
    val profile = GLProfile.get(GLProfile.GL2)
    val capabilities = new GLCapabilities(profile)

    val canvas = new GLCanvas(capabilities)
    val glMain = new GLMain(canvas)

    canvas.addGLEventListener(glMain)
    canvas.addMouseListener(glMain)
    canvas.addMouseMotionListener(glMain)
    canvas.addKeyListener(glMain)
    canvas.setBackground(new Color(1, 0, 0, 0))
    canvas
  }
}
