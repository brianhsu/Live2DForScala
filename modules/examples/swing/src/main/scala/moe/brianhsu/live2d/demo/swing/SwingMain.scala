package moe.brianhsu.live2d.demo.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLProfile}

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing._

object SwingMain {
  private val frame = new JFrame("Live 2D Scala Demo (Swing+JOGL)")
  private val live2DWidget = createGLCanvas()

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")


    frame.setSize(1080, 720)
    frame.setVisible(true)
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)

    frame.getContentPane.setLayout(new GridBagLayout)

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.gridwidth = 2
    gc1.fill = GridBagConstraints.HORIZONTAL
    frame.getContentPane.add(live2DWidget.toolbar, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 0
    gc2.gridy = 1
    gc2.gridwidth = 1
    gc2.fill = GridBagConstraints.VERTICAL
    frame.getContentPane.add(createLeftPane(), gc2)

    val gc3 = new GridBagConstraints()
    gc3.gridx = 1
    gc3.gridy = 1
    gc3.gridwidth = 1
    gc3.weightx = 1
    gc3.weighty = 1
    gc3.fill = GridBagConstraints.BOTH
    frame.getContentPane.add(live2DWidget.canvas, gc3)

    val gc4 = new GridBagConstraints()
    gc4.gridx = 0
    gc4.gridy = 2
    gc4.gridwidth = 2
    gc4.weightx = 0
    gc4.weighty = 0
    gc4.fill = GridBagConstraints.HORIZONTAL
    frame.getContentPane.add(live2DWidget.statusBar, gc4)


    frame.setVisible(true)
  }

  private def createLeftPane() = {
    val panel = new JPanel()
    panel.setBorder(BorderFactory.createTitledBorder("Avatar Control"))
    panel.setLayout(new GridBagLayout)

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.fill = GridBagConstraints.BOTH
    gc1.anchor = GridBagConstraints.NORTHWEST
    panel.add(live2DWidget.effectSelector, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 0
    gc2.gridy = 1
    gc2.weightx = 1
    gc2.weighty = 1
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.fill = GridBagConstraints.BOTH
    panel.add(live2DWidget.motionSelector, gc2)
    
    val gc3 = new GridBagConstraints()
    gc3.gridx = 0
    gc3.gridy = 2
    gc3.weightx = 0
    gc3.weighty = 1
    gc3.fill = GridBagConstraints.BOTH
    panel.add(live2DWidget.expressionSelector, gc3)


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
