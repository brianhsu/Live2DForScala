package moe.brianhsu.live2d.framework

import moe.brianhsu.live2d.framework.model.Live2DModel

class PartData {
  var PartId: String = null                ///< パーツID
  var ParameterIndex: Option[Int] = None         ///< パラメータのインデックス
  var PartIndex: Option[Int] = None             ///< パーツのインデックス
  var Link: List[PartData] = Nil

  def Initialize(model: Live2DModel): Unit = {
    ParameterIndex = model.getParameterIndex(PartId)
    PartIndex = model.getPartIndex(PartId)

    ParameterIndex.foreach { i =>
      val parameterId = model.parameterList(i).id
      model.setParameterValue(parameterId, 1)
    }

  }
}
