package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMic
import java.awt.event.ActionEvent
import java.awt.{GridBagConstraints, GridBagLayout, Insets}
import javax.sound.sampled.Mixer
import javax.swing._
import scala.annotation.unused

class SwingMixerSelector(onMixerChanged: Option[Mixer] => Unit) extends JComponent {
  private var mixers: List[Mixer] = Nil

  val deviceRefresh = new JButton("Refresh")
  val lipSyncDevice = new JComboBox[String](Array.empty[String])
  val sliderControl = new SwingSliderControl("Weight:", 0, 100, 50)
  val forceLipSync = new JCheckBox("Force Lip Sync")

  {
    this.setLayout(new GridBagLayout)

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.anchor = GridBagConstraints.NORTHWEST
    this.add(deviceRefresh, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 1
    gc2.gridy = 0
    gc2.anchor = GridBagConstraints.NORTHWEST
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.weightx = 1
    gc2.insets = new Insets(0, 10, 5, 0)
    lipSyncDevice.setPrototypeDisplayValue("MaxSizeString")
    this.add(lipSyncDevice, gc2)

    val gc3 = new GridBagConstraints()
    gc3.gridx = 0
    gc3.gridy = 1
    gc3.gridwidth = 2
    gc3.fill = GridBagConstraints.HORIZONTAL
    gc3.fill = GridBagConstraints.HORIZONTAL
    gc3.weightx = 1
    this.add(sliderControl, gc3)

    val gc4 = new GridBagConstraints()
    gc4.gridx = 0
    gc4.gridy = 2
    gc4.fill = GridBagConstraints.HORIZONTAL
    gc4.gridwidth = 2
    gc4.anchor = GridBagConstraints.NORTHWEST
    this.add(forceLipSync, gc4)

    this.forceLipSync.setEnabled(false)
    this.forceLipSync.setToolTipText("Force lip sync with ParamMouthOpenY even when the model does not declare support it")

    this.updateSoundDeviceSelector()
    this.lipSyncDevice.addActionListener(onStatusChanged)
    this.forceLipSync.addActionListener(onStatusChanged)
  }

  def isForceLipSync: Boolean = forceLipSync.isSelected

  def currentWeightPercentage: Int = sliderControl.percentage

  def currentMixer: Option[Mixer] = mixers.drop(lipSyncDevice.getSelectedIndex).headOption

  override def setEnabled(enabled: Boolean): Unit = {
    super.setEnabled(enabled)
    this.deviceRefresh.setEnabled(enabled)
    this.lipSyncDevice.setEnabled(enabled)
    this.sliderControl.setEnabled(enabled)
    this.forceLipSync.setEnabled(enabled)
  }

  private def onStatusChanged(@unused actionEvent: ActionEvent): Unit = {
    onMixerChanged(currentMixer)
  }

  private def updateSoundDeviceSelector(): Unit = {

    new Thread {

      override def run(): Unit = {
        SwingMixerSelector.this.mixers = LipSyncFromMic.findInputMixers()

        for (mixer <- SwingMixerSelector.this.mixers) {
          println(mixer.getMixerInfo.getDescription)
        }

        SwingUtilities.invokeLater(() => {
          lipSyncDevice.removeAllItems()
          mixers.foreach { mixer =>
            val mixerName = mixer.getMixerInfo.getName
            val mixerDesc = mixer.getMixerInfo.getDescription.dropWhile(_ != ':').drop(1).trim
            val title = s"$mixerName ($mixerDesc)"
            lipSyncDevice.addItem(title)
          }
          lipSyncDevice.setSelectedIndex(0)
          onMixerChanged(currentMixer)
        })
      }
    }.start()

  }

}
