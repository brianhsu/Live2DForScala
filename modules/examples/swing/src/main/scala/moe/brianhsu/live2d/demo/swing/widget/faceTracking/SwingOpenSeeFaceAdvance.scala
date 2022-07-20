package moe.brianhsu.live2d.demo.swing.widget.faceTracking

import moe.brianhsu.live2d.demo.openSeeFace.OpenSeeFaceSetting

import java.awt.event.ActionEvent
import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing._
import javax.swing.event.{DocumentEvent, DocumentListener}
import scala.annotation.unused

class SwingOpenSeeFaceAdvance extends JPanel with OpenSeeFaceSetting  {

  this.setLayout(new GridBagLayout)
  this.setBorder(BorderFactory.createTitledBorder("OpenSeeFace Settings"))

  private val commandText = createTextField(this, 0, "Command:", "python facetracker.py")
  private val ipText = createTextField(this, 1, "IP:", "127.0.0.1", "Set IP address for sending tracking data")
  private val portText = createTextField(this, 2, "Port:", "11573", "Set port for sending tracking data")
  private val cameraIdText = createTextField(this, 3, "Camera ID:", "0", "Set camera ID (0, 1...)")
  private val modelCombo = createComboField(
    this, 4, "Model:", List("-3", "-2", "-1", "0", "1", "2", "3", "4"), 6,
    "<html>This can be used to select the tracking model.<br>\n" +
      "Higher numbers are models with better tracking quality, but slower speed, <br>" +
      "except for model 4, which is wink optimized.\n<br>" +
      "Models 1 and 0 tend to be too rigid for expression and blink detection.\n<br>" +
      "Model -2 is roughly equivalent to model 1, but faster.\n <br>" +
      "Model -3 is between models 0 and -1.<br></html>"
  )
  private val visualizeCombo = createComboField(
    this, 5, "Visualize:", List("0", "1", "2", "3", "4"), 0,
    "Set this to 1 to visualize the tracking, to 2 to also show face ids, " +
      "to 3 to add confidence values or to 4 to add numbers to the point display."
  )
  private val extraParameterText = createTextField(this, 6, "Extra Parameters:", "--width 640 --height 480 --fps 30")
  private val mirrorCheckbox = createCheckbox(this, 7, "Mirror Input", "Process a mirror image of the input video")
  private val commandPreviewText = createCommandPreview(this, 8, "Command Preview:")

  {
    addUpdateListener(commandText, updateCommandPreview)
    addUpdateListener(ipText, updateCommandPreview)
    addUpdateListener(portText, updateCommandPreview)
    addUpdateListener(cameraIdText, updateCommandPreview)
    addUpdateListener(extraParameterText, updateCommandPreview)
    modelCombo.addActionListener(updateCommandPreview)
    visualizeCombo.addActionListener(updateCommandPreview)
    mirrorCheckbox.addActionListener(updateCommandPreview)
    mirrorCheckbox.setSelected(true)

    updateCommandPreview()
  }

  private def addUpdateListener(textField: JTextField, callback: () => Unit): Unit = {
    textField.getDocument.addDocumentListener(new DocumentListener {
      override def insertUpdate(documentEvent: DocumentEvent): Unit = callback()
      override def removeUpdate(documentEvent: DocumentEvent): Unit = callback()
      override def changedUpdate(documentEvent: DocumentEvent): Unit = callback()
    })
  }

  private def updateCommandPreview(@unused actionEvent: ActionEvent): Unit = {
    updateCommandPreview()
  }

  private def updateCommandPreview(): Unit = {
    val command =
      commandText.getText + " " +
        Option(ipText.getText).filter(_.nonEmpty).map("--ip " + _ + " ").getOrElse("") +
        Option(portText.getText).filter(_.nonEmpty).map("--port " + _ + " ").getOrElse("") +
        Option(cameraIdText.getText).filter(_.nonEmpty).map("--capture " + _ + " ").getOrElse("") +
        Option(modelCombo.getSelectedItem.toString).filter(_.nonEmpty).map("--model " + _ + " ").getOrElse("") +
        Option(visualizeCombo.getSelectedItem.toString).filter(_.nonEmpty).map("--visualize " + _ + " ").getOrElse("") +
        (if (mirrorCheckbox.isSelected) { "--mirror-input " } else {""}) +
        Option(extraParameterText.getText).filter(_.nonEmpty).map(_ + " ").getOrElse("")

    commandPreviewText.setText(command)
  }

  private def createTextField(parent: JPanel, row: Int, title: String, default: String, tooltip: String = "") = {
    val label = new JLabel(title)
    val textField = new JTextField(default)

    textField.setToolTipText(tooltip)

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
    parent.add(textField, gc2)

    textField
  }

  private def createComboField(parent: JPanel, row: Int, title: String, values: List[String], defaultIndex: Int, tooltip: String): JComboBox[String] = {
    val label = new JLabel(title)
    val comboBox = new JComboBox[String]()

    values.foreach(comboBox.addItem)
    comboBox.setToolTipText(tooltip)
    comboBox.setSelectedIndex(defaultIndex)

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

  private def createCheckbox(parent: JPanel, row: Int, title: String, tooltip: String): JCheckBox = {
    val checkBox = new JCheckBox()

    checkBox.setText(title)
    checkBox.setToolTipText(tooltip)

    val gc = new GridBagConstraints()
    gc.gridx = 0
    gc.gridy = row
    gc.anchor = GridBagConstraints.NORTHWEST
    gc.fill = GridBagConstraints.HORIZONTAL
    gc.weightx = 1
    parent.add(checkBox, gc)

    checkBox
  }

  private def createCommandPreview(parent: JPanel, row: Int, title: String): JTextArea = {

    val label = new JLabel(title)
    val textArea = new JTextArea()
    val scrollPane = new JScrollPane(textArea)
    textArea.setRows(3)
    textArea.setColumns(20)
    textArea.setEditable(false)
    textArea.setLineWrap(true)

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = row
    gc1.anchor = GridBagConstraints.NORTHWEST
    gc1.weightx = 1
    parent.add(label, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 0
    gc2.gridy = row + 1
    gc2.gridwidth = 2
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.weightx = 1
    parent.add(scrollPane, gc2)

    textArea
  }

  override def getCommand: String = commandPreviewText.getText

  override def getHostname: String = ipText.getText

  override def getPort: Int = portText.getText.toInt
}
