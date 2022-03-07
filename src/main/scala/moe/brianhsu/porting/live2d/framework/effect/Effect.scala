package moe.brianhsu.porting.live2d.framework.effect

import moe.brianhsu.live2d.enitiy.model.Live2DModel

trait Effect {
  def updateParameters(model: Live2DModel, deltaTimeSeconds: Float): Unit
}
