package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.demo.app.DemoApp.{ClickAndDrag, FollowMouse}
import moe.brianhsu.live2d.demo.swing.Live2DUI
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection}
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy
import moe.brianhsu.live2d.usecase.updater.impl.GenericUpdateStrategy.EffectTiming.{AfterExpression, BeforeExpression}

import java.awt.event.ActionEvent
import java.awt.{GridBagConstraints, GridBagLayout}
import javax.sound.sampled.Mixer
import javax.swing.event.ChangeEvent
import javax.swing.{BorderFactory, JCheckBox, JComboBox, JPanel}
import scala.annotation.unused


class SwingEffectSelector(live2DWidget: Live2DUI) extends JPanel {

  private val blink = new JCheckBox("Blink")
  private val breath = new JCheckBox("Breath")
  private val faceDirection = new JCheckBox("Face direction")
  private val faceDirectionMode = new JComboBox[String](Array("Click and drag", "Follow mouse"))
  private val lipSyncFromMic = new JCheckBox("Lip Sync")
  private val lipSyncDevice = new SwingMixerSelector(onMixerChanged)

  {
    this.setLayout(new GridBagLayout)
    this.setBorder(BorderFactory.createTitledBorder("Effects"))

    val gc1 = new GridBagConstraints()
    gc1.gridx = 0
    gc1.gridy = 0
    gc1.fill = GridBagConstraints.HORIZONTAL
    gc1.anchor = GridBagConstraints.NORTHWEST
    this.add(blink, gc1)

    val gc2 = new GridBagConstraints()
    gc2.gridx = 1
    gc2.gridy = 0
    gc2.fill = GridBagConstraints.HORIZONTAL
    gc2.anchor = GridBagConstraints.NORTHWEST
    this.add(breath, gc2)

    val gc3 = new GridBagConstraints()
    gc3.gridx = 0
    gc3.gridy = 1
    gc3.fill = GridBagConstraints.HORIZONTAL
    gc3.anchor = GridBagConstraints.NORTHWEST
    this.add(faceDirection, gc3)

    val gc4 = new GridBagConstraints()
    gc4.gridx = 1
    gc4.gridy = 1
    gc4.fill = GridBagConstraints.HORIZONTAL
    gc4.anchor = GridBagConstraints.NORTHWEST
    this.add(faceDirectionMode, gc4)

    val gc5 = new GridBagConstraints()
    gc5.gridx = 0
    gc5.gridy = 2
    gc5.fill = GridBagConstraints.HORIZONTAL
    gc5.gridwidth = 2
    gc5.anchor = GridBagConstraints.NORTHWEST
    this.add(lipSyncFromMic, gc5)

    val gc6 = new GridBagConstraints()
    gc6.gridx = 0
    gc6.gridy = 3
    gc6.fill = GridBagConstraints.HORIZONTAL
    gc6.gridwidth = 2
    gc6.anchor = GridBagConstraints.NORTHWEST
    this.add(lipSyncDevice, gc6)


    this.blink.setSelected(false)
    this.blink.addActionListener(updateBlinkEffect)

    this.breath.setSelected(false)
    this.breath.addActionListener(updateBreathEffect)

    this.faceDirection.setSelected(false)
    this.faceDirection.addActionListener(updateFaceDirectionMode)

    this.faceDirectionMode.setEnabled(false)
    this.faceDirectionMode.setSelectedIndex(0)
    this.faceDirectionMode.addActionListener(updateFaceDirectionMode)

    this.lipSyncDevice.setEnabled(false)
    this.lipSyncFromMic.addActionListener(onLipSycFromMicChanged)
    this.lipSyncDevice.sliderControl.slider.addChangeListener { _: ChangeEvent =>
      onMicLipSyncWeightChanged(lipSyncDevice.currentWeightPercentage)
    }
  }

  def syncWithStrategy(strategy: GenericUpdateStrategy): Unit = {
    val effects = strategy.effects(BeforeExpression) ++ strategy.effects(AfterExpression)
    val hasEyeBlink = effects.exists(_.isInstanceOf[EyeBlink])
    val hasBreath = effects.exists(_.isInstanceOf[Breath])
    val hasFaceDirection = effects.exists(_.isInstanceOf[FaceDirection])
    this.blink.setSelected(hasEyeBlink)
    this.breath.setSelected(hasBreath)
    this.faceDirection.setSelected(hasFaceDirection)
    this.faceDirectionMode.setEnabled(hasFaceDirection)
    lipSyncFromMic.setSelected(false)
    this.lipSyncDevice.setEnabled(false)

    live2DWidget.demoAppHolder.foreach { live2D =>
      live2D.faceDirectionMode match {
        case ClickAndDrag => this.faceDirectionMode.setSelectedIndex(0)
        case FollowMouse => this.faceDirectionMode.setSelectedIndex(1)
      }
    }
  }

  private def updateBlinkEffect(@unused actionEvent: ActionEvent): Unit = {
    if (this.blink.isSelected) {
      live2DWidget.demoAppHolder.foreach(_.enableEyeBlink())
    } else {
      live2DWidget.demoAppHolder.foreach(_.disableEyeBlink())
    }
  }

  private def updateBreathEffect(@unused actionEvent: ActionEvent): Unit = {
    if (this.breath.isSelected) {
      live2DWidget.demoAppHolder.foreach(_.enableBreath())
    } else {
      live2DWidget.demoAppHolder.foreach(_.disableBreath())
    }
  }

  private def updateFaceDirectionMode(@unused actionEvent: ActionEvent): Unit = {
    this.faceDirectionMode.setEnabled(this.faceDirection.isSelected)
    live2DWidget.demoAppHolder.foreach { live2D =>
      live2D.disableFaceDirection()
      live2D.resetFaceDirection()
      live2D.faceDirectionMode = this.faceDirectionMode.getSelectedIndex match {
        case 0 => ClickAndDrag
        case 1 => FollowMouse
        case _ => ClickAndDrag
      }

      if (this.faceDirection.isSelected) {
        live2D.enableFaceDirection()
      } else {
        live2D.disableFaceDirection()
      }
    }
  }

  private def onMicLipSyncWeightChanged(weight: Int): Unit = {
    live2DWidget.demoAppHolder.foreach(_.updateMicLipSyncWeight(weight))
  }

  private def onMixerChanged(mixerHolder: Option[Mixer]): Unit = {
    live2DWidget.demoAppHolder.foreach { demoApp =>
      demoApp.disableMicLipSync()
      mixerHolder
        .filter(_ => lipSyncFromMic.isSelected)
        .foreach(mixer => demoApp.enableMicLipSync(mixer, lipSyncDevice.currentWeightPercentage, lipSyncDevice.isForceLipSync))
    }
  }

  private def onLipSycFromMicChanged(@unused event: ActionEvent): Unit = {
    lipSyncDevice.setEnabled(lipSyncFromMic.isSelected)
    live2DWidget.demoAppHolder.foreach { demoApp =>
      if (lipSyncFromMic.isSelected) {
        lipSyncDevice.currentMixer.foreach { mixer =>
          demoApp.enableMicLipSync(mixer, lipSyncDevice.currentWeightPercentage, lipSyncDevice.isForceLipSync)
        }
      } else {
        demoApp.disableMicLipSync()
      }
    }

  }


}
