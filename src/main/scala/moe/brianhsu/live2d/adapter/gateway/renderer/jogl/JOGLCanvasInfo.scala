package moe.brianhsu.live2d.adapter.gateway.renderer.jogl

import com.jogamp.opengl.awt.GLCanvas
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfo

class JOGLCanvasInfo(canvasInfo: GLCanvas) extends DrawCanvasInfo {
  override def currentCanvasWidth: Int = canvasInfo.getWidth
  override def currentCanvasHeight: Int = canvasInfo.getHeight
  override def currentSurfaceWidth: Int = canvasInfo.getSurfaceWidth
  override def currentSurfaceHeight: Int = canvasInfo.getSurfaceHeight
}
