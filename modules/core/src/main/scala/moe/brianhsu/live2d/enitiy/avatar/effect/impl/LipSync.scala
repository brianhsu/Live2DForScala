package moe.brianhsu.live2d.enitiy.avatar.effect.impl

import moe.brianhsu.live2d.enitiy.avatar.effect.Effect
import moe.brianhsu.live2d.enitiy.model.Live2DModel
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation
import moe.brianhsu.live2d.enitiy.updater.UpdateOperation.ParameterValueAdd


trait LipSync extends Effect {
  protected var weight: Float

  protected def lipSyncIds: List[String]
  protected def currentRms: Float

  override def calculateOperations(model: Live2DModel,
                                   totalElapsedTimeInSeconds: Float,
                                   deltaTimeInSeconds: Float): List[UpdateOperation] = {
    if (currentRms == 0) {
      Nil
    } else {
      lipSyncIds.map(ParameterValueAdd(_, currentRms, weight))
    }
  }

}
