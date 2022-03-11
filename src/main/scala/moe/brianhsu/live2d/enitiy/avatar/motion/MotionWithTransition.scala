package moe.brianhsu.live2d.enitiy.avatar.motion
import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.math.Easing
import moe.brianhsu.live2d.enitiy.model.Live2DModel

class MotionWithTransition(motion: Motion) extends Motion {

  private var isFinished: Boolean = false
  private var isStarted: Boolean = false
  private var startTimeInSeconds: Float = -1.0f
  private var fadeInStartTimeInSeconds: Float = 0.0f
  private var endTimeInSeconds: Option[Float] = None

  override def fadeInTimeInSeconds: Float = motion.fadeInTimeInSeconds
  override def fadeOutTimeInSeconds: Float = motion.fadeOutTimeInSeconds
  override def durationInSeconds: Option[Float] = motion.durationInSeconds

  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[EffectOperation] = {
    if (isFinished) {
      Nil
    } else {
      if (!isStarted) {
        this.isStarted = true
        this.startTimeInSeconds = totalElapsedTimeInSeconds
        this.fadeInStartTimeInSeconds = totalElapsedTimeInSeconds
        if (this.endTimeInSeconds.isEmpty && durationInSeconds.isDefined) {
          this.endTimeInSeconds = Some(this.startTimeInSeconds + durationInSeconds.get)
        }
      }

      this.isFinished = this.endTimeInSeconds.exists(_ < totalElapsedTimeInSeconds)

      val fadeIn: Float = calculateFadeIn(totalElapsedTimeInSeconds)
      val fadeOut: Float = calculateFadeOut(totalElapsedTimeInSeconds)
      val fadeWeight = weight * fadeIn * fadeOut

      assert(fadeWeight >= 0.0f && fadeWeight <= 1.0f, "fadeWeight inside MotionWithTransition is invalid")

      motion.calculateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, fadeWeight)
    }
  }

  private def calculateFadeIn(totalElapsedTimeInSeconds: Float): Float = {
    if (fadeInTimeInSeconds == 0.0f) {
      1.0f
    } else {
      Easing.sine((totalElapsedTimeInSeconds - this.fadeInStartTimeInSeconds) / fadeInTimeInSeconds)
    }
  }

  private def calculateFadeOut(totalElapsedTimeInSeconds: Float): Float = {
    if (fadeOutTimeInSeconds == 0.0f || endTimeInSeconds.isEmpty) {
      1.0f
    } else {
      Easing.sine((this.endTimeInSeconds.get - totalElapsedTimeInSeconds) / fadeOutTimeInSeconds)
    }
  }
}
