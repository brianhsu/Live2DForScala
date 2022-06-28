package moe.brianhsu.live2d.demo.swt.widget

import org.eclipse.swt.SWT
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.{Composite, Label}

class SWTStatusBar(parent: Composite) extends Composite(parent, SWT.NONE) {
  val statusText = new Label(this, SWT.HORIZONTAL|SWT.BORDER)

  {
    this.setLayout(new FillLayout)
    this.statusText.setText("Ready.")
  }

  def updateStatus(message: String): Unit = {
    this.statusText.setText(message)
  }
}
