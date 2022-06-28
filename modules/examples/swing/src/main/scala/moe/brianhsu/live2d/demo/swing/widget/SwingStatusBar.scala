package moe.brianhsu.live2d.demo.swing.widget

import javax.swing.{BorderFactory, JLabel}

class SwingStatusBar extends JLabel("Ready.") {
  this.setBorder(BorderFactory.createEtchedBorder())
}
