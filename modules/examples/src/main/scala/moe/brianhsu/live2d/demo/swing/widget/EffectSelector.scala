package moe.brianhsu.live2d.demo.swing.widget

import moe.brianhsu.live2d.demo.app.Live2DDemoApp.{ClickAndDrag, FollowMouse}
import moe.brianhsu.live2d.demo.swing.Live2DWidget
import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy

import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.{BorderFactory, JCheckBox, JComboBox, JPanel}
import scala.annotation.unused

class EffectSelector(live2DWidget: Live2DWidget) extends JPanel {

  private val blink = new JCheckBox("Blink")
  private val breath = new JCheckBox("Breath")
  private val faceDirection = new JCheckBox("Face direction")
  private val faceDirectionMode = new JComboBox[String](Array("Click and drag", "Follow mouse"))

  {
    this.setLayout(new GridLayout(2,2))
    this.setBorder(BorderFactory.createTitledBorder("Effects"))

    this.add(blink)
    this.add(breath)
    this.add(faceDirection)
    this.add(faceDirectionMode)

    this.blink.setSelected(false)
    this.blink.addActionListener(updateBlinkEffect)

    this.breath.setSelected(false)
    this.breath.addActionListener(updateBreathEffect)

    this.faceDirection.setSelected(false)
    this.faceDirection.addActionListener(updateFaceDirectionMode)

    this.faceDirectionMode.setEnabled(false)
    this.faceDirectionMode.setSelectedIndex(0)
    this.faceDirectionMode.addActionListener(updateFaceDirectionMode)
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


  def syncWithStrategy(basicUpdateStrategy: BasicUpdateStrategy): Unit = {
    val effects = basicUpdateStrategy.effects
    val hasEyeBlink = effects.exists(_.isInstanceOf[EyeBlink])
    val hasBreath = effects.exists(_.isInstanceOf[Breath])
    val hasFaceDirection = effects.exists(_.isInstanceOf[FaceDirection])
    this.blink.setSelected(hasEyeBlink)
    this.breath.setSelected(hasBreath)
    this.faceDirection.setSelected(hasFaceDirection)
    this.faceDirectionMode.setEnabled(hasFaceDirection)

    live2DWidget.demoAppHolder.foreach { live2D =>
      live2D.faceDirectionMode match {
        case ClickAndDrag => this.faceDirectionMode.setSelectedIndex(0)
        case FollowMouse => this.faceDirectionMode.setSelectedIndex(1)
      }
    }
  }

}
