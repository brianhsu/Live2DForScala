package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.demo.openSeeFace.{CameraListing, ExternalOpenSeeFaceDataReader}
import moe.brianhsu.live2d.demo.swing.Live2DUI
import moe.brianhsu.live2d.demo.swing.widget.faceTracking.{SwingOpenSeeFaceAdvance, SwingOpenSeeFaceBundle, SwingOpenSeeFaceSetting}
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData
import moe.brianhsu.live2d.enitiy.openSeeFace.OpenSeeFaceData.Point

import java.awt.event.ActionEvent
import java.awt.{BasicStroke, CardLayout, Color, Graphics, Graphics2D, GridBagConstraints, GridBagLayout}
import java.util.concurrent.{Callable, ScheduledThreadPoolExecutor, TimeUnit}
import javax.swing._
import scala.annotation.unused
import scala.util.Try

class SwingFaceTrackingPane(live2DWidget: Live2DUI) extends JPanel {
  private var openSeeFaceDataHolder: Option[OpenSeeFaceData] = None
  private var openSeeFaceReaderHolder: Option[ExternalOpenSeeFaceDataReader] = None

  private val openSeeFaceModeLabel = new JLabel("Mode:")
  private val openSeeFaceModeCombo = new JComboBox[String](Array("Bundle", "Advanced"))
  private val openSeeFaceAdvance = new SwingOpenSeeFaceAdvance
  private val openSeeFaceBundle = new SwingOpenSeeFaceBundle(CameraListing.createByOS())
  private val cardLayout = new CardLayout
  private val openSeeFacePanel = new JPanel(cardLayout)
  private val (startButton, stopButton, buttonPanel, buttonCardLayout) = createStartStopButton()
  private val outlinePanel = new OutlinePanel
  private val executor = new ScheduledThreadPoolExecutor(1)

