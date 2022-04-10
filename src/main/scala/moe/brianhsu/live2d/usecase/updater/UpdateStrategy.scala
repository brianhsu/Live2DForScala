package moe.brianhsu.live2d.usecase.updater

import moe.brianhsu.live2d.enitiy.avatar.updater.FrameTimeInfo

trait UpdateStrategy {
  def update(frameTimeInfo: FrameTimeInfo): Unit
}
