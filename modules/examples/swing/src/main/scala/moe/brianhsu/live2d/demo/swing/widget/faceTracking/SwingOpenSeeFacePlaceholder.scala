package moe.brianhsu.live2d.demo.swing.widget.faceTracking

import moe.brianhsu.live2d.demo.openSeeFace.OpenSeeFaceSetting

import java.awt.{GridBagLayout, GridLayout}
import javax.swing._

class SwingOpenSeeFacePlaceholder extends JPanel with OpenSeeFaceSetting {

  this.setLayout(new GridBagLayout)
  this.setBorder(BorderFactory.createTitledBorder("OpenSeeFace Settings"))


  {
    this.setLayout(new GridLayout(1, 1))
    val message = "<html>Due to MacOS security model, <br>I can't offer a working bundled <br>OpenSeeFace. :(</html>"
    this.add(new JLabel(message))
  }

  override def getCommand: String = "NotSupported"

  override def getHostname: String = "127.0.0.1"

  override def getPort: Int = 11573
}
