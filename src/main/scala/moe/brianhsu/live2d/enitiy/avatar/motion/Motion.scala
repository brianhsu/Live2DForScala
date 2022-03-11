package moe.brianhsu.live2d.enitiy.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.model.Live2DModel

trait Motion {
  def fadeInTimeInSeconds: Float
  def fadeOutTimeInSeconds: Float
  def durationInSeconds: Option[Float]
  def calculateOperations(model: Live2DModel,
                          totalElapsedTimeInSeconds: Float,
                          deltaTimeInSeconds: Float,
                          weight: Float): List[EffectOperation]
}
