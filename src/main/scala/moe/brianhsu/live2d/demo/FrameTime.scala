package moe.brianhsu.live2d.demo


object FrameTime {
  private var currentFrame = 0L
  private var lastFrame = 0L
  private var deltaTime = 0L

  def getDeltaTime: Long = deltaTime
  def updateFrameTime(currentTime: Long): Unit = {
    this.currentFrame = currentTime
    this.deltaTime = currentTime - this.lastFrame
    this.lastFrame = currentTime
  }
}
