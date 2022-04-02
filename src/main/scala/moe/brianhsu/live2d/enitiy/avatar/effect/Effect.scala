package moe.brianhsu.live2d.enitiy.avatar.effect

import moe.brianhsu.live2d.enitiy.avatar.updater.UpdateOperation
import moe.brianhsu.live2d.enitiy.model.Live2DModel

trait Effect {
  def calculateOperations(model: Live2DModel,
                          totalElapsedTimeInSeconds: Float,
                          deltaTimeInSeconds: Float): List[UpdateOperation]
}
