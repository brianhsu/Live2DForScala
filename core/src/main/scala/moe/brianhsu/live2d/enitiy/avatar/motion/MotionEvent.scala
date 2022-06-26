package moe.brianhsu.live2d.enitiy.avatar.motion

case class MotionEvent(value: String, fireTime: Float) {

  /**
   * Check if this event should be fired.
   *
   * The time of these two parameter are based are start time of current motion as 0,
   * so lastCheckTimeInDeltaSeconds will always be a negative float number.
   *
   * @param lastCheckTimeInDeltaSeconds How many seconds ago we checked if this event should be fired.
   * @param currentMotionTimeInSeconds  How may seconds already passed for this motion.
   * @return
   */
  def shouldBeFired(lastCheckTimeInDeltaSeconds: Float, currentMotionTimeInSeconds: Float): Boolean = {
    this.fireTime >= lastCheckTimeInDeltaSeconds &&
      this.fireTime <= currentMotionTimeInSeconds
  }
}
