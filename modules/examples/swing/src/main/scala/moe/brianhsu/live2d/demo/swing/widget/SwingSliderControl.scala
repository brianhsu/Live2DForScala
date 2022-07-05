package moe.brianhsu.live2d.demo.swing.widget

import java.awt.{GridBagConstraints, GridBagLayout}
import javax.swing.event.ChangeEvent
import javax.swing.{JComponent, JLabel, JSlider}

class SwingSliderControl(title: String, min: Int, max: Int, default: Int) extends JComponent {
  val titleLabel = new JLabel(s"$title:")
  val slider = new JSlider(min, max, default)
  val percentageLabel = new JLabel(s"$default%")

  {
    this.setLayout(new GridBagLayout())

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.fill = GridBagConstraints.NONE
    gc1.anchor = GridBagConstraints.NORTHWEST
    this.add(titleLabel, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 1
    gc2.gridy = 0
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.weightx = 1
    this.add(slider, gc2)

    val gc3 = new GridBagConstraints()
    gc3.gridx = 2
    gc3.gridy = 0
    gc3.fill = GridBagConstraints.NONE
    gc3.anchor = GridBagConstraints.NORTHWEST

    this.add(percentageLabel, gc3)

    this.slider.addChangeListener { _: ChangeEvent => updatePercentage(slider.getValue) }
  }

  override def setEnabled(isEnabled: Boolean): Unit = {
    this.slider.setEnabled(isEnabled)
  }

  def updatePercentage(value: Int): Unit = {
    this.slider.setValue(value)
    percentageLabel.setText(s"$value%")
  }

}
