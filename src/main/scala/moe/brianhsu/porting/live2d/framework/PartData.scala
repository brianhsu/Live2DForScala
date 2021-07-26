package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.porting.live2d.framework.model.Live2DModel

class PartData {
  var PartId: String = null                ///< パーツID
  var Link: List[PartData] = Nil

  def Initialize(model: Live2DModel): Unit = {
    model.getParameterWithFallback(PartId).update(value = 1)
  }
}
