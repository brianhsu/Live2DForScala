package moe.brianhsu.live2d.demo


object FrameTime {
  private var currentFrame = 0L
  private var lastFrame = 0L
  private var deltaTime = 0L

  def getDeltaTime: Float = deltaTime / 1000000000.0f
  def updateFrameTime(): Unit = {
    this.currentFrame = System.nanoTime()
    this.deltaTime = currentFrame - this.lastFrame
    this.lastFrame = currentFrame
  }
}
