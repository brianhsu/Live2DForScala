package moe.brianhsu.live2d.enitiy.avatar.updater

trait UpdateStrategy {
  def update(deltaTimeInSeconds: Float): Unit
}
