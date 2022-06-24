package moe.brianhsu.live2d.adapter.gateway.renderer.jogl

import com.jogamp.opengl.awt.GLCanvas
import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader

class JOGLCanvasInfoReader(canvasInfo: GLCanvas) extends DrawCanvasInfoReader {
  override def currentCanvasWidth: Int = canvasInfo.getWidth
  override def currentCanvasHeight: Int = canvasInfo.getHeight

  override def currentSurfaceWidth: Int = (canvasInfo.getWidth * canvasInfo.getGraphicsConfiguration.getDefaultTransform.getScaleX).toInt
  override def currentSurfaceHeight: Int = (canvasInfo.getHeight * canvasInfo.getGraphicsConfiguration.getDefaultTransform.getScaleY).toInt
}
