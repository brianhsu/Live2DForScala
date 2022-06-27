package moe.brianhsu.porting.live2d.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLProfile}

import java.awt.event.ActionEvent
import java.awt.{BorderLayout, Component, Dimension, GridBagLayout, GridLayout}
import javax.swing._

object SwingWithJavaOpenGL {
  private val frame = new JFrame("Live 2D Scala Demo")
  private val live2DWidget = createGLCanvas()
  private val statusLine = createStatusLine()

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")


    frame.setSize(1080, 720)
    frame.setVisible(true)
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.getContentPane.add(BorderLayout.PAGE_START, createToolBar())
    frame.getContentPane.add(BorderLayout.LINE_START, createLeftPane())
    frame.getContentPane.add(BorderLayout.CENTER, live2DWidget.canvas)
    frame.getContentPane.add(BorderLayout.PAGE_END, statusLine)

    frame.setVisible(true)
  }

  private def createToolBar() = {
    val toolbar = new JToolBar()
    val button = new JButton("Load Avatar")
    val checkbox = new JCheckBox("Blink")
    toolbar.add(button)
    toolbar.add(checkbox)
    toolbar
  }

  private def createEffectSelector() = {
    val panel = new JPanel()
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
    val blink = new JCheckBox("Blink")
    val breath = new JCheckBox("Breath")
    val faceDirection = new JCheckBox("Face direction")
    val comboBox = new JComboBox[String](Array("Click and drag", "Follow mouse"))

    panel.setBorder(BorderFactory.createTitledBorder("Effects"))
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS))
    panel.add(blink)
    panel.add(breath)
    panel.add(faceDirection)
    panel.add(comboBox)
    val scrollPane = new JScrollPane(panel)
    scrollPane
  }

  private def createLeftPane() = {
    val panel = new JPanel()
    panel.setLayout(new GridLayout(3, 1))
    panel.add(createEffectSelector())
    panel.add(live2DWidget.motionSelector)
    panel.add(live2DWidget.expressionSelector)
    panel.setPreferredSize(new Dimension(200, 100))
    panel
  }

  private def createStatusLine(): JLabel = {
    val statusLine = new JLabel("Ready.")
    statusLine.setBorder(BorderFactory.createEtchedBorder())
    statusLine
  }

  private def createLoadButton(parent: Component): JButton = {
    val button = new JButton("Load Avatar")
    button.addActionListener { _: ActionEvent =>
      val fileChooser = new JFileChooser()
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
      fileChooser.showOpenDialog(parent) match {
        case JFileChooser.APPROVE_OPTION => loadAvatar(fileChooser.getSelectedFile.getAbsolutePath)
        case _ =>
      }
    }
    button
  }

  private def loadAvatar(directory: String): Unit = {
    live2DWidget.doWithLive2DView { view =>
      val avatarHolder = view.switchAvatar(directory)
      avatarHolder.failed.foreach(e => showErrorMessage("Cannot load avatar", e.getMessage))
    }
  }

  private def showErrorMessage(title: String, message: String): Unit = {
    JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE)
  }

  private def createGLCanvas(): Live2DWidget = {
    val profile = GLProfile.get(GLProfile.GL2)
    val capabilities = new GLCapabilities(profile)

    val canvas = new GLCanvas(capabilities)
    val widget = new Live2DWidget(
      canvas, { live2DWidget =>
        live2DWidget.doWithLive2DView { view =>
          view.setLogger { message =>
            statusLine.setText(message)
            statusLine.updateUI()
          }
        }
      }
    )

    canvas.addGLEventListener(widget)
    canvas.addMouseListener(widget)
    canvas.addMouseMotionListener(widget)
    canvas.addMouseWheelListener(widget)
    canvas.addKeyListener(widget)
    widget
  }
}
