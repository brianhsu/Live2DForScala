package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.framework.model.Live2DModel

class PartData {
  var PartId: String = null                ///< パーツID
  var ParameterIndex: Int = -1         ///< パラメータのインデックス
  var PartIndex: Int = -1             ///< パーツのインデックス
  var Link: List[PartData] = Nil

  def Initialize(model: Live2DModel): Unit = {
    println("======= Initialize[Start] ======")
    ParameterIndex = model.getParameterIndex(PartId)
    PartIndex = model.getPartIndex(PartId)
    printf("    ParameterIndex[%s] = %d\n", PartId, ParameterIndex);
    printf("    PartIndex[%s] = %d\n", PartId, PartIndex);
    printf("    SetParameterValue(%d, %f)\n", ParameterIndex, 1.0);
    model.setParameterValueUsingIndex(ParameterIndex, 1)
    println("======= Initialize[Done] ======")

  }
}
