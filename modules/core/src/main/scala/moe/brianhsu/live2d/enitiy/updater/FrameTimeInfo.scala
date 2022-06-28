package moe.brianhsu.live2d.enitiy.updater

trait FrameTimeInfo {
  /**
   * How long has passed since last frame.
   * @return The seconds indicate how long has been passed since last frame.
   */
  def deltaTimeInSeconds: Float

  /**
   * How much longer has passed since first frame.
   *
   * @return The seconds indicate how long has been passed since first frame.
   */
  def totalElapsedTimeInSeconds: Float
}
