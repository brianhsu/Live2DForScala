package moe.brianhsu.live2d.demo.swt.widget

import moe.brianhsu.live2d.demo.app.DemoApp
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets._

class SWTAvatarControlPanel(parent: Composite) extends Composite(parent, SWT.NONE) {

  val tabFolder = new TabFolder(this, SWT.BORDER)
  val tabItem = new TabItem(tabFolder, SWT.NONE)
  val tabItem1 = new TabItem(tabFolder, SWT.NONE)

  val normalComposite = new Composite(tabFolder, SWT.NONE)
  val faceTrackingComposite = new SWTFaceTrackingComposite(tabFolder)
  val effectSelector = new SWTEffectSelector(normalComposite)
  val motionSelector = new SWTMotionSelector(normalComposite)
  val expressionSelector = new SWTExpressionSelector(normalComposite)

  {
    tabItem.setText("Normal")
    tabItem.setControl(normalComposite)
    tabItem1.setText("Face Tracking")
    tabItem1.setControl(faceTrackingComposite)

    this.setLayout(new FillLayout(SWT.VERTICAL))
    val fillLayout = new FillLayout(SWT.VERTICAL)
    fillLayout.marginWidth = 10
    this.normalComposite.setLayout(fillLayout)

  }

  def setDemoApp(demoApp: DemoApp): Unit = {
    effectSelector.setDemoApp(demoApp)
    motionSelector.setDemoApp(demoApp)
    expressionSelector.setDemoApp(demoApp)
    faceTrackingComposite.setDemoApp(demoApp)

  }
}
