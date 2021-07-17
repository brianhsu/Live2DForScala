package moe.brianhsu.live2d.demo

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLDrawableFactory, GLProfile}

import java.awt.Color
import java.awt.event.{MouseEvent, MouseMotionListener}
import javax.swing.JFrame

object Main {

  // TODO:
  // This should be fixed.
  var frame: JFrame = null

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")

    this.frame = new JFrame("Sample")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(1024, 768)
    frame.getContentPane.add(createGLCanvas())
    frame.setVisible(true)

    /*
    val d = FaceDirection
    d.set(-0.0205007792f, 0.5387520790f);
    d.update(0.0173173174f);
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(-0.0205007792f, 0.5387520790f)
    d.update(0.0165990293f);
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(-0.0205007792f, 0.5387520790f)
    d.update(0.0153490268f)
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(-0.0205007792f, 0.5387520790f)
    d.update(0.0170495827f)
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(0, 0);
    d.update(0.0182148647f);
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(0, 0);
    d.update(0.0135475658f)
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(0, 0)
    d.update(0.0177046135f)
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

    d.set(0, 0)
    d.update(0.0158471335f)
    printf("GetX: %.10f, GetY:%.10f\n", d.getX, d.getY)

     */

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
