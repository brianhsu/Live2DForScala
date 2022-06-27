package moe.brianhsu.porting.live2d.swing

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.{GLCapabilities, GLProfile}
import moe.brianhsu.live2d.adapter.gateway.avatar.AvatarFileReader

import java.awt.TrayIcon.MessageType
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{BorderLayout, Color, Component}
import javax.swing.{JButton, JDialog, JFileChooser, JFrame, JOptionPane}

object SwingWithJavaOpenGL {
  private val frame = new JFrame("Live 2D Scala Demo")
  private val live2DWidget = createGLCanvas()

  def main(args: Array[String]): Unit = {

    System.setProperty("sun.awt.noerasebackground", "true")


    frame.setSize(1080, 720)
    frame.setVisible(true)
    frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    frame.getContentPane.add(BorderLayout.NORTH, createLoadButton(frame))
    frame.getContentPane.add(BorderLayout.CENTER, live2DWidget.canvas)

    frame.setVisible(true)

  }

  private def createLoadButton(parent: Component): JButton = {
    val button = new JButton("Load Avatar")
    button.addActionListener { actionEvent: ActionEvent =>
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
    val widget = new Live2DWidget(canvas)

    canvas.addGLEventListener(widget)
    canvas.addMouseListener(widget)
    canvas.addMouseMotionListener(widget)
    canvas.addMouseWheelListener(widget)
    canvas.addKeyListener(widget)

    widget
  }
}
