package moe.brianhsu.live2d.enitiy.avatar.motion

trait Motion extends MotionUpdater {
  def fadeInTimeInSeconds: Float
  def fadeOutTimeInSeconds: Float
  def durationInSeconds: Option[Float]
  def events: List[MotionEvent]
}
