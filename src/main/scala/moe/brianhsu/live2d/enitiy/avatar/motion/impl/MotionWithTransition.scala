package moe.brianhsu.live2d.enitiy.avatar.motion.impl

import moe.brianhsu.live2d.enitiy.avatar.motion.impl.MotionWithTransition.Callback
import moe.brianhsu.live2d.enitiy.avatar.motion.{Motion, MotionEvent}
import moe.brianhsu.live2d.enitiy.math.Easing
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.usecase.updater.UpdateOperation

object MotionWithTransition {
  type Callback= (MotionWithTransition, MotionEvent) => Unit
}

class MotionWithTransition(val baseMotion: Motion) {

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

  def calculateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float): List[UpdateOperation] = {
    if (mIsFinished) {
      Nil
    } else {
      if (!isStarted) {
        this.isStarted = true
        this.startTimeInSeconds = totalElapsedTimeInSeconds
        this.fadeInStartTimeInSeconds = totalElapsedTimeInSeconds
        if (this.endTimeInSeconds.isEmpty && !baseMotion.isLoop && baseMotion.durationInSeconds.isDefined) {
          this.endTimeInSeconds = Some(this.startTimeInSeconds + baseMotion.durationInSeconds.get)
        }
      }

      val timeOffsetSeconds: Float = Math.max(totalElapsedTimeInSeconds - startTimeInSeconds, 0.0f)

      fireTriggeredEvents(totalElapsedTimeInSeconds)

      val operations = createUpdateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, weight, this.startTimeInSeconds, this.fadeInStartTimeInSeconds, this.endTimeInSeconds)
      if (baseMotion.durationInSeconds.nonEmpty && timeOffsetSeconds >= baseMotion.durationInSeconds.get) {
        if (baseMotion.isLoop) {
          this.startTimeInSeconds = totalElapsedTimeInSeconds
          if (baseMotion.isLoopFadeIn) {
            this.fadeInStartTimeInSeconds = totalElapsedTimeInSeconds
          }
        } else {
          // TODO: Motion Finished callback
          println(s"Motion $this finished")
        }
      }
      this.mIsFinished = this.endTimeInSeconds.exists(_ < totalElapsedTimeInSeconds)

      operations
    }
  }

  def setEventCallback(callback: Callback): Unit = {
    this.eventCallbackHolder = Some(callback)
  }

  def startFadeOut(totalElapsedTimeInSeconds: Float): Unit = {
    this.mIsForceToFadeOut = true
    val newEndTimeSeconds = totalElapsedTimeInSeconds + baseMotion.fadeOutTimeInSeconds.getOrElse(0.0f)
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
      eventCallback <- eventCallbackHolder
      triggeredEvent <- baseMotion.events.filter(_.shouldBeFired(this.lastEventCheckTimeInSeconds - startTimeInSeconds,
                                                      totalElapsedTimeInSeconds - startTimeInSeconds))
    } {
      eventCallback(this, triggeredEvent)
    }

    this.lastEventCheckTimeInSeconds = totalElapsedTimeInSeconds
  }

  private def createUpdateOperations(model: Live2DModel, totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float,
                                     startTimeInSeconds: Float, fadeInStartTimeInSeconds: Float,
                                     endTimeInSeconds: Option[Float]): List[UpdateOperation] = {
    val fadeIn: Float = calculateFadeIn(totalElapsedTimeInSeconds)
    val fadeOut: Float = calculateFadeOut(totalElapsedTimeInSeconds)
    val fadeWeight = weight * fadeIn * fadeOut

    baseMotion.calculateOperations(model, totalElapsedTimeInSeconds, deltaTimeInSeconds, fadeWeight, startTimeInSeconds, fadeInStartTimeInSeconds, endTimeInSeconds)
  }

  private def calculateFadeIn(totalElapsedTimeInSeconds: Float): Float = {
    baseMotion.fadeInTimeInSeconds
      .filter(_ > 0.0f)
      .map(fadeInTime => Easing.sine((totalElapsedTimeInSeconds - this.fadeInStartTimeInSeconds) / fadeInTime))
      .getOrElse(1.0f)
  }

  private def calculateFadeOut(totalElapsedTimeInSeconds: Float): Float = {
    val fadeOutHolder = for {
      _ <- endTimeInSeconds
      fadeOutTime <- baseMotion.fadeOutTimeInSeconds if fadeOutTime > 0.0f
    } yield {
      Easing.sine((this.endTimeInSeconds.get - totalElapsedTimeInSeconds) / fadeOutTime)
    }
    fadeOutHolder.getOrElse(1.0f)
  }

  /**
   * Get the value of endTimeInSeconds
   *
   * For unit test only.
   */
  private[impl] def getEndTimeInSecondsForUnitTest: Option[Float] = endTimeInSeconds

  /**
   * Get the value of startTimeInSeconds
   *
   * For unit test only.
   */
  private[impl] def getStartTimeInSecondsForUnitTest: Float = startTimeInSeconds

  /**
   * Get the value of fadeInStartTimeInSeconds
   *
   * For unit test only.
   */
  private[impl] def getFadeInStartTimeInSecondsForUnitTest: Float = fadeInStartTimeInSeconds

}
