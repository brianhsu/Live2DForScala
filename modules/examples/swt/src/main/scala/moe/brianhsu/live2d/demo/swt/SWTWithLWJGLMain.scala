package moe.brianhsu.live2d.demo.swt

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.swt.widget.SWTAvatarDisplayArea.AvatarListener
import moe.brianhsu.live2d.demo.swt.widget.{SWTAvatarControlPanel, SWTAvatarDisplayArea, SWTStatusBar, SWTToolbar}
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets.{Display, Shell}

object SWTWithLWJGLMain {

  private val display = new Display()
  private val shell = new Shell(display)
  private val toolbar = new SWTToolbar(shell)
  private val sashForm = new SashForm(shell, SWT.HORIZONTAL|SWT.SMOOTH)
  private val avatarControl = new SWTAvatarControlPanel(sashForm)
  private val avatarArea = new SWTAvatarDisplayArea(sashForm)
  private val statusBar = new SWTStatusBar(shell)

  def main(args: Array[String]): Unit = {
    try {
      setupUILayout()
      setupAvatarEventListener()

      shell.setText("Live 2D Scala Demo (SWT+LWJGL)")
      shell.setSize(1080, 720)
      shell.open()

      while (!shell.isDisposed) {
        if (!display.readAndDispatch()) {
          display.sleep()
        }
      }
    } finally {
      display.dispose()
      System.exit(0)
    }
  }

  private def setupAvatarEventListener(): Unit = {
    avatarControl.setDemoApp(avatarArea.demoApp)
    toolbar.setDemoApp(avatarArea.demoApp)
    avatarArea.setAvatarListener(new AvatarListener {
      override def onAvatarLoaded(live2DView: DemoApp): Unit = {
        live2DView.avatarHolder.foreach(avatarControl.expressionSelector.updateExpressionList)
        live2DView.avatarHolder.foreach(avatarControl.motionSelector.updateMotionTree)
        live2DView.strategyHolder.foreach { strategy =>
          avatarControl.effectSelector.syncWithStrategy(strategy)
          avatarControl.motionSelector.syncWithStrategy(strategy)
        }
        avatarControl.faceTrackingComposite.enableStartButton()
      }
      override def onStatusUpdated(status: String): Unit = statusBar.updateStatus(status)
    })

  }

  private def setupUILayout(): Unit = {
    shell.setLayout(new GridLayout(1, false))
    sashForm.setWeights(1, 4)
    sashForm.setSashWidth(5)

    val gridData = new GridData
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    toolbar.setLayoutData(gridData)

    val gridData1 = new GridData
    gridData1.horizontalAlignment = GridData.FILL
    gridData1.grabExcessHorizontalSpace = true
    gridData1.grabExcessVerticalSpace = true
    sashForm.setLayoutData(gridData1)

    val gridData4 = new GridData
    gridData4.horizontalAlignment = GridData.FILL
    gridData4.grabExcessHorizontalSpace = true
    statusBar.setLayoutData(gridData4)
  }

}
