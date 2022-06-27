package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.demo.swing.Live2DWidget

import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing._
import scala.annotation.unused

class Toolbar(live2DWidget: Live2DWidget) extends JToolBar("Live 2D For Scala Swing Toolbar") {

  private val loadAvatar = new JButton("Load Avatar")
  private val pureBackground = new JButton("Pure Color Background")
  private val defaultBackground = new JButton("Default Background")
  private val selectBackground = new JButton("Select Background Image")

  {
    this.loadAvatar.addActionListener(loadAvatarAction)
    this.defaultBackground.addActionListener(switchToDefaultBackground)
    this.pureBackground.addActionListener(switchToPureColor)
    this.selectBackground.addActionListener(changeBackground)
    this.add(loadAvatar)
    this.add(defaultBackground)
    this.add(selectBackground)
    this.add(pureBackground)
  }

  private def changeBackground(@unused actionEvent: ActionEvent): Unit = {

    val jpgFilter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg")
    val pngFilter = new FileNameExtensionFilter("PNG file", "png")
    val fileChooser = new JFileChooser()
    fileChooser.setFileFilter(pngFilter)
    fileChooser.addChoosableFileFilter(jpgFilter)
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)

    val result = fileChooser.showOpenDialog(this.getParent)
    if (result == JFileChooser.APPROVE_OPTION) {
      val filePath = fileChooser.getSelectedFile.getAbsolutePath
      if (filePath.toLowerCase.endsWith("png") ||
          filePath.toLowerCase.endsWith("jpg") ||
          filePath.toLowerCase.endsWith("jpeg")) {

        for {
          live2D <- live2DWidget.demoAppHolder
          exception <- live2D.changeBackground(filePath).failed
        } {
          exception.printStackTrace()
          showErrorMessage("Cannot load background", "File format not supported.")
        }
      } else {
        showErrorMessage("Cannot load background", "File format not supported.")
      }
    }


  }

  private def switchToDefaultBackground(@unused actionEvent: ActionEvent): Unit = {
    live2DWidget.demoAppHolder.foreach(_.switchToDefaultBackground())
  }

  private def switchToPureColor(@unused actionEvent: ActionEvent): Unit = {
    for {
      live2d <- live2DWidget.demoAppHolder
      selectedColor <- Option(JColorChooser.showDialog(this.getParent, "Choose Background", new Color(0.0f, 1.0f, 0.0f)))
    } {
      live2d.switchToPureColorBackground(selectedColor)
    }
  }

  private def loadAvatarAction(@unused action: ActionEvent): Unit = {
    val fileChooser = new JFileChooser()
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    val result = fileChooser.showOpenDialog(this.getParent)
    if (result == JFileChooser.APPROVE_OPTION) {
      live2DWidget.demoAppHolder.foreach { view =>
        val avatarHolder = view.switchAvatar(fileChooser.getSelectedFile.getAbsolutePath)
        avatarHolder.failed.foreach(e => showErrorMessage("Cannot load avatar", e.getMessage))
      }
    }
  }

  private def showErrorMessage(title: String, message: String): Unit = {
    JOptionPane.showMessageDialog(this.getParent, message, title, JOptionPane.ERROR_MESSAGE)
  }

}
