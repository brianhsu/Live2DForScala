package moe.brianhsu.porting.live2d.swtopengl

import org.eclipse.swt._
import org.eclipse.swt.graphics._
import org.eclipse.swt.layout._
import org.eclipse.swt.opengl._
import org.eclipse.swt.widgets._
import org.lwjgl.opengl._

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
    GL.createCapabilities()

    canvas.addListener(SWT.Resize, event => {
      val bounds = canvas.getBounds()
      val fAspect = bounds.width.asInstanceOf[Float] / bounds.height.asInstanceOf[Float]
      canvas.setCurrent()
      GL.createCapabilities()
      GL11.glViewport(0, 0, bounds.width, bounds.height)
      GL11.glMatrixMode(GL11.GL_PROJECTION)
      GL11.glLoadIdentity()
      val near = 0.5f
      val bottom = -near * Math.tan(45.0f / 2).asInstanceOf[Float]
      val left = fAspect * bottom
      GL11.glFrustum(left, -left, bottom, -bottom, near, 400.0f)
      GL11.glMatrixMode(GL11.GL_MODELVIEW)
      GL11.glLoadIdentity()
    })
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    GL11.glColor3f(1.0f, 0.0f, 0.0f)
    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST)
    GL11.glClearDepth(1.0)
    GL11.glLineWidth(2)
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    val run = new Runnable() {
      var rot = 0

      override def run(): Unit = {
        if (!canvas.isDisposed()) {
          canvas.setCurrent()
          GL.createCapabilities()
          GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
          GL11.glClearColor(.3f, .5f, .8f, 1.0f)
          GL11.glLoadIdentity()
          GL11.glTranslatef(0.0f, 0.0f, -10.0f)
          val frot = rot
          GL11.glRotatef(0.15f * rot, 2.0f * frot, 10.0f * frot, 1.0f)
          GL11.glRotatef(0.3f * rot, 3.0f * frot, 1.0f * frot, 1.0f)
          rot += 1
          GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
          GL11.glColor3f(0.9f, 0.9f, 0.9f)
          drawTorus(1, 1.9f + (Math.sin((0.004f * frot)).asInstanceOf[Float]), 15, 15)
          canvas.swapBuffers()
          display.asyncExec(this)
        }
      }
    };
    canvas.addListener(SWT.Paint, event => run.run())
    display.asyncExec(run)
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
