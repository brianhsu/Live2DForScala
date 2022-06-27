package moe.brianhsu.live2d.demo.swt

import moe.brianhsu.live2d.adapter.gateway.opengl.lwjgl.{LWJGLBinding, SWTOpenGLCanvasInfoReader}
import moe.brianhsu.live2d.demo.app.DemoApp
import moe.brianhsu.live2d.demo.swt.widget.AvatarArea
import org.eclipse.swt.SWT
import org.eclipse.swt.events.{KeyEvent, KeyListener, MouseEvent, MouseListener}
import org.eclipse.swt.layout.{FillLayout, GridLayout}
import org.eclipse.swt.opengl.{GLCanvas, GLData}
import org.eclipse.swt.widgets._
import org.lwjgl.opengl.GL


object SWTWithLWJGL {

  def createToolbar(parent: Composite) = {
    val toolBar = new ToolBar(parent, SWT.NONE)
    val loadAvatar = new ToolItem(toolBar, SWT.PUSH)
    loadAvatar.setText("Load Avatar")

    val separator1 = new ToolItem(toolBar, SWT.SEPARATOR)
    val defaultBackground = new ToolItem(toolBar, SWT.PUSH)
    defaultBackground.setText("Default Background")
    val separator2 = new ToolItem(toolBar, SWT.SEPARATOR)

    val selectBackground = new ToolItem(toolBar, SWT.PUSH)
    selectBackground.setText("Select Background")
    val separator3 = new ToolItem(toolBar, SWT.SEPARATOR)

    val pureColorBackground = new ToolItem(toolBar, SWT.PUSH)
    pureColorBackground.setText("Pure Color Background")

    toolBar
  }

  private def createStatusLine(parent: Composite) = {
    val label = new Label(parent, SWT.HORIZONTAL|SWT.SHADOW_OUT)
    label.setText("Ready")
    label
  }
  def main(args: Array[String]): Unit = {
    val display = new Display()
    val shell = new Shell(display)
    shell.setLayout(new GridLayout(2, false))
    import org.eclipse.swt.layout.GridData
    val gridData = new GridData
    gridData.horizontalSpan = 2
    gridData.horizontalAlignment = GridData.FILL
    gridData.grabExcessHorizontalSpace = true
    val toolbar = createToolbar(shell)
    toolbar.setLayoutData(gridData)

    val gridData2 = new GridData
    gridData2.horizontalSpan = 1
    gridData2.verticalAlignment = GridData.FILL
    gridData2.grabExcessHorizontalSpace = false
    gridData2.grabExcessVerticalSpace = true

    val leftPanel = new Button(shell, SWT.PUSH)
    leftPanel.setText("Left Panel")
    leftPanel.setLayoutData(gridData2)

    val gridData3 = new GridData
    gridData3.horizontalSpan = 1
    gridData3.horizontalAlignment = GridData.FILL
    gridData3.verticalAlignment = GridData.FILL
    gridData3.grabExcessHorizontalSpace = true
    gridData3.grabExcessVerticalSpace = true

    val avatarArea = new AvatarArea(display, shell)
    avatarArea.setLayoutData(gridData3)

    val gridData4 = new GridData
    gridData4.horizontalSpan = 2
    gridData4.horizontalAlignment = GridData.FILL
    gridData4.grabExcessHorizontalSpace = true
    val statusBar = createStatusLine(shell)
    statusBar.setLayoutData(gridData4)

    shell.setText("SWT/LWJGL Example")
    shell.setSize(640, 480)
    shell.open()

    while (!shell.isDisposed) {
      if (!display.readAndDispatch()) {
        display.sleep()
      }
    }
    display.dispose()

  }

}
