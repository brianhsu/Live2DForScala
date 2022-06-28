package moe.brianhsu.live2d.demo.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLProfile}

import java.awt.{BorderLayout, GridBagConstraints, GridBagLayout}
import javax.swing._

object SwingMain {
  private val frame = new JFrame("Live 2D Scala Demo (Swing+JOGL)")
  private val live2DWidget = createGLCanvas()

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")


    frame.setSize(1080, 720)
    frame.setVisible(true)
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.getContentPane.add(BorderLayout.PAGE_START, live2DWidget.toolbar)
    frame.getContentPane.add(BorderLayout.LINE_START, createLeftPane())
    frame.getContentPane.add(BorderLayout.CENTER, live2DWidget.canvas)
    frame.getContentPane.add(BorderLayout.PAGE_END, live2DWidget.statusBar)

    frame.setVisible(true)
  }

  private def createLeftPane() = {
    val panel = new JPanel()
    panel.setBorder(BorderFactory.createTitledBorder("Avatar Control"))
    panel.setLayout(new GridBagLayout)

    val gc = new GridBagConstraints()
    gc.gridx = 0
    gc.gridy = 0
    gc.weighty = 0.1
    gc.anchor = GridBagConstraints.NORTHWEST
    gc.fill = GridBagConstraints.HORIZONTAL
    panel.add(live2DWidget.effectSelector, gc)

    gc.gridx = 0
    gc.gridy = 1
    gc.weightx = 0
    gc.weighty = 2
    gc.anchor = GridBagConstraints.NORTHWEST
    gc.fill = GridBagConstraints.BOTH
    panel.add(live2DWidget.motionSelector, gc)

    gc.gridx = 0
    gc.gridy = 2
    gc.weightx = 0
    gc.weighty = 2
    gc.fill = GridBagConstraints.BOTH
    panel.add(live2DWidget.expressionSelector, gc)

    panel
  }


  private def createGLCanvas(): Live2DUI = {
    val profile = GLProfile.get(GLProfile.GL2)
    val capabilities = new GLCapabilities(profile)

    val canvas = new GLCanvas(capabilities)
    val live2DUI = new Live2DUI(canvas)

    canvas.addGLEventListener(live2DUI)
    canvas.addMouseListener(live2DUI)
    canvas.addMouseMotionListener(live2DUI)
    canvas.addMouseWheelListener(live2DUI)
    canvas.addKeyListener(live2DUI)

    live2DUI
  }
}
