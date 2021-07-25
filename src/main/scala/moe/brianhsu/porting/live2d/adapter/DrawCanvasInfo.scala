package moe.brianhsu.porting.live2d.adapter

trait DrawCanvasInfo {
  def currentCanvasWidth: Int

  def currentCanvasHeight: Int

  def currentSurfaceWidth: Int

  def currentSurfaceHeight: Int
}
