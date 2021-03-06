package moe.brianhsu.live2d.enitiy.updater

class SystemNanoTimeBasedFrameInfo extends FrameTimeInfo {
  private var isFirstFrame: Boolean = true
  private var lastFrame: Long = 0L
  private var deltaTime: Long = 0L
  private var currentTimeInSeconds: Float = 0.0f

  /**
   * Update frame time info
   * 
   * @param currentSystemTimeInNano Current system time in nanoseconds.
   */
  def updateFrameTime(currentSystemTimeInNano: Long = System.nanoTime()): Unit = {
    val currentFrame = currentSystemTimeInNano
    if (isFirstFrame) {
      this.lastFrame = currentFrame
      this.isFirstFrame = false
    }
    this.deltaTime = currentFrame - this.lastFrame
    this.lastFrame = currentFrame
    this.currentTimeInSeconds = this.currentTimeInSeconds + deltaTimeInSeconds
  }

  /**
   * How long has passed since last frame.
   *
   * @return The seconds indicate how long has been passed since last frame.
   */
  override def deltaTimeInSeconds: Float = deltaTime / 1000000000.0f

  /**
   * How much longer has passed since first frame.
   *
   * @return The seconds indicate how long has been passed since first frame.
   */
  override def totalElapsedTimeInSeconds: Float = currentTimeInSeconds
}
