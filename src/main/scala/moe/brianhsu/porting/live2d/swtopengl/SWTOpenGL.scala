package moe.brianhsu.porting.live2d.swtopengl

import moe.brianhsu.porting.live2d.adapter.lwjgl.{LWJGLOpenGL, SWTOpenGLCanvasInfo}
import moe.brianhsu.porting.live2d.demo.LAppView
import org.eclipse.swt._
import org.eclipse.swt.events.{PaintEvent, PaintListener}
import org.eclipse.swt.graphics._
import org.eclipse.swt.layout._
import org.eclipse.swt.opengl._
import org.eclipse.swt.widgets._
import org.lwjgl.opengl._
import org.lwjgl.opengles.GLES

object SWTOpenGL {
  def drawTorus(r: Float, R: Float, nsides: Int, rings: Int): Unit = {
    val ringDelta = 2.0f * Math.PI.asInstanceOf[Float] / rings
    val sideDelta = 2.0f * Math.PI.asInstanceOf[Float] / nsides
    var theta = 0.0f
    var cosTheta = 1.0f
    var sinTheta = 0.0f
    var i = rings - 1
    while (i >= 0) {
      val theta1 = theta + ringDelta
      val cosTheta1 = Math.cos(theta1).asInstanceOf[Float]
      val sinTheta1 = Math.sin(theta1).asInstanceOf[Float]
      GL11.glBegin(GL11.GL_QUAD_STRIP)
      var phi = 0.0f
      var j = nsides
      while (j >= 0) {
        phi += sideDelta
        val cosPhi = Math.cos(phi).asInstanceOf[Float]
        val sinPhi = Math.sin(phi).asInstanceOf[Float]
        val dist = R + r * cosPhi;
        GL11.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi)
        GL11.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi)
        GL11.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi)
        GL11.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi)
        j = j - 1
      }
      GL11.glEnd();
      theta = theta1;
      cosTheta = cosTheta1;
      sinTheta = sinTheta1;
      i = i - 1
    }
  }
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
    GL.createCapabilities
    val canvasInfo = new SWTOpenGLCanvasInfo(canvas)
    val appView = new LAppView(canvasInfo)(new LWJGLOpenGL)

    canvas.addListener(SWT.RESIZE, event => {
      appView.resize()
      appView.display()
    })
    val run = new Runnable() {
      override def run(): Unit = {
        if (!canvas.isDisposed()) {
          canvas.setCurrent();
          GL.createCapabilities();
          appView.display()
          canvas.swapBuffers();
          display.asyncExec(this);
        }
      }
    };
    canvas.addListener(SWT.Paint, event => run.run());
    display.asyncExec(run);
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
