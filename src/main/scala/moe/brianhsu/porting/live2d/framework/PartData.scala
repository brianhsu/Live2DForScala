package moe.brianhsu.porting.live2d.framework

import moe.brianhsu.porting.live2d.framework.model.Live2DModel

class PartData {
  var PartId: String = null                ///< パーツID
  var ParameterIndex: Int = -1         ///< パラメータのインデックス
  var PartIndex: Int = -1             ///< パーツのインデックス
  var Link: List[PartData] = Nil

  def Initialize(model: Live2DModel): Unit = {
    ParameterIndex = model.getParameterIndex(PartId)
    PartIndex = model.getPartIndex(PartId)
    model.setParameterValueUsingIndex(ParameterIndex, 1)

  }
}
