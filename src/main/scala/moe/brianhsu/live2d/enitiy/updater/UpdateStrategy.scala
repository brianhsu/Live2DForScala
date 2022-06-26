package moe.brianhsu.live2d.enitiy.updater

trait UpdateStrategy {
  def update(frameTimeInfo: FrameTimeInfo): Unit
}
