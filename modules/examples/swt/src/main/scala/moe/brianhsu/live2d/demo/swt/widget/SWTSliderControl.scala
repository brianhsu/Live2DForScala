package moe.brianhsu.live2d.demo.swt.widget

import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets.{Composite, Event, Label, Scale}

class SWTSliderControl(title: String, min: Int, max: Int, default: Int, parent: Composite) extends Composite(parent, SWT.NONE) {

  private val titleLabel = createLabel(title)
  private val scale = createScale(min, max, default)
  private val valueLabel = createLabel(s"$default%")

  {
    val gridLayout = new GridLayout(3, false)
    gridLayout.marginHeight = 0

    this.setLayout(gridLayout)

    val titleLabelData = new GridData
    titleLabelData.horizontalAlignment = GridData.BEGINNING
    titleLabelData.verticalAlignment = GridData.CENTER
    titleLabelData.grabExcessVerticalSpace = false
    titleLabelData.grabExcessHorizontalSpace = false
    titleLabel.setLayoutData(titleLabelData)

    val scaleData = new GridData
    scaleData.horizontalAlignment = GridData.FILL
    scaleData.verticalAlignment = GridData.CENTER
    scaleData.grabExcessVerticalSpace = false
    scaleData.grabExcessHorizontalSpace = true
    scale.setLayoutData(scaleData)
    scale.addListener(SWT.Selection, { _: Event => updatePercentage(scale.getSelection) })

    val valueLabelData = new GridData
    titleLabelData.horizontalAlignment = GridData.BEGINNING
    titleLabelData.verticalAlignment = GridData.CENTER
    valueLabelData.grabExcessVerticalSpace = false
    valueLabelData.grabExcessHorizontalSpace = false
    valueLabel.setLayoutData(valueLabelData)

    // Force SWT keep enough space for value label when it reach 100%
    valueLabel.setText("100%")
    this.layout()

    valueLabel.setText(s"$default%")
  }

  override def setEnabled(isEnabled: Boolean): Unit = {
    super.setEnabled(isEnabled)
    this.scale.setEnabled(isEnabled)
  }

  def addChangeListener(callback: Int => Unit): Unit = {
    this.scale.addListener(SWT.Selection, (_: Event) => callback(scale.getSelection))
  }

  def percentage: Int = scale.getSelection

  def updatePercentage(value: Int): Unit = {
    scale.setSelection(value)
    valueLabel.setText(s"$value%")
  }

  private def createLabel(title: String): Label = {
    val label = new Label(this, SWT.NONE)
    label.setText(title)
    label
  }

  private def createScale(min: Int, max: Int, default: Int): Scale = {
    val scale = new Scale(this, SWT.HORIZONTAL)
    scale.setMinimum(min)
    scale.setMaximum(max)
    scale.setSelection(default)
    scale.setPageIncrement(5)
    scale
  }

}
