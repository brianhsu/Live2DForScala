package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.app.DemoApp.{ClickAndDrag, FollowMouse}
import moe.brianhsu.live2d.usecase.updater.impl.EasyUpdateStrategy
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{FillLayout, GridData, GridLayout}
import org.eclipse.swt.widgets.{Button, Combo, Composite, Event, Group}

import javax.sound.sampled.Mixer
import scala.annotation.unused

class SWTEffectSelector(parent: Composite) extends Composite(parent, SWT.NONE) {
  private var demoAppHolder: Option[DemoApp] = None

  private val effectGroup = new Group(this, SWT.SHADOW_ETCHED_IN)
  private val blink = createCheckbox("Blink")
  private val breath = createCheckbox("Breath")
  private val faceDirection = createCheckbox("Face Direction", 2)
  private val faceDirectionMode = createDropdown("Click and drag" :: "Follow by mouse" :: Nil)
  private val lipSyncFromMic = createCheckbox("Lip Sync", 2)
  private val lipSyncDevice = new SWTMixerSelector(effectGroup, onMixerChanged)

  {
    this.setLayout(new FillLayout)

    effectGroup.setText("Effects")
    effectGroup.setLayout(new GridLayout(2, false))

    val deviceSelectorLayoutData = new GridData
    deviceSelectorLayoutData.horizontalAlignment = GridData.FILL
    deviceSelectorLayoutData.grabExcessHorizontalSpace = true
    deviceSelectorLayoutData.horizontalSpan = 2
    lipSyncDevice.setLayoutData(deviceSelectorLayoutData)
    lipSyncDevice.setEnabled(false)
    faceDirectionMode.setEnabled(false)

    blink.addListener(SWT.Selection, onBlinkChanged)
    breath.addListener(SWT.Selection, onBreathChanged)
    faceDirection.addListener(SWT.Selection, updateFaceDirectionMode)
    faceDirectionMode.addListener(SWT.Selection, updateFaceDirectionMode)
    lipSyncFromMic.addListener(SWT.Selection, onLipSycFromMicChanged)
    lipSyncDevice.sliderControl.addChangeListener(onMicLipSyncWeightChanged)

  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    this.demoAppHolder = Some(demoApp)
  }


  def syncWithStrategy(strategy: EasyUpdateStrategy): Unit = {
    this.blink.setSelection(strategy.isEyeBlinkEnabled)
    this.breath.setSelection(strategy.isBreathEnabled)
    this.faceDirection.setSelection(strategy.isFaceDirectionEnabled)
    this.faceDirectionMode.setEnabled(strategy.isFaceDirectionEnabled)
    this.lipSyncFromMic.setSelection(false)
    this.lipSyncDevice.setEnabled(false)

    demoAppHolder.foreach { live2D =>
      live2D.faceDirectionMode match {
        case ClickAndDrag => this.faceDirectionMode.select(0)
        case FollowMouse => this.faceDirectionMode.select(1)
      }
    }
  }



  private def createCheckbox(title: String, rowSpan: Int = 1): Button = {
    val button = new Button(effectGroup, SWT.CHECK)
    val layoutData = new GridData

    layoutData.horizontalSpan = rowSpan

    button.setText(title)
    button.setLayoutData(layoutData)
    button
  }

  private def createDropdown(values: List[String]) = {
    val dropdown = new Combo(effectGroup, SWT.READ_ONLY|SWT.DROP_DOWN)
    values.foreach(dropdown.add)
    dropdown.select(0)
    dropdown
  }

  private def updateFaceDirectionMode(@unused event: Event): Unit = {
    this.faceDirectionMode.setEnabled(this.faceDirection.getSelection)
    demoAppHolder.foreach { live2D =>
      live2D.enableFaceDirection(false)
      live2D.resetFaceDirection()
      live2D.faceDirectionMode = this.faceDirectionMode.getSelectionIndex match {
        case 0 => ClickAndDrag
        case 1 => FollowMouse
        case _ => ClickAndDrag
      }

      live2D.enableFaceDirection(this.faceDirection.getSelection)
    }
  }

  private def onLipSycFromMicChanged(@unused event: Event): Unit = {
    lipSyncDevice.setEnabled(lipSyncFromMic.getSelection)
    demoAppHolder.foreach { demoApp =>
      if (lipSyncFromMic.getSelection) {
        lipSyncDevice.currentMixer.foreach { mixer =>
          demoApp.enableMicLipSync(
            mixer, lipSyncDevice.currentWeightPercentage,
            lipSyncDevice.isForceLipSync
          )
        }
      } else {
        demoApp.disableMicLipSync()
      }
    }

  }

  private def onMicLipSyncWeightChanged(weight: Int): Unit = {
    demoAppHolder.foreach(_.updateMicLipSyncWeight(weight))
  }

  private def onMixerChanged(mixerHolder: Option[Mixer]): Unit = {
    demoAppHolder.foreach { demoApp =>
      demoApp.disableMicLipSync()
      mixerHolder
        .filter(_ => lipSyncFromMic.getSelection)
        .foreach { mixer =>
          demoApp.enableMicLipSync(
            mixer,
            lipSyncDevice.currentWeightPercentage,
            lipSyncDevice.isForceLipSync
          )
        }
    }
  }

  private def onBreathChanged(@unused event: Event): Unit = {
    demoAppHolder.foreach(_.enableBreath(this.breath.getSelection))
  }

  private def onBlinkChanged(@unused event: Event): Unit = {
    demoAppHolder.foreach(_.enableEyeBlink(this.blink.getSelection))
  }
}
