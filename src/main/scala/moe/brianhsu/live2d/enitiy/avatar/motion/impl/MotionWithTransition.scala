package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.math.Easing
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object MotionWithTransition {
  type Callback= (MotionWithTransition, MotionEvent) => Unit
}

class MotionWithTransition(val baseMotion: Motion) extends Motion {

  private var mIsFinished: Boolean = false
  private var isStarted: Boolean = false
  private var startTimeInSeconds: Float = -1.0f
  private var fadeInStartTimeInSeconds: Float = 0.0f
  private var endTimeInSeconds: Option[Float] = None
  private var mIsForceToFadeOut: Boolean = false
  private var eventCallbackHolder: Option[Callback] = None
  private var lastEventCheckTimeInSeconds: Float = 0

  def isFinished: Boolean = mIsFinished
  def isForceToFadeOut: Boolean = mIsForceToFadeOut

  override def fadeInTimeInSeconds: Float = baseMotion.fadeInTimeInSeconds
  override def fadeOutTimeInSeconds: Float = baseMotion.fadeOutTimeInSeconds
  override def durationInSeconds: Option[Float] = baseMotion.durationInSeconds
  override def events: List[MotionEvent] = baseMotion.events
  override def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[EffectOperation] = {
    if (mIsFinished) {
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

      this.mIsFinished = this.endTimeInSeconds.exists(_ < totalElapsedTimeInSeconds)

      fireTriggeredEvents(totalElapsedTimeInSeconds)
      createUpdateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, weight)
    }
  }

  def setEventCallback(callback: Callback): Unit = {
    this.eventCallbackHolder = Some(callback)
  }

  def startFadeOut(totalElapsedTimeInSeconds: Float): Unit = {
    this.mIsForceToFadeOut = true
    val newEndTimeSeconds = totalElapsedTimeInSeconds + fadeOutTimeInSeconds
    val newEndTimeLessThanOriginal = endTimeInSeconds.forall(newEndTimeSeconds < _)
    if (newEndTimeLessThanOriginal) {
      this.endTimeInSeconds = Some(newEndTimeSeconds)
    }
  }

  def markAsForceFadeOut(): Unit = {
    this.mIsForceToFadeOut = true
  }

  private def fireTriggeredEvents(totalElapsedTimeInSeconds: Float): Unit = {
    for {
      _ <- durationInSeconds
      eventCallback <- eventCallbackHolder
      triggeredEvent <- events.filter(_.shouldBeFired(this.lastEventCheckTimeInSeconds - startTimeInSeconds,
                                                      totalElapsedTimeInSeconds - startTimeInSeconds))
    } {
      eventCallback(this, triggeredEvent)
    }

    this.lastEventCheckTimeInSeconds = totalElapsedTimeInSeconds
  }

  private def createUpdateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float) = {
    val fadeIn: Float = calculateFadeIn(totalElapsedTimeInSeconds)
    val fadeOut: Float = calculateFadeOut(totalElapsedTimeInSeconds)
    val fadeWeight = weight * fadeIn * fadeOut

    baseMotion.calculateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, fadeWeight)
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

  /**
   * Get the value of endTimeInSeconds
   *
   * For unit test only.
   */
  private[impl] def getEndTimeInSecondsForUnitTest(): Option[Float] = endTimeInSeconds

}
