package moe.brianhsu.live2d.demo.app

import moe.brianhsu.live2d.boundary.gateway.renderer.DrawCanvasInfoReader
import moe.brianhsu.live2d.demo.app.DemoApp.OnOpenGLThread
import moe.brianhsu.live2d.enitiy.opengl.OpenGLBinding
import moe.brianhsu.live2d.enitiy.opengl.texture.TextureManager

abstract class OpenGLBase(protected val drawCanvasInfo: DrawCanvasInfoReader, protected val onOpenGLThread: OnOpenGLThread)
                         (protected implicit val openGL: OpenGLBinding) {

  protected val textureManager: TextureManager = TextureManager.getInstance
  protected def display(isForceUpdate: Boolean = false): Unit
}
