package moe.brianhsu.porting.live2d.swtopengl

import moe.brianhsu.porting.live2d.adapter.lwjgl.{LWJGLOpenGL, SWTOpenGLCanvasInfo}
import moe.brianhsu.porting.live2d.demo.LAppView
import org.eclipse.swt._
import org.eclipse.swt.events.{KeyEvent, KeyListener, MouseEvent, MouseListener}
import org.eclipse.swt.layout._
import org.eclipse.swt.opengl._
import org.eclipse.swt.widgets._
import org.lwjgl.opengl._

object SWTOpenGL {

  def main(args: Array[String]): Unit = {
    val display = new Display()
    val shell = new Shell(display)
    shell.setLayout(new FillLayout)
    val comp = new Composite(shell, SWT.NONE)
    comp.setLayout(new FillLayout)
    val data = new GLData()
    data.doubleBuffer = true
    val canvas = new GLCanvas(comp, SWT.NONE, data)
    canvas.setCurrent()
    GL.createCapabilities()

    implicit val gl = new LWJGLOpenGL
    val canvasInfo = new SWTOpenGLCanvasInfo(canvas)
    val appView = new LAppView(canvasInfo)

    canvas.addListener(SWT.Resize, _ => {
      canvas.setCurrent()
      GL.createCapabilities()
      appView.resize()
      appView.display()
    })

    val updater = new Runnable() {
      override def run(): Unit = {
        if (!canvas.isDisposed) {
          canvas.setCurrent()
          GL.createCapabilities()
          appView.display()
          canvas.swapBuffers()
          display.asyncExec(this)
        }
      }
    }
    canvas.addPaintListener(_ => updater.run())
    canvas.addKeyListener(new KeyListener {
      override def keyPressed(e: KeyEvent): Unit = {}
      override def keyReleased(e: KeyEvent): Unit = {
        appView.keyReleased(e.character)
      }
    })
    canvas.addMouseListener(new MouseListener() {
      override def mouseDoubleClick(e: MouseEvent): Unit = {}
      override def mouseDown(e: MouseEvent): Unit = {}
      override def mouseUp(e: MouseEvent): Unit = {
        if ((e.stateMask & SWT.BUTTON1) != 0) {
          appView.onMouseReleased(e.x, e.y)
        }
      }
    })
    canvas.addMouseMoveListener( mouseEvent => {
      if ((mouseEvent.stateMask & SWT.BUTTON1) != 0) {
        appView.onMouseDragged(mouseEvent.x, mouseEvent.y)
      }
    })
    display.asyncExec(updater)
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
