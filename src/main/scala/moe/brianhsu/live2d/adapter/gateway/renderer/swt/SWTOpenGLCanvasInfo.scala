package moe.brianhsu.live2d.adapter.gateway.renderer.swt

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfo
import org.eclipse.swt.opengl.GLCanvas

class SWTOpenGLCanvasInfo(canvas: GLCanvas) extends DrawCanvasInfo {
  override def currentCanvasWidth: Int = canvas.getBounds.width
  override def currentCanvasHeight: Int = canvas.getBounds.height
  override def currentSurfaceWidth: Int = canvas.getSize.x
  override def currentSurfaceHeight: Int = canvas.getSize.y
}
