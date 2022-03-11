package moe.brianhsu.live2d.enitiy.avatar.motion

import moe.brianhsu.live2d.enitiy.avatar.effect.EffectOperation
import moe.brianhsu.live2d.enitiy.model.Live2DModel

trait Motion {
  def fadeInTimeInSeconds: Float
  def fadeOutTimeInSeconds: Float
  def calculateOperations(model: Live2DModel,
                          currentTimeInSeconds: Float,
                          deltaTimeInSeconds: Float,
                          weight: Float): List[EffectOperation]
}
