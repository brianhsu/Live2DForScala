package moe.brianhsu.live2d.demo.swt

import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.swt.widget.SWTAvatarDisplayArea.AvatarListener
import moe.brianhsu.live2d.demo.swt.widget.{SWTAvatarControlPanel, SWTAvatarDisplayArea, SWTStatusBar, SWTToolbar}
import org.eclipse.swt.layout.{GridData, GridLayout}
import org.eclipse.swt.widgets.{Display, Shell}

object SWTWithLWJGLMain {

  private val display = new Display()
  private val shell = new Shell(display)
  private val toolbar = new SWTToolbar(shell)
  private val avatarControl = new SWTAvatarControlPanel(shell)
  private val avatarArea = new SWTAvatarDisplayArea(shell)
  private val statusBar = new SWTStatusBar(shell)

  def main(args: Array[String]): Unit = {
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
    display.dispose()

  }

  private def setupAvatarEventListener(): Unit = {
    avatarControl.setDemoApp(avatarArea.demoApp)
    toolbar.setDemoApp(avatarArea.demoApp)
    avatarArea.setAvatarListener(new AvatarListener {
      override def onAvatarLoaded(live2DView: DemoApp): Unit = {
        live2DView.avatarHolder.foreach(avatarControl.expressionSelector.updateExpressionList)
        live2DView.avatarHolder.foreach(avatarControl.motionSelector.updateMotionTree)
        live2DView.basicUpdateStrategyHolder.foreach { strategy =>
          avatarControl.effectSelector.syncWithStrategy(strategy)
          avatarControl.motionSelector.syncWithStrategy(strategy)
        }
      }
      override def onStatusUpdated(status: String): Unit = statusBar.updateStatus(status)
    })

  }

  private def setupUILayout(): Unit = {
    shell.setLayout(new GridLayout(2, false))

    val gridData = new GridData
    gridData.horizontalSpan = 2
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    toolbar.setLayoutData(gridData)

    val gridData2 = new GridData
    gridData2.horizontalSpan = 1
    gridData2.verticalAlignment = GridData.FILL
    gridData2.grabExcessHorizontalSpace = false
    gridData2.grabExcessVerticalSpace = true
    avatarControl.setLayoutData(gridData2)

    val gridData3 = new GridData
    gridData3.horizontalSpan = 1
    gridData3.horizontalAlignment = GridData.FILL
    gridData3.verticalAlignment = GridData.FILL
    gridData3.grabExcessHorizontalSpace = true
    gridData3.grabExcessVerticalSpace = true

    avatarArea.setLayoutData(gridData3)

    val gridData4 = new GridData
    gridData4.horizontalSpan = 2
    gridData4.horizontalAlignment = GridData.FILL
    gridData4.grabExcessHorizontalSpace = true
    statusBar.setLayoutData(gridData4)
  }

}
