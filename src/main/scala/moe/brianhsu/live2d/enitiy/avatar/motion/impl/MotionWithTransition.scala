package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.math.Easing
import moe.brianhsu.live2d.enitiy.model.Live2DModel

object MotionWithTransition {
  type Callback= (MotionWithTransition, MotionEvent) => Unit
}
class MotionWithTransition(motion: Motion) extends Motion {

  private var mIsFinished: Boolean = false
  private var isStarted: Boolean = false
  private var startTimeInSeconds: Float = -1.0f
  private var fadeInStartTimeInSeconds: Float = 0.0f
  private var endTimeInSeconds: Option[Float] = None
  private var shouldFadeOut: Boolean = false
  private var eventCallbackHolder: Option[Callback] = None
  private var lastEventCheckTimeInSeconds: Float = 0

  def setEventCallback(callback: Callback): Unit = {
    this.eventCallbackHolder = Some(callback)
  }

  def isFinished: Boolean = mIsFinished
  def isTriggeredFadeOut: Boolean = {
    shouldFadeOut && endTimeInSeconds.forall(_ < 0.0f)
  }

  def startFadeOut(userTimeSeconds: Float): Unit = {
    this.shouldFadeOut = true
    val newEndTimeSeconds = userTimeSeconds + fadeOutTimeInSeconds
    val newEndTimeLessThanOriginal = endTimeInSeconds.forall(endTime => endTime < 0.0f || newEndTimeSeconds < endTime)
    if (newEndTimeLessThanOriginal) {
      this.endTimeInSeconds = Some(newEndTimeSeconds)
    }
  }

  override def fadeInTimeInSeconds: Float = motion.fadeInTimeInSeconds
  override def fadeOutTimeInSeconds: Float = motion.fadeOutTimeInSeconds
  override def durationInSeconds: Option[Float] = motion.durationInSeconds
  override def events: List[MotionEvent] = Nil

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

  private def fireTriggeredEvents(totalElapsedTimeInSeconds: Float) = {
    for {
      eventCallback <- eventCallbackHolder
      duration <- durationInSeconds
      triggeredEvent <- events.filter(_.shouldBeFired(this.lastEventCheckTimeInSeconds - totalElapsedTimeInSeconds,
        totalElapsedTimeInSeconds - duration))
    } {
      eventCallback(this, triggeredEvent)
    }

    this.lastEventCheckTimeInSeconds = totalElapsedTimeInSeconds
  }

  private def createUpdateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float) = {
    val fadeIn: Float = calculateFadeIn(totalElapsedTimeInSeconds)
    val fadeOut: Float = calculateFadeOut(totalElapsedTimeInSeconds)
    val fadeWeight = weight * fadeIn * fadeOut

    assert(fadeWeight >= 0.0f && fadeWeight <= 1.0f, "fadeWeight inside MotionWithTransition is invalid")

    motion.calculateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, fadeWeight)
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

  def prepareToFadeOut(): Unit = {
    this.shouldFadeOut = true
  }
}
