package moe.brianhsu.live2d.boundary.gateway.renderer

trait DrawCanvasInfo {
  def currentCanvasWidth: Int
  def currentCanvasHeight: Int
  def currentSurfaceWidth: Int
  def currentSurfaceHeight: Int
}
