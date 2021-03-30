package moe.brianhsu.live2d.framework.effect

import moe.brianhsu.live2d.framework.model.Live2DModel

import scala.collection.mutable.ListBuffer

class Pose {
  val _partGroups: ListBuffer[PartData] = ListBuffer.empty                ///< パーツグループ
  val _partGroupCounts: ListBuffer[Int] = ListBuffer.empty           ///< それぞれのパーツグループの個数
  var _fadeTimeSeconds: Float = 0.0f           ///< フェード時間[秒]
  var _lastModel: Live2DModel = null                 ///< 前回操作したモデル


}
