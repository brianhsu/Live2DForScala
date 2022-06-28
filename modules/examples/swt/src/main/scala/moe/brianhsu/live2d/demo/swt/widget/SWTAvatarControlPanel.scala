package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets.Composite

class SWTAvatarControlPanel(parent: Composite) extends Composite(parent, SWT.NONE) {

  val effectSelector = new SWTEffectSelector(this)
  val motionSelector = new SWTMotionSelector(this)
  val expressionSelector = new SWTExpressionSelector(this)

  {
    this.setLayout(new GridLayout(1, true))

    effectSelector.setLayout(new GridLayout(2, false))
    val groupLayoutData = new GridData
    groupLayoutData.horizontalAlignment = GridData.FILL
    groupLayoutData.verticalAlignment = GridData.FILL
    groupLayoutData.grabExcessVerticalSpace = true
    motionSelector.setLayoutData(groupLayoutData)
    expressionSelector.setLayoutData(groupLayoutData)
  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    effectSelector.setDemoApp(demoApp)
    motionSelector.setDemoApp(demoApp)
    expressionSelector.setDemoApp(demoApp)
  }
}
