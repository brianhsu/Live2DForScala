package moe.brianhsu.porting.live2d.swing.widget

import moe.brianhsu.live2d.enitiy.avatar.effect.impl.{Breath, EyeBlink, FaceDirection}
import moe.brianhsu.live2d.usecase.updater.impl.BasicUpdateStrategy
import moe.brianhsu.porting.live2d.Live2DView.{ClickAndDrag, FollowMouse}
import moe.brianhsu.porting.live2d.swing.Live2DWidget

import java.awt.FlowLayout
import java.awt.event.ActionEvent
import javax.swing.{BorderFactory, JCheckBox, JComboBox, JPanel}
import scala.annotation.unused

class EffectSelector(live2DWidget: Live2DWidget) extends JPanel {
  private val blink = new JCheckBox("Blink")
  private val breath = new JCheckBox("Breath")
  private val faceDirection = new JCheckBox("Face direction")
  private val comboBox = new JComboBox[String](Array("Click and drag", "Follow mouse"))

  {
    this.setLayout(new FlowLayout)
    this.setBorder(BorderFactory.createTitledBorder("Effects"))
    this.add(blink)
    this.add(breath)
    this.add(faceDirection)
    this.add(comboBox)
    this.blink.setSelected(false)
    this.blink.addActionListener((_: ActionEvent) => {
      if (this.blink.isSelected) {
        live2DWidget.doWithLive2DView(_.enableEyeBlink())
      } else {
        live2DWidget.doWithLive2DView(_.disableEyeBlink())
      }
    })
    this.breath.setSelected(false)
    this.breath.addActionListener((_: ActionEvent) => {
      if (this.breath.isSelected) {
        live2DWidget.doWithLive2DView(_.enableBreath())
      } else {
        live2DWidget.doWithLive2DView(_.disableBreath())
      }
    })
    this.faceDirection.setSelected(false)
    this.faceDirection.addActionListener(updateFaceDirectionMode)
    this.comboBox.setEnabled(false)
    this.comboBox.addActionListener(updateFaceDirectionMode)
    this.comboBox.setSelectedIndex(0)
  }

  def updateFaceDirectionMode(@unused actionEvent: ActionEvent): Unit = {
    this.comboBox.setEnabled(this.faceDirection.isSelected)
    live2DWidget.doWithLive2DView { live2D =>
      live2D.disableFaceDirection()
      live2D.resetFaceDirection()
      live2D.faceDirectionMode = this.comboBox.getSelectedIndex match {
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
    this.comboBox.setEnabled(hasFaceDirection)

    live2DWidget.doWithLive2DView { live2D =>
      live2D.faceDirectionMode match {
        case ClickAndDrag => this.comboBox.setSelectedIndex(0)
        case FollowMouse => this.comboBox.setSelectedIndex(1)
      }
    }
  }

}
