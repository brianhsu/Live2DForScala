package moe.brianhsu.porting.live2d.adapter.lwjgl

import moe.brianhsu.porting.live2d.adapter.DrawCanvasInfo
import org.eclipse.swt.opengl.GLCanvas

class SWTOpenGLCanvasInfo(canvas: GLCanvas) extends DrawCanvasInfo {
  override def currentCanvasWidth: Int = canvas.getBounds.width

  override def currentCanvasHeight: Int = canvas.getBounds.height

  override def currentSurfaceWidth: Int = canvas.getSize.x

  override def currentSurfaceHeight: Int = canvas.getSize.y
}
