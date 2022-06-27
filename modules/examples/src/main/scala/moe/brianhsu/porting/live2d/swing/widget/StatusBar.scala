package moe.brianhsu.porting.live2d.swing.widget

import javax.swing.{BorderFactory, JLabel}

class StatusBar extends JLabel("Ready.") {
  this.setBorder(BorderFactory.createEtchedBorder())
}
