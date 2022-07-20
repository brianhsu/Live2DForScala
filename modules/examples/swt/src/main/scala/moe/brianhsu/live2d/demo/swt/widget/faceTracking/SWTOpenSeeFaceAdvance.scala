package moe.brianhsu.live2d.demo.swt.widget.faceTracking

import moe.brianhsu.live2d.demo.openSeeFace.OpenSeeFaceSetting
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets._

import scala.annotation.unused

class SWTOpenSeeFaceAdvance(parent: Composite) extends Composite(parent, SWT.NONE) with OpenSeeFaceSetting {

  private val commandText = createTextField(this, "Command:", "python /home/brianhsu/WorkRoom/OpenSeeFace/facetracker.py")
  private val ipText = createTextField(this,"IP:", "127.0.0.1", "Set IP address for sending tracking data")
  private val portText = createTextField(this,"Port:", "11573", "Set port for sending tracking data")
  private val cameraIdText = createTextField(this,"Camera ID:", "0", "Set camera ID (0, 1...)")
  private val modelCombo = createComboField(
    this, "Model:", Array("-3", "-2", "-1", "0", "1", "2", "3", "4"), 6,
    "This can be used to select the tracking model.\n" +
      "Higher numbers are models with better tracking quality, but slower speed, " +
      "except for model 4, which is wink optimized.\n" +
      "Models 1 and 0 tend to be too rigid for expression and blink detection.\n" +
      "Model -2 is roughly equivalent to model 1, but faster.\n " +
      "Model -3 is between models 0 and -1."
  )
  private val visualizeCombo = createComboField(
    this, "Visualize:", Array("0", "1", "2", "3", "4"), 0,
    "Set this to 1 to visualize the tracking, to 2 to also show face ids, " +
      "to 3 to add confidence values or to 4 to add numbers to the point display."
  )
  private val extraParameterText = createTextField(this, "Extra Parameters:", "--width 640 --height 480 --fps 30")
  private val mirrorCheckbox = createCheckbox(this, "Mirror Input", "Process a mirror image of the input video")
  private val commandPreviewText = createCommandPreview(this, "Command Preview:")

  {
    this.setLayout(new GridLayout(2, true))

    commandText.addListener(SWT.Modify, updateSegments)
    ipText.addListener(SWT.Modify, updateSegments)
    portText.addListener(SWT.Modify, updateSegments)
    cameraIdText.addListener(SWT.Modify, updateSegments)
    modelCombo.addListener(SWT.Selection, updateSegments)
    visualizeCombo.addListener(SWT.Selection, updateSegments)
    extraParameterText.addListener(SWT.Modify, updateSegments)
    mirrorCheckbox.addListener(SWT.Selection, updateSegments)

    updateCommandPreview()
  }

  override def getCommand: String = commandPreviewText.getText

  private def updateSegments(@unused event: Event): Unit = {
    updateCommandPreview()
  }

  private def updateCommandPreview(): Unit = {
    val command =
      commandText.getText + " " +
      Option(ipText.getText).filter(_.nonEmpty).map("--ip " + _ + " ").getOrElse("") +
      Option(portText.getText).filter(_.nonEmpty).map("--port " + _ + " ").getOrElse("") +
      Option(cameraIdText.getText).filter(_.nonEmpty).map("--capture " + _ + " ").getOrElse("") +
      Option(modelCombo.getText).filter(_.nonEmpty).map("--model " + _ + " ").getOrElse("") +
      Option(visualizeCombo.getText).filter(_.nonEmpty).map("--visualize " + _ + " ").getOrElse("") +
      (if (mirrorCheckbox.getSelection) { "--mirror-input " } else {""}) +
      Option(extraParameterText.getText).filter(_.nonEmpty).map(_ + " ").getOrElse("")

    commandPreviewText.setText(command)
  }

  private def createCommandPreview(parent: Composite, title: String): Text = {
    val titleLabel = new Label(parent, SWT.NONE)
    val previewText = new Text(parent, SWT.BORDER|SWT.WRAP|SWT.MULTI|SWT.V_SCROLL|SWT.READ_ONLY)

    val gridData = new GridData
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    gridData.horizontalSpan = 2
    titleLabel.setText(title)
    titleLabel.setLayoutData(gridData)

    val gd = new GridData()
    gd.horizontalAlignment = GridData.FILL
    gd.grabExcessHorizontalSpace = true
    gd.horizontalSpan = 2
    gd.heightHint = previewText.getLineHeight * 3

    previewText.setLayoutData(gd)

    previewText
  }

  private def createCheckbox(parent: Composite, title: String, tooltip: String): Button = {
    val checkbox = new Button(parent, SWT.CHECK)

    checkbox.setText(title)
    checkbox.setToolTipText(tooltip)
    checkbox.setSelection(true)

    val gridData = new GridData
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    gridData.horizontalSpan = 2
    checkbox.setLayoutData(gridData)

    checkbox
  }

  private def createComboField(parent: Composite, title: String, values: Array[String], defaultIndex: Int, tooltip:String): Combo = {
    val titleLabel = new Label(parent, SWT.NONE)
    val comboBox = new Combo(parent, SWT.DROP_DOWN|SWT.READ_ONLY)

    titleLabel.setText(title)
    comboBox.setItems(values: _*)
    comboBox.select(defaultIndex)
    comboBox.setToolTipText(tooltip)

    val gridData = new GridData
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    comboBox.setLayoutData(gridData)

    comboBox
  }

  private def createTextField(parent: Composite, title: String, default: String, tooltip: String = ""): Text = {
    val titleLabel = new Label(parent, SWT.NONE)
    val textField = new Text(parent, SWT.BORDER | SWT.SINGLE)

    titleLabel.setText(title)
    textField.setText(default)
    textField.setToolTipText(tooltip)

    val gridData = new GridData
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    textField.setLayoutData(gridData)

    textField
  }

  override def getHostname: String = ipText.getText

  override def getPort: Int = portText.getText.toInt
}
