package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMic
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets.{Button, Combo, Composite, Event}

import javax.sound.sampled.Mixer
import scala.annotation.unused

class SWTMixerSelector(parent: Composite, onMixerChanged: Option[Mixer] => Unit) extends Composite(parent, SWT.NONE) {
  private val deviceRefresh = new Button(this, SWT.PUSH)
  private val lipSyncDevice = createSoundDeviceSelector()
  private var mixers: List[Mixer] = Nil

  {
    val gridLayout = new GridLayout(2, false)
    gridLayout.marginBottom = 5
    this.setLayout(gridLayout)
    deviceRefresh.setText("\uD83D\uDD03")
    deviceRefresh.addListener(SWT.Selection, (_: Event) => updateSoundDeviceSelector())
    lipSyncDevice.addListener(SWT.Selection, onDeviceSelected)
    updateSoundDeviceSelector()
    onMixerChanged(currentMixer)
  }

  def currentMixer: Option[Mixer] = mixers.drop(lipSyncDevice.getSelectionIndex).headOption

  override def setEnabled(enabled: Boolean): Unit = {
    super.setEnabled(enabled)
    this.deviceRefresh.setEnabled(enabled)
    this.lipSyncDevice.setEnabled(enabled)
  }

  private def onDeviceSelected(@unused event: Event): Unit = {
    onMixerChanged(currentMixer)
  }

  private def createSoundDeviceSelector(): Combo = {
    val dropdown = new Combo(this, SWT.READ_ONLY|SWT.DROP_DOWN)
    val data = new GridData()
    data.grabExcessHorizontalSpace = true
    data.horizontalSpan = 1
    data.widthHint = 300
    dropdown.setLayoutData(data)
    dropdown
  }

  private def updateSoundDeviceSelector(): Unit = {

    new Thread {

      override def run(): Unit = {
        println("Update avaliable mixers...")
        SWTMixerSelector.this.mixers = LipSyncFromMic.availableMixers
        for (mixer <- SWTMixerSelector.this.mixers) {
          println(mixer.getMixerInfo.getDescription)
        }


        parent.getDisplay.syncExec(() => {
          lipSyncDevice.removeAll()
          mixers.foreach { mixer => lipSyncDevice.add(mixer.getMixerInfo.getDescription) }
          lipSyncDevice.select(0)
          onMixerChanged(currentMixer)
          parent.layout(true)
        })
      }
    }.start()

  }

}
