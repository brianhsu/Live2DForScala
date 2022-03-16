package moe.brianhsu.live2d.enitiy.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.model.Live2DModel

trait Motion {
  def isLoop: Boolean
  def isLoopFadeIn: Boolean
  def fadeInTimeInSeconds: Option[Float]
  def fadeOutTimeInSeconds: Float
  def durationInSeconds: Option[Float]
  def events: List[MotionEvent]
  def calculateOperations(model: Live2DModel,
                          totalElapsedTimeInSeconds: Float, deltaTimeInSeconds: Float, weight: Float,
                          startTimeInSeconds: Float, fadeInStartTimeInSeconds: Float,
                          endTimeInSeconds: Option[Float]): List[EffectOperation]
}