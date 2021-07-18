package moe.brianhsu.live2d.demo

import com.jogamp.opengl.awt.GLCanvas

class OpenGLCanvasInfo(canvasInfo: GLCanvas) extends DrawCanvasInfo {
  override def currentCanvasWidth: Int = canvasInfo.getWidth
  override def currentCanvasHeight: Int = canvasInfo.getHeight
  override def currentSurfaceWidth: Int = canvasInfo.getSurfaceWidth
  override def currentSurfaceHeight: Int = canvasInfo.getSurfaceHeight
}
