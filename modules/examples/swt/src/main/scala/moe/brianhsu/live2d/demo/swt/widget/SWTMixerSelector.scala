package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.LipSyncFromMic
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets.{Button, Combo, Composite, Event}

import javax.sound.sampled.Mixer
import scala.annotation.unused

class SWTMixerSelector(parent: Composite, onMixerChanged: Option[Mixer] => Unit) extends Composite(parent, SWT.NONE) {
  private var mixers: List[Mixer] = Nil

  val deviceRefresh: Button = new Button(this, SWT.PUSH)
  val lipSyncDevice: Combo = createSoundDeviceSelector()
  val sliderControl: SWTSliderControl = new SWTSliderControl("Weight:", 0, 100, 50, this)
  val forceLipSync: Button = createForceCheckbox()

  {
    val gridLayout = new GridLayout(2, false)
    this.setLayout(gridLayout)

    deviceRefresh.setText("\uD83D\uDD03")
    lipSyncDevice.setText("aa")

    val layoutData1 = new GridData
    layoutData1.horizontalAlignment = GridData.FILL
    layoutData1.grabExcessVerticalSpace = false
    layoutData1.grabExcessHorizontalSpace = true
    lipSyncDevice.setLayoutData(layoutData1)

    val layoutData2 = new GridData
    layoutData2.horizontalAlignment = GridData.FILL
    layoutData2.horizontalSpan = 2
    layoutData2.grabExcessHorizontalSpace = true
    sliderControl.setLayoutData(layoutData2)

    val layoutData3 = new GridData
    layoutData3.horizontalAlignment = GridData.FILL
    layoutData3.horizontalSpan = 2
    layoutData3.grabExcessHorizontalSpace = true
    forceLipSync.setLayoutData(layoutData3)

    deviceRefresh.addListener(SWT.Selection, (_: Event) => updateSoundDeviceSelector())
    lipSyncDevice.addListener(SWT.Selection, onStatusChanged)
    forceLipSync.addListener(SWT.Selection, onStatusChanged)

    updateSoundDeviceSelector()
    onMixerChanged(currentMixer)
  }

  def isForceLipSync: Boolean = forceLipSync.getSelection

  def currentWeightPercentage: Int = sliderControl.percentage

  def currentMixer: Option[Mixer] = mixers.drop(lipSyncDevice.getSelectionIndex).headOption

  override def setEnabled(enabled: Boolean): Unit = {
    super.setEnabled(enabled)
    this.deviceRefresh.setEnabled(enabled)
    this.lipSyncDevice.setEnabled(enabled)
    this.forceLipSync.setEnabled(enabled)
  }

  private def onStatusChanged(@unused event: Event): Unit = {
    onMixerChanged(currentMixer)
  }

  private def createForceCheckbox(): Button = {
    val checkbox = new Button(this, SWT.CHECK)
    checkbox.setText("Force Lip Sync")
    checkbox.setToolTipText("Force lip sync with ParamMouthOpenY even when the model does not declare support it")
    checkbox
  }

  private def createSoundDeviceSelector(): Combo = {
    val dropdown = new Combo(this, SWT.READ_ONLY|SWT.DROP_DOWN)
    dropdown.add("Hello")
    dropdown.select(0)
    dropdown
  }

  private def updateSoundDeviceSelector(): Unit = {

    new Thread {

      override def run(): Unit = {
        SWTMixerSelector.this.mixers = LipSyncFromMic.findInputMixers()

        for (mixer <- SWTMixerSelector.this.mixers) {
          println(mixer.getMixerInfo.getDescription)
        }

        parent.getDisplay.syncExec(() => {
          lipSyncDevice.removeAll()
          mixers.foreach { mixer =>
            val mixerName = mixer.getMixerInfo.getName
            val mixerDesc = mixer.getMixerInfo.getDescription.dropWhile(_ != ':').drop(1).trim
            val title = s"$mixerName ($mixerDesc)"
            lipSyncDevice.add(title)
          }
          lipSyncDevice.select(0)
          onMixerChanged(currentMixer)
        })
      }
    }.start()

  }

}