  {
    this.setLayout(new GridBagLayout)
    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.anchor = GridBagConstraints.NORTHWEST
    this.add(openSeeFaceModeLabel, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 1
    gc2.gridy = 0
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.weightx = 1
    this.add(openSeeFaceModeCombo)

    val gc3 = new GridBagConstraints()
    gc3.gridx = 0
    gc3.gridy = 1
    gc3.gridwidth = 2
    gc3.fill = GridBagConstraints.HORIZONTAL
    gc3.weightx = 1
    gc2.anchor = GridBagConstraints.NORTHWEST
    this.openSeeFacePanel.add(openSeeFaceBundle, "Bundle")
    this.openSeeFacePanel.add(openSeeFaceAdvance, "Advanced")
    this.add(openSeeFacePanel, gc3)

    val gc4 = new GridBagConstraints()
    gc4.gridx = 0
    gc4.gridy = 2
    gc4.gridwidth = 2
    gc4.fill = GridBagConstraints.HORIZONTAL
    gc4.weightx = 1
    gc4.anchor = GridBagConstraints.NORTHWEST
    this.add(buttonPanel, gc4)

    val gc5 = new GridBagConstraints()
    gc5.gridx = 0
    gc5.gridy = 3
    gc5.gridwidth = 2
    gc5.fill = GridBagConstraints.BOTH
    gc5.weightx = 1
    gc5.weighty = 1
    gc5.anchor = GridBagConstraints.NORTHWEST
    this.add(outlinePanel, gc5)

    this.openSeeFaceModeCombo.addActionListener(onModeSelected)
    this.startButton.addActionListener(onStartSelected)
    this.stopButton.addActionListener(onStopSelected)

  }

  def enableStartButton(): Unit = {
    this.startButton.setEnabled(true)
  }

  private def onStartSelected(@unused event: ActionEvent): Unit = {
    live2DWidget.demoAppHolder.foreach(_.disableFaceTracking())

    val settings = getOpenSeeFaceSetting
    val dataReader: Try[ExternalOpenSeeFaceDataReader] = for {
      reader <- Try(ExternalOpenSeeFaceDataReader(settings.getCommand, settings.getHostname, settings.getPort, onDataRead))
      startedReader <- reader.ensureStarted()
    } yield {
      startedReader
    }

    this.openSeeFaceReaderHolder = dataReader.toOption

    dataReader.failed.foreach { e =>
      JOptionPane.showMessageDialog(this, e.getMessage, "Failed to start OpenSeeFace", JOptionPane.ERROR_MESSAGE)
    }

    for {
      demoApp <- live2DWidget.demoAppHolder
      reader <- dataReader
    } {
      demoApp.enableFaceTracking(reader)
      this.startButton.setEnabled(false)
      this.stopButton.setEnabled(true)
      this.buttonCardLayout.show(buttonPanel, "Stop")
    }

    this.outlinePanel.update(this.outlinePanel.getGraphics)

  }

  private def onStopSelected(@unused event: ActionEvent): Unit = {
    this.live2DWidget.demoAppHolder.foreach(_.disableFaceTracking())
    this.openSeeFaceReaderHolder = None

    this.openSeeFaceDataHolder = None

    this.outlinePanel.update(this.outlinePanel.getGraphics)

    this.startButton.setEnabled(true)
    this.stopButton.setEnabled(false)
    this.buttonCardLayout.show(buttonPanel, "Start")
  }

  private def onDataRead(data: OpenSeeFaceData): Unit = {
    this.openSeeFaceDataHolder = Some(data)
    SwingUtilities.invokeLater { () =>
      this.outlinePanel.update(this.outlinePanel.getGraphics)
    }
  }

  private def getOpenSeeFaceSetting: SwingOpenSeeFaceSetting = {
    this.openSeeFaceModeCombo.getSelectedIndex match {
      case 0 => openSeeFaceBundle
      case 1 => openSeeFaceAdvance
    }
  }

  private class OutlinePanel extends JPanel {

    this.setBorder(BorderFactory.createTitledBorder("Outline"))

    override def paint(g: Graphics): Unit = {
      val gc = g.asInstanceOf[Graphics2D]
      val width = this.getBounds().width
      val height = this.getBounds().width
      gc.clearRect(0, 0, width, height)

      if (openSeeFaceReaderHolder.isDefined && openSeeFaceDataHolder.isEmpty) {

        if (System.currentTimeMillis() / 500 % 2 == 0) {
          gc.setColor(new Color(255, 0, 0))
          gc.fillOval(10, 10, 20, 20)
        }


        executor.schedule(new Callable[Unit] {
          override def call(): Unit = {
            SwingUtilities.invokeLater { () =>
              if (OutlinePanel.this.isValid) {
                OutlinePanel.this.paint(OutlinePanel.this.getGraphics)
              }
            }
          }
        }, 500, TimeUnit.MILLISECONDS)
      }

      openSeeFaceDataHolder.foreach { data =>

        if (System.currentTimeMillis() / 500 % 2 == 0) {
          gc.setColor(new Color(255, 0, 0))
          gc.fillOval(10, 10, 20, 20)
        }

        gc.setColor(new Color(0, 0, 255))

        val scaleX = width / data.resolution.width
        val scaleY = height / data.resolution.height


        def convert(p: Point): List[Int] = {
          List((p.x * scaleX - (width / 4)).toInt * 2, (p.y * scaleY - (height / 3)).toInt * 2)
        }

        val (faceOutlineX, faceOutlineY) = splitXY(data.landmarks.slice(0, 17).flatMap(convert).toArray)
        val (noseVerticalX, noseVerticalY) = splitXY(data.landmarks.slice(27, 31).flatMap(convert).toArray)
        val (noseHorizontalX, noseHorizontalY) = splitXY(data.landmarks.slice(31, 36).flatMap(convert).toArray)
        val (rightEyeBrowX, rightEyeBrowY) = splitXY(data.landmarks.slice(17, 22).flatMap(convert).toArray)
        val (leftEyeBrowX, leftEyeBrowY) = splitXY(data.landmarks.slice(22, 27).flatMap(convert).toArray)
        val (rightEyeX, rightEyeY) = splitXY(data.landmarks.slice(36, 42).appended(data.landmarks(36)).flatMap(convert).toArray)
        val (leftEyeX, leftEyeY) = splitXY(data.landmarks.slice(42, 48).appended(data.landmarks(42)).flatMap(convert).toArray)
        val (mouthX, mouthY) = splitXY(data.landmarks.slice(48, 59).appended(data.landmarks(48)).flatMap(convert).toArray)
        val (upperLipX, upperLipY) = splitXY(data.landmarks.slice(59, 62).flatMap(convert).toArray)
        val (lowerLipX, lowerLipY) = splitXY(data.landmarks.slice(63, 66).flatMap(convert).toArray)

        gc.setStroke(new BasicStroke(2))

        gc.drawPolyline(faceOutlineX, faceOutlineY, faceOutlineX.length)
        gc.drawPolyline(noseVerticalX, noseVerticalY, noseVerticalX.length)
        gc.drawPolyline(noseHorizontalX, noseHorizontalY, noseHorizontalX.length)
        gc.drawPolyline(rightEyeBrowX, rightEyeBrowY, rightEyeBrowX.length)
        gc.drawPolyline(leftEyeBrowX, leftEyeBrowY, leftEyeBrowX.length)
        gc.drawPolyline(rightEyeX, rightEyeY, rightEyeX.length)
        gc.drawPolyline(leftEyeX, leftEyeY, leftEyeX.length)
        gc.drawPolyline(mouthX, mouthY, mouthX.length)
        gc.drawPolyline(upperLipX, upperLipY, upperLipX.length)
        gc.drawPolyline(lowerLipX, lowerLipY, lowerLipX.length)
      }

    }

    private def splitXY(points: Array[Int]): (Array[Int], Array[Int]) = {
      val (xWithIndex, yWithIndex) = points.zipWithIndex.partition(_._2 % 2 == 0)
      (xWithIndex.map(_._1), yWithIndex.map(_._1))
    }

  }

  private def onModeSelected(@unused actionEvent: ActionEvent): Unit = {
    this.cardLayout.show(openSeeFacePanel, this.openSeeFaceModeCombo.getSelectedItem.toString)
  }

  private def createStartStopButton(): (JButton, JButton, JPanel, CardLayout) = {
    val cardLayout = new CardLayout
    val panel = new JPanel(cardLayout)

    val startButton = new JButton("Start")
    val stopButton = new JButton("Stop")

    stopButton.setEnabled(false)
    startButton.setEnabled(false)
    panel.add(startButton, "Start")
    panel.add(stopButton, "Stop")

    (startButton, stopButton, panel, cardLayout)
  }

}
