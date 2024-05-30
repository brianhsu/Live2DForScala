package moe.brianhsu.live2d.demo.swing.widget.faceTracking

import moe.brianhsu.live2d.demo.openSeeFace.{CameraListing, OpenSeeFaceSetting}

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing._
import scala.annotation.unused

class SwingOpenSeeFaceBundle(cameraListing: CameraListing) extends JPanel with OpenSeeFaceSetting {

  this.setLayout(new GridBagLayout)
  this.setBorder(BorderFactory.createTitledBorder("OpenSeeFace Settings"))

  private val cameraCombo = createComboField(this, 0, "Camera:", cameraListing.listing.map(_.title), 0, "Select camera for face tracking")
  private val fpsCombo = createComboField(
    this, 1, "FPS:", List("24", "30", "60", "120", "150"), 1,
    "Set camera frames per second"
  )

  private val resolutionCombo = createComboField(
    this, 2, "Resolution:", List("320x240", "640x480", "1024x768", "1280x720", "1920x1080"), 1,
    "Set camera resolution"
  )

  private val modelCombo = createComboField(
    this, 3, "Model:", List("-3", "-2", "-1", "0", "1", "2", "3", "4"), 6,
    "This can be used to select the tracking model.\n" +
      "Higher numbers are models with better tracking quality, but slower speed, " +
      "except for model 4, which is wink optimized.\n" +
      "Models 1 and 0 tend to be too rigid for expression and blink detection.\n" +
      "Model -2 is roughly equivalent to model 1, but faster.\n " +
      "Model -3 is between models 0 and -1."
  )
  private val visualizeCombo = createComboField(
    this, 4, "Visualize:", List("0", "1", "2", "3", "4"), 0,
    "Set this to 1 to visualize the tracking, to 2 to also show face ids, " +
      "to 3 to add confidence values or to 4 to add numbers to the point display."
  )

  @unused private val placeHolder = createPlaceHolder(this, 5)

  private def createPlaceHolder(parent: JPanel, row: Int): JPanel = {
    val placeHolder = new JPanel()

    val gc = new GridBagConstraints()
    gc.gridx = 0
    gc.gridy = row
    gc.anchor = GridBagConstraints.NORTHWEST
    gc.fill = GridBagConstraints.VERTICAL
    gc.weighty = 1
    parent.add(placeHolder, gc)

    placeHolder
  }

  private def createComboField(parent: JPanel, row: Int, title: String, values: List[String], defaultIndex: Int, tooltip: String): JComboBox[String] = {
    val label = new JLabel(title)
    val comboBox = new JComboBox[String]()

    values.foreach(comboBox.addItem)
    comboBox.setToolTipText(tooltip)

    val isDefaultIndexValid = comboBox.getItemCount > defaultIndex

    if (values.nonEmpty && isDefaultIndexValid) {
      comboBox.setSelectedIndex(defaultIndex)
    }

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = row
    gc1.anchor = GridBagConstraints.NORTHWEST
    parent.add(label, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 1
    gc2.gridy = row
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.weightx = 1
    parent.add(comboBox, gc2)

    comboBox

  }

  override def getCommand: String = {
    s"${OpenSeeFaceSetting.bundleExecution} " +
      s"--model-dir ${OpenSeeFaceSetting.bundleModelDir} -M " +
      cameraIdSetting +
      cameraResolutionSetting +
      Option(fpsCombo.getSelectedItem.toString).filter(_.nonEmpty).map("--fps " + _ + " ").getOrElse("") +
      Option(modelCombo.getSelectedItem.toString).filter(_.nonEmpty).map("--model " + _ + " ").getOrElse("") +
      Option(visualizeCombo.getSelectedItem.toString).filter(_.nonEmpty).map("--visualize " + _ + " ").getOrElse("")
  }

  private def cameraResolutionSetting: String = {
    val Array(width, height) = resolutionCombo.getSelectedItem.toString.split("x")
    s"--width $width --height $height "
  }

  private def cameraIdSetting: String = {
    val cameraId = cameraListing.listing
      .find(_.title == cameraCombo.getSelectedItem.toString)
      .map(_.cameraId)
      .getOrElse(0)

    s"--capture $cameraId "
  }

  override def getHostname: String = "127.0.0.1"

  override def getPort: Int = 11573
}
